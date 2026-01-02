import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  EmpresaVideojuegosService,
  VideojuegoEmpresaDTO,
  VideojuegoCreateRequest,
  VideojuegoUpdateRequest
} from '../../../../core/services/empresa-videojuegos.service';

@Component({
  selector: 'empresa-videojuegos-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h3 class="text-vaq-primary fw-bold mb-0">Mis Videojuegos</h3>
      <div class="d-flex gap-2">
        <button class="btn btn-sm btn-vaq" (click)="load()">Refrescar</button>
        <button class="btn btn-sm btn-vaq-secondary" (click)="toggleCreate()">
          {{ showCreate ? 'Cerrar' : 'Nuevo videojuego' }}
        </button>
      </div>
    </div>

    @if (alert) {
      <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">
        {{ alert.msg }}
      </div>
    }

    @if (showCreate) {
      <div class="card shadow-sm mb-3">
        <div class="card-body">
          <h5 class="fw-bold text-vaq-primary">Crear videojuego</h5>
          <div class="row g-2">
            <div class="col-md-4">
              <label class="form-label">Título</label>
              <input class="form-control" [(ngModel)]="createForm.titulo" name="ctitulo">
            </div>
            <div class="col-md-3">
              <label class="form-label">Precio</label>
              <input class="form-control" type="number" [(ngModel)]="createForm.precio" name="cprecio">
            </div>
            <div class="col-md-3">
              <label class="form-label">Clasificación</label>
              <input class="form-control" [(ngModel)]="createForm.clasificacionEdad" name="cclas">
            </div>
            <div class="col-md-2">
              <label class="form-label">Edad mínima</label>
              <input class="form-control" type="number" [(ngModel)]="createForm.edadMinima" name="cedad">
            </div>
            <div class="col-12">
              <label class="form-label">Descripción</label>
              <input class="form-control" [(ngModel)]="createForm.descripcion" name="cdesc">
            </div>
            <div class="col-12">
              <button class="btn btn-vaq" (click)="create()" [disabled]="creating">
                {{ creating ? 'Creando...' : 'Crear' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    }

    @if (editing) {
      <div class="card shadow-sm mb-3 border-start border-5" style="border-color: var(--vaq-secondary)!important;">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <h5 class="fw-bold text-vaq-secondary mb-0">Editando: {{ editing.titulo }}</h5>
            <button class="btn btn-sm btn-outline-secondary" (click)="cancelEdit()">Cerrar</button>
          </div>

          <div class="row g-2 mt-2">
            <div class="col-md-4">
              <label class="form-label">Título</label>
              <input class="form-control" [(ngModel)]="editForm.titulo" name="etitulo">
            </div>
            <div class="col-md-3">
              <label class="form-label">Precio</label>
              <input class="form-control" type="number" [(ngModel)]="editForm.precio" name="eprecio">
            </div>
            <div class="col-md-3">
              <label class="form-label">Clasificación</label>
              <input class="form-control" [(ngModel)]="editForm.clasificacionEdad" name="eclas">
            </div>
            <div class="col-md-2">
              <label class="form-label">Edad mínima</label>
              <input class="form-control" type="number" [(ngModel)]="editForm.edadMinima" name="eedad">
            </div>
            <div class="col-12">
              <label class="form-label">Descripción</label>
              <input class="form-control" [(ngModel)]="editForm.descripcion" name="edesc">
            </div>
            <div class="col-12 d-flex gap-2">
              <button class="btn btn-vaq" (click)="saveEdit()" [disabled]="saving">
                {{ saving ? 'Guardando...' : 'Guardar cambios' }}
              </button>
              <button class="btn btn-danger" (click)="suspend(editing.idVideojuego)">
                Suspender venta
              </button>
            </div>
          </div>
        </div>
      </div>
    }

    @if (loading) {
      <div class="alert alert-info">Cargando videojuegos...</div>
    } @else {
      @if (items.length === 0) {
        <div class="alert alert-secondary">No tienes videojuegos registrados.</div>
      } @else {
        <div class="table-responsive">
          <table class="table table-sm table-striped align-middle">
            <thead>
              <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Precio</th>
                <th>Estado</th>
                <th>Clasificación</th>
                <th>Edad</th>
                <th style="width: 220px;">Acciones</th>
              </tr>
            </thead>
            <tbody>
              @for (v of items; track v.idVideojuego) {
                <tr>
                  <td>{{ v.idVideojuego }}</td>
                  <td class="fw-bold">{{ v.titulo }}</td>
                  <td>{{ v.precio | currency:'GTQ' }}</td>
                  <td>
                    <span class="badge" [class.bg-success]="(v.estado||'ACTIVO')==='ACTIVO'" [class.bg-secondary]="(v.estado||'')!=='ACTIVO'">
                      {{ v.estado || 'ACTIVO' }}
                    </span>
                  </td>
                  <td>{{ v.clasificacionEdad || 'N/A' }}</td>
                  <td>{{ v.edadMinima ?? 'N/A' }}</td>
                  <td class="d-flex gap-2">
                    <button class="btn btn-sm btn-vaq" (click)="startEdit(v)">Editar</button>
                    <button class="btn btn-sm btn-outline-secondary" (click)="detail(v.idVideojuego)">Detalle</button>
                    <button class="btn btn-sm btn-danger" (click)="suspend(v.idVideojuego)">Suspender</button>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>

        @if (detailJson) {
          <div class="card shadow-sm mt-3">
            <div class="card-body">
              <h5 class="text-vaq-primary fw-bold">Detalle (JSON)</h5>
              <pre class="mb-0" style="white-space: pre-wrap;">{{ detailJson }}</pre>
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

  showCreate = false;
  creating = false;

  saving = false;
  editing: VideojuegoEmpresaDTO | null = null;

  alert: { type:'ok'|'err', msg: string } | null = null;
  detailJson = '';

  createForm: VideojuegoCreateRequest = {
    titulo: '',
    precio: 0,
    descripcion: '',
    clasificacionEdad: '',
    edadMinima: 0
  };

  editForm: VideojuegoUpdateRequest = {};

  ngOnInit() { this.load(); }

  load() {
    this.alert = null;
    this.detailJson = '';
    this.loading = true;

    this.svc.listMine().subscribe({
      next: (d) => { this.items = d ?? []; this.loading = false; },
      error: (e) => {
        this.items = [];
        this.loading = false;
        this.alert = { type:'err', msg: e?.error?.error || 'Error cargando mis videojuegos' };
      }
    });
  }

  toggleCreate() { this.showCreate = !this.showCreate; }

  create() {
    this.alert = null;
    this.creating = true;

    this.svc.create(this.createForm).subscribe({
      next: () => {
        this.creating = false;
        this.alert = { type:'ok', msg:'Videojuego creado' };
        this.showCreate = false;
        this.createForm = { titulo:'', precio:0, descripcion:'', clasificacionEdad:'', edadMinima:0 };
        this.load();
      },
      error: (e) => {
        this.creating = false;
        this.alert = { type:'err', msg: e?.error?.error || 'Error creando videojuego' };
      }
    });
  }

  startEdit(v: VideojuegoEmpresaDTO) {
    this.editing = v;
    this.editForm = {
      titulo: v.titulo,
      precio: v.precio,
      clasificacionEdad: v.clasificacionEdad,
      edadMinima: v.edadMinima
    };
  }

  cancelEdit() {
    this.editing = null;
    this.editForm = {};
  }

  saveEdit() {
    if (!this.editing) return;
    this.alert = null;
    this.saving = true;

    this.svc.update(this.editing.idVideojuego, this.editForm).subscribe({
      next: () => {
        this.saving = false;
        this.alert = { type:'ok', msg:'Actualizado correctamente' };
        this.cancelEdit();
        this.load();
      },
      error: (e) => {
        this.saving = false;
        this.alert = { type:'err', msg: e?.error?.error || 'Error actualizando' };
      }
    });
  }

  suspend(id: number) {
    this.alert = null;
    this.svc.suspend(id).subscribe({
      next: () => { this.alert = { type:'ok', msg:'Venta suspendida' }; this.load(); },
      error: (e) => this.alert = { type:'err', msg: e?.error?.error || 'Error suspendiendo' }
    });
  }

  detail(id: number) {
    this.alert = null;
    this.svc.detailMine(id).subscribe({
      next: (d) => this.detailJson = JSON.stringify(d, null, 2),
      error: (e) => this.alert = { type:'err', msg: e?.error?.error || 'Error obteniendo detalle' }
    });
  }
}
