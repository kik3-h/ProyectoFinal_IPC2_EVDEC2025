import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { EmpresaMultimediaService, MultimediaTipo } from '../../../../core/services/empresa-multimedia.service';
import { EmpresaVideojuegosService } from '../../../../core/services/empresa-videojuegos.service';

@Component({
  selector: 'empresa-multimedia-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h3 class="text-vaq-primary fw-bold mb-0">Multimedia</h3>
      <button class="btn btn-sm btn-vaq" (click)="loadDetalle()">Refrescar</button>
    </div>

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

          <div class="col-md-3">
            <label class="form-label">Tipo</label>
            <select class="form-select" [(ngModel)]="tipo" name="tipo">
              <option value="PORTADA">PORTADA</option>
              <option value="GALERIA">GALERIA</option>
            </select>
          </div>

          <div class="col-md-5">
            <label class="form-label">Archivo</label>
            <input class="form-control" type="file" (change)="onFile($event)">
          </div>

          <div class="col-12 d-flex gap-2">
            <button class="btn btn-vaq-secondary" (click)="upload()" [disabled]="uploading">
              {{uploading?'Subiendo...':'Subir multimedia'}}
            </button>
            <button class="btn btn-outline-secondary" (click)="loadDetalle()">Cargar detalle del juego</button>
          </div>
        </div>
      </div>
    </div>

    @if(loading){
      <div class="alert alert-info">Cargando detalle...</div>
    }

    @if(juego){
      <div class="card shadow-sm mb-3">
        <div class="card-body">
          <h5 class="fw-bold text-vaq-primary mb-1">
            {{juego.titulo ?? juego.nombre ?? ('Juego '+idVideojuego)}}
          </h5>
          <div class="text-muted small mb-2">{{juego.descripcion ?? '—'}}</div>

          <div class="row g-3">
            <div class="col-md-6">
              <h6 class="fw-bold">Portada</h6>
              @if(portada.length===0){
                <div class="text-muted">No hay portada.</div>
              } @else {
                <div class="row g-2">
                  @for(m of portada; track m.id){
                    <div class="col-6">
                      <div class="card">
                        <img [src]="img(m.id)" style="height:150px; object-fit:cover;" (error)="imgErr($event)">
                        <div class="card-body p-2 d-flex justify-content-between align-items-center">
                          <span class="small text-muted">ID {{m.id}}</span>
                          <button class="btn btn-sm btn-danger" (click)="del(m.id)">Eliminar</button>
                        </div>
                      </div>
                    </div>
                  }
                </div>
              }
            </div>

            <div class="col-md-6">
              <h6 class="fw-bold">Galería</h6>
              @if(galeria.length===0){
                <div class="text-muted">No hay imágenes en galería.</div>
              } @else {
                <div class="row g-2">
                  @for(m of galeria; track m.id){
                    <div class="col-6">
                      <div class="card">
                        <img [src]="img(m.id)" style="height:150px; object-fit:cover;" (error)="imgErr($event)">
                        <div class="card-body p-2 d-flex justify-content-between align-items-center">
                          <span class="small text-muted">ID {{m.id}}</span>
                          <button class="btn btn-sm btn-danger" (click)="del(m.id)">Eliminar</button>
                        </div>
                      </div>
                    </div>
                  }
                </div>
              }
            </div>
          </div>

          <hr>
          <h6 class="fw-bold">JSON detalle</h6>
          <pre class="mb-0" style="white-space:pre-wrap; max-height:260px; overflow:auto;">{{juegoJson}}</pre>
        </div>
      </div>
    } @else {
      <div class="alert alert-warning">
        Ingresa un ID de videojuego y presiona “Cargar detalle del juego”.
      </div>
    }
  `
})
export class EmpresaMultimediaComponent {
  private route = inject(ActivatedRoute);
  private media = inject(EmpresaMultimediaService);
  private juegos = inject(EmpresaVideojuegosService);

  idVideojuego = 0;
  tipo: MultimediaTipo = 'PORTADA';
  file: File | null = null;

  uploading = false;
  loading = false;

  alert: {type:'ok'|'err', msg:string} | null = null;

  juego: any = null;
  juegoJson = '';

  portada: {id:number}[] = [];
  galeria: {id:number}[] = [];

  // cache bust por imagen
  bust = new Map<number, number>();

  ngOnInit(){
    const qid = this.route.snapshot.queryParamMap.get('juegoId');
    if (qid) this.idVideojuego = Number(qid);
    if (this.idVideojuego > 0) this.loadDetalle();
  }

  onFile(ev: Event){
    const input = ev.target as HTMLInputElement;
    this.file = input.files && input.files[0] ? input.files[0] : null;
  }

  upload(){
    this.alert = null;
    if (!this.idVideojuego || this.idVideojuego < 1){
      this.alert = {type:'err', msg:'ID de videojuego inválido'};
      return;
    }
    if (!this.file){
      this.alert = {type:'err', msg:'Selecciona un archivo'};
      return;
    }

    this.uploading = true;
    this.media.upload(this.idVideojuego, this.tipo, this.file).subscribe({
      next: () => {
        this.uploading = false;
        this.alert = {type:'ok', msg:'Multimedia subida'};
        this.loadDetalle();
      },
      error: (e) => {
        this.uploading = false;
        this.alert = {type:'err', msg: e?.error?.error || 'Error subiendo multimedia'};
      }
    });
  }

  loadDetalle(){
    this.alert = null;
    if (!this.idVideojuego || this.idVideojuego < 1){
      this.alert = {type:'err', msg:'ID de videojuego inválido'};
      return;
    }
    this.loading = true;

    // usamos tu endpoint: GET /api/empresa/videojuegos/{id} que reusa detallePublico()
    this.juegos.detailMine(this.idVideojuego).subscribe({
      next: (d) => {
        this.loading = false;
        this.juego = d;
        this.juegoJson = JSON.stringify(d, null, 2);

        const list = this.extractMultimedia(d);
        this.portada = list.filter(x => x.tipo === 'PORTADA').map(x => ({id:x.id}));
        this.galeria = list.filter(x => x.tipo === 'GALERIA').map(x => ({id:x.id}));
      },
      error: (e) => {
        this.loading = false;
        this.juego = null;
        this.juegoJson = '';
        this.portada = [];
        this.galeria = [];
        this.alert = {type:'err', msg: e?.error?.error || 'Error cargando detalle'};
      }
    });
  }

  del(idMultimedia: number){
    this.alert = null;
    this.media.delete(idMultimedia).subscribe({
      next: () => {
        this.alert = {type:'ok', msg:'Multimedia eliminada'};
        this.loadDetalle();
      },
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error eliminando multimedia'}
    });
  }

  img(id: number){
    const t = this.bust.get(id);
    return this.media.imageUrl(id, t);
  }

  imgErr(ev: Event){
    (ev.target as HTMLImageElement).src = 'assets/banner-placeholder.jpg';
  }

  private extractMultimedia(d: any): {id:number, tipo:'PORTADA'|'GALERIA'}[] {
    // intentamos varios nombres típicos
    const arr = d?.multimedia ?? d?.imagenes ?? d?.media ?? [];
    if (!Array.isArray(arr)) return [];

    return arr.map((x:any) => ({
      id: Number(x?.idMultimedia ?? x?.id ?? x),
      tipo: (x?.tipo ?? x?.type ?? 'GALERIA') as any
    })).filter((x:any) => Number.isFinite(x.id) && x.id > 0);
  }
}
