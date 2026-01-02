import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminCategoriasService, Categoria } from '../../../../core/services/admin-categorias.service';
import { AdminModeracionService } from '../../../../core/services/admin-moderacion.service';

@Component({
  selector: 'admin-moderacion-page', // <- evita NG0912
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h3 class="text-vaq-primary fw-bold mb-3">Moderación</h3>

    @if(alert){
      <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">
        {{alert.msg}}
      </div>
    }

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-2 align-items-end">
          <div class="col-md-4">
            <label class="form-label">ID Videojuego</label>
            <input class="form-control" type="number" [(ngModel)]="idVideojuego" name="idVideojuego">
          </div>
          <div class="col-md-4 d-flex gap-2">
            <button class="btn btn-vaq w-100" (click)="loadJuego()">Cargar juego</button>
            <button class="btn btn-outline-secondary w-100" (click)="loadCategorias()">Cargar categorías</button>
          </div>
          <div class="col-md-4 d-flex gap-2">
            <button class="btn btn-vaq-secondary w-100" (click)="guardar()" [disabled]="!juego">Guardar categorías</button>
          </div>
        </div>
      </div>
    </div>

    @if(juego){
      <div class="card shadow-sm mb-3">
        <div class="card-body">
          <h5 class="fw-bold text-vaq-primary mb-1">{{ juego.titulo ?? juego.nombre ?? ('Juego '+idVideojuego) }}</h5>
          <div class="text-muted small mb-2">{{ juego.descripcion ?? '—' }}</div>

          <div class="row g-3">
            <div class="col-md-6">
              <h6 class="fw-bold">Categorías disponibles</h6>

              @if(categorias.length===0){
                <div class="alert alert-secondary">No hay categorías cargadas.</div>
              } @else {
                <div class="border rounded p-2" style="max-height:280px; overflow:auto;">
                  @for(c of categorias; track c.idCategoria){
                    <div class="form-check">
                      <input class="form-check-input"
                             type="checkbox"
                             [checked]="selected.has(c.idCategoria!)"
                             (change)="toggle(c.idCategoria!)"
                             id="cat{{c.idCategoria}}">
                      <label class="form-check-label" for="cat{{c.idCategoria}}">
                        {{c.nombre}} <span class="text-muted small">(#{{c.idCategoria}})</span>
                      </label>
                    </div>
                  }
                </div>
              }
            </div>

            <div class="col-md-6">
              <h6 class="fw-bold">JSON del juego</h6>
              <pre class="mb-0" style="white-space:pre-wrap; max-height:280px; overflow:auto;">{{ juegoJson }}</pre>
            </div>
          </div>
        </div>
      </div>
    } @else {
      <div class="alert alert-warning">
        Carga un videojuego por ID para asignarle categorías y aprobarlas.
      </div>
    }
  `
})
export class AdminModeracionComponent {
  private catsSvc = inject(AdminCategoriasService);
  private modSvc = inject(AdminModeracionService);

  idVideojuego = 0;

  categorias: Categoria[] = [];
  selected = new Set<number>();

  juego: any = null;
  juegoJson = '';

  alert: {type:'ok'|'err', msg:string} | null = null;

  ngOnInit(){
    this.loadCategorias();
  }

  loadCategorias(){
    this.alert = null;
    this.catsSvc.list().subscribe({
      next: (d) => this.categorias = d ?? [],
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error cargando categorías'}
    });
  }

  loadJuego(){
    this.alert = null;
    if(!this.idVideojuego || this.idVideojuego < 1){
      this.alert = {type:'err', msg:'ID de videojuego inválido'};
      return;
    }
    this.modSvc.getJuego(this.idVideojuego).subscribe({
      next: (d) => {
        this.juego = d;
        this.juegoJson = JSON.stringify(d, null, 2);
        this.selected = new Set(this.extractCatIds(d));
      },
      error: (e) => {
        this.juego = null;
        this.juegoJson = '';
        this.alert = {type:'err', msg: e?.error?.error || 'Error cargando juego (verifica endpoint /api/videojuegos/{id})'};
      }
    });
  }

  toggle(idCat: number){
    if(this.selected.has(idCat)) this.selected.delete(idCat);
    else this.selected.add(idCat);
  }

  guardar(){
    this.alert = null;
    if(!this.idVideojuego || !this.juego){
      this.alert = {type:'err', msg:'Carga un juego primero'};
      return;
    }
    const categorias = Array.from(this.selected.values());
    this.modSvc.setCategorias(this.idVideojuego, categorias).subscribe({
      next: () => this.alert = {type:'ok', msg:'Categorías del juego actualizadas'},
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error guardando categorías'}
    });
  }

  // Intenta leer categorías desde varias formas posibles del DTO
  private extractCatIds(d: any): number[] {
    const arr =
      d?.categorias ??
      d?.categoriasAprobadas ??
      d?.categoriasPendientes ??
      [];

    if(Array.isArray(arr)){
      // puede venir como [{idCategoria:1,...}] o [1,2]
      return arr.map((x:any) => Number(x?.idCategoria ?? x?.id ?? x)).filter((n:number)=> Number.isFinite(n) && n>0);
    }
    return [];
  }
}

