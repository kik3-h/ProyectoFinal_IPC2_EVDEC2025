import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {
  EmpresaVideojuegosService,
  VideojuegoEmpresaDTO,
  VideojuegoCreateRequest,
  VideojuegoUpdateRequest
} from '../../../../core/services/empresa-videojuegos.service';

@Component({
  selector: 'empresa-videojuegos-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h3 class="text-vaq-primary fw-bold mb-0">Mis Videojuegos</h3>
      <div class="d-flex gap-2">
        <button class="btn btn-sm btn-vaq" (click)="load()">Refrescar</button>
        <button class="btn btn-sm btn-vaq-secondary" (click)="showCreate=!showCreate">
          {{ showCreate ? 'Cerrar' : 'Nuevo videojuego' }}
        </button>
      </div>
    </div>

    @if(alert){
      <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">
        {{alert.msg}}
      </div>
    }

    @if(showCreate){
      <div class="card shadow-sm mb-3">
        <div class="card-body">
          <h5 class="fw-bold text-vaq-primary">Crear videojuego</h5>

          <div class="row g-2">
            <div class="col-md-6">
              <label class="form-label">Título</label>
              <input class="form-control" [(ngModel)]="create.titulo" name="ctitulo">
            </div>
            <div class="col-md-3">
              <label class="form-label">Precio</label>
              <input class="form-control" type="number" [(ngModel)]="create.precio" name="cprecio">
            </div>
            <div class="col-md-3">
              <label class="form-label">Clasificación</label>
              <input class="form-control" [(ngModel)]="create.clasificacionEdad" name="cclas" placeholder="E/T/M">
            </div>
            <div class="col-md-3">
              <label class="form-label">Edad mínima</label>
              <input class="form-control" type="number" [(ngModel)]="create.edadMinima" name="cedad">
            </div>
            <div class="col-md-3">
              <label class="form-label">Fecha publicación</label>
              <input class="form-control" type="date" [(ngModel)]="create.fechaPublicacion" name="cfecha">
            </div>
            <div class="col-md-6">
              <label class="form-label">Recursos mínimos</label>
              <input class="form-control" [(ngModel)]="create.recursosMinimos" name="crec">
            </div>
            <div class="col-12">
              <label class="form-label">Descripción</label>
              <textarea class="form-control" rows="2" [(ngModel)]="create.descripcion" name="cdesc"></textarea>
            </div>

            <div class="col-12">
              <button class="btn btn-vaq" (click)="crear()" [disabled]="creating">
                {{creating?'Creando...':'Crear'}}
              </button>
            </div>
          </div>
        </div>
      </div>
    }

    @if(editing){
      <div class="card shadow-sm mb-3 border-start border-5" style="border-color:var(--vaq-secondary)!important">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <h5 class="fw-bold text-vaq-secondary mb-0">Editando: {{editing.titulo}}</h5>
            <button class="btn btn-sm btn-outline-secondary" (click)="cancelEdit()">Cerrar</button>
          </div>

          <div class="row g-2 mt-2">
            <div class="col-md-6">
              <label class="form-label">Título</label>
              <input class="form-control" [(ngModel)]="edit.titulo" name="etitulo">
            </div>
            <div class="col-md-3">
              <label class="form-label">Precio</label>
              <input class="form-control" type="number" [(ngModel)]="edit.precio" name="eprecio">
            </div>
            <div class="col-md-3">
              <label class="form-label">Clasificación</label>
              <input class="form-control" [(ngModel)]="edit.clasificacionEdad" name="eclas">
            </div>

            <div class="col-md-3">
              <label class="form-label">Edad mínima</label>
              <input class="form-control" type="number" [(ngModel)]="edit.edadMinima" name="eedad">
            </div>
            <div class="col-md-3">
              <label class="form-label">Fecha publicación</label>
              <input class="form-control" type="date" [(ngModel)]="edit.fechaPublicacion" name="efecha">
            </div>
            <div class="col-md-6">
              <label class="form-label">Recursos mínimos</label>
              <input class="form-control" [(ngModel)]="edit.recursosMinimos" name="erec">
            </div>

            <div class="col-12">
              <label class="form-label">Descripción</label>
              <textarea class="form-control" rows="2" [(ngModel)]="edit.descripcion" name="edesc"></textarea>
            </div>

            <div class="col-12 d-flex gap-2">
              <button class="btn btn-vaq" (click)="guardar()" [disabled]="saving">
                {{saving?'Guardando...':'Guardar cambios'}}
              </button>
              <button class="btn btn-danger" (click)="suspender(editing.idVideojuego)">Suspender venta</button>
              <a class="btn btn-outline-secondary"
                 [routerLink]="['/empresa/multimedia']"
                 [queryParams]="{ juegoId: editing.idVideojuego }">
                Gestionar multimedia
              </a>
            </div>
          </div>
        </div>
      </div>
    }

    @if(loading){
      <div class="alert alert-info">Cargando videojuegos...</div>
    } @else {
      @if(items.length===0){
        <div class="alert alert-secondary">No tienes videojuegos aún.</div>
      } @else {
        <div class="row g-3">
          @for(v of items; track v.idVideojuego){
            <div class="col-md-4">
              <div class="card shadow-sm h-100">
                <img class="card-img-top"
                     style="height:170px; object-fit:cover;"
                     [src]="cover(v)"
                     (error)="onImgError($event)">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <div class="fw-bold">{{v.titulo}}</div>
                    <span class="badge" [class.bg-success]="(v.estado||'ACTIVO')==='ACTIVO'" [class.bg-secondary]="(v.estado||'')!=='ACTIVO'">
                      {{v.estado || 'ACTIVO'}}
                    </span>
                  </div>
                  <div class="text-muted small">ID: {{v.idVideojuego}}</div>
                  <div class="mt-2 fw-bold">{{ (v.precio ?? v.precioBase ?? 0) | currency:'GTQ' }}</div>

                  <div class="d-flex gap-2 mt-2">
                    <button class="btn btn-sm btn-vaq" (click)="startEdit(v)">Editar</button>
                    <button class="btn btn-sm btn-outline-secondary" (click)="detalle(v.idVideojuego)">Detalle</button>
                    <a class="btn btn-sm btn-vaq-secondary"
                       [routerLink]="['/empresa/multimedia']"
                       [queryParams]="{ juegoId: v.idVideojuego }">
                      Multimedia
                    </a>
                  </div>
                </div>
              </div>
            </div>
          }
        </div>

        @if(detailJson){
          <div class="card shadow-sm mt-3">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center">
                <h5 class="text-vaq-primary fw-bold mb-0">Detalle (JSON)</h5>
                <button class="btn btn-sm btn-outline-secondary" (click)="detailJson=''">Cerrar</button>
              </div>
              <pre class="mb-0 mt-2" style="white-space:pre-wrap">{{detailJson}}</pre>
            </div>
          </div>
        }
      }
    }
  `
})
export class EmpresaVideojuegosComponent {
  private svc = inject(EmpresaVideojuegosService);

  loading = true;
  items: VideojuegoEmpresaDTO[] = [];

  alert: {type:'ok'|'err', msg:string} | null = null;

  showCreate = false;
  creating = false;
  saving = false;

  editing: VideojuegoEmpresaDTO | null = null;
  detailJson = '';

  create: VideojuegoCreateRequest = {
    titulo: '',
    descripcion: '',
    precio: 0,
    clasificacionEdad: '',
    edadMinima: 0,
    recursosMinimos: '',
    fechaPublicacion: ''
  };

  edit: VideojuegoUpdateRequest = {};

  ngOnInit(){ this.load(); }

  load(){
    this.alert = null;
    this.detailJson = '';
    this.loading = true;

    this.svc.listMine().subscribe({
      next: (d) => { this.items = d ?? []; this.loading = false; },
      error: (e) => {
        this.items = [];
        this.loading = false;
        this.alert = {type:'err', msg: e?.error?.error || 'Error cargando mis videojuegos'};
      }
    });
  }

  cover(v: VideojuegoEmpresaDTO){
    const url = this.svc.getCoverUrl(v.idVideojuego);
    return url || 'assets/banner-placeholder.jpg';
  }

  onImgError(ev: Event){
    (ev.target as HTMLImageElement).src = 'assets/banner-placeholder.jpg';
  }

  crear(){
    this.alert = null;
    if(!this.create.titulo || this.create.titulo.trim().length===0){
      this.alert = {type:'err', msg:'Título requerido'};
      return;
    }
    this.creating = true;
    this.svc.create(this.create).subscribe({
      next: () => {
        this.creating = false;
        this.alert = {type:'ok', msg:'Videojuego creado'};
        this.showCreate = false;
        this.create = { titulo:'', descripcion:'', precio:0, clasificacionEdad:'', edadMinima:0, recursosMinimos:'', fechaPublicacion:'' };
        this.load();
      },
      error: (e) => {
        this.creating = false;
        this.alert = {type:'err', msg: e?.error?.error || 'Error creando videojuego'};
      }
    });
  }

  startEdit(v: VideojuegoEmpresaDTO){
    this.editing = v;
    this.edit = {
      titulo: v.titulo,
      descripcion: v.descripcion,
      precio: v.precio ?? v.precioBase,
      clasificacionEdad: v.clasificacionEdad,
      edadMinima: v.edadMinima,
      recursosMinimos: v.recursosMinimos,
      fechaPublicacion: v.fechaPublicacion
    };
  }

  cancelEdit(){
    this.editing = null;
    this.edit = {};
  }

  guardar(){
    if(!this.editing) return;
    this.alert = null;
    this.saving = true;

    this.svc.update(this.editing.idVideojuego, this.edit).subscribe({
      next: () => {
        this.saving = false;
        this.alert = {type:'ok', msg:'Actualizado correctamente'};
        this.cancelEdit();
        this.load();
      },
      error: (e) => {
        this.saving = false;
        this.alert = {type:'err', msg: e?.error?.error || 'Error actualizando'};
      }
    });
  }

  suspender(id: number){
    this.alert = null;
    this.svc.suspend(id).subscribe({
      next: () => { this.alert = {type:'ok', msg:'Venta suspendida'}; this.load(); },
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error suspendiendo'}
    });
  }

  detalle(id: number){
    this.alert = null;
    this.svc.detailMine(id).subscribe({
      next: (d) => this.detailJson = JSON.stringify(d, null, 2),
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error obteniendo detalle'}
    });
  }
}
