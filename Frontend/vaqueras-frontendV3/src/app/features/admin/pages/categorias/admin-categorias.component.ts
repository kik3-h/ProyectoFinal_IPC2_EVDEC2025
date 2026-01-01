import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminCategoriasService, Categoria } from '../../../../core/services/admin-categorias.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="d-flex justify-content-between align-items-center mb-2">
      <h3 class="text-vaq-primary fw-bold mb-0">Categorías</h3>
      <button class="btn btn-sm btn-vaq" (click)="load()">Refrescar</button>
    </div>

    @if (alert) { <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">{{alert.msg}}</div> }

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-2">
          <div class="col-md-4">
            <label class="form-label">Nombre</label>
            <input class="form-control" [(ngModel)]="form.nombre" name="nombre">
          </div>
          <div class="col-md-6">
            <label class="form-label">Descripción</label>
            <input class="form-control" [(ngModel)]="form.descripcion" name="descripcion">
          </div>
          <div class="col-md-2 d-flex align-items-end">
            <button class="btn btn-vaq w-100" (click)="create()">Crear</button>
          </div>
        </div>
      </div>
    </div>

    <div class="table-responsive">
      <table class="table table-sm table-striped align-middle">
        <thead>
          <tr><th>ID</th><th>Nombre</th><th>Descripción</th><th style="width:170px;">Acciones</th></tr>
        </thead>
        <tbody>
          @for (c of items; track c.idCategoria) {
            <tr>
              <td>{{c.idCategoria}}</td>
              <td><input class="form-control form-control-sm" [(ngModel)]="c.nombre" name="n{{c.idCategoria}}"></td>
              <td><input class="form-control form-control-sm" [(ngModel)]="c.descripcion" name="d{{c.idCategoria}}"></td>
              <td class="d-flex gap-2">
                <button class="btn btn-sm btn-vaq" (click)="update(c)">Guardar</button>
                <button class="btn btn-sm btn-danger" (click)="remove(c.idCategoria!)">Eliminar</button>
              </td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  `
})
export class AdminCategoriasComponent {
  private svc = inject(AdminCategoriasService);

  items: Categoria[] = [];
  form: Categoria = { nombre: '', descripcion: '' };
  alert: { type: 'ok' | 'err', msg: string } | null = null;

  ngOnInit() { this.load(); }

  load() {
    this.svc.list().subscribe({
      next: (d) => this.items = d ?? [],
      error: () => this.alert = { type: 'err', msg: 'Error cargando categorías' }
    });
  }

  create() {
    this.svc.create(this.form).subscribe({
      next: () => { this.alert = { type:'ok', msg:'Categoría creada' }; this.form = { nombre:'', descripcion:'' }; this.load(); },
      error: () => this.alert = { type:'err', msg:'Error creando categoría' }
    });
  }

  update(c: Categoria) {
    if (!c.idCategoria) return;
    this.svc.update(c.idCategoria, c).subscribe({
      next: () => this.alert = { type:'ok', msg:'Categoría actualizada' },
      error: () => this.alert = { type:'err', msg:'Error actualizando categoría' }
    });
  }

  remove(id: number) {
    this.svc.delete(id).subscribe({
      next: () => { this.alert = { type:'ok', msg:'Categoría eliminada' }; this.load(); },
      error: () => this.alert = { type:'err', msg:'Error eliminando categoría' }
    });
  }
}
