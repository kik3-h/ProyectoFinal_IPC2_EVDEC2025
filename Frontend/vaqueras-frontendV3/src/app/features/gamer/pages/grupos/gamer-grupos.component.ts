import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GamerGruposService } from '../../../../core/services/gamer-grupos.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h3 class="text-vaq-primary fw-bold">Grupos familiares</h3>

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-2">
          <div class="col-md-6">
            <label class="form-label">Nombre del grupo</label>
            <input class="form-control" [(ngModel)]="nombreGrupo" name="nombreGrupo">
          </div>
          <div class="col-md-3 d-flex align-items-end">
            <button class="btn btn-vaq w-100" (click)="crear()">Crear</button>
          </div>
        </div>
      </div>
    </div>

    <button class="btn btn-sm btn-vaq mb-2" (click)="load()">Refrescar</button>

    <ul class="list-group">
      @for (g of grupos; track g.idGrupo || g.id) {
        <li class="list-group-item d-flex justify-content-between align-items-center">
          <div>
            <div class="fw-bold">{{ g.nombreGrupo || g.nombre }}</div>
            <div class="small text-muted">ID: {{ g.idGrupo || g.id }}</div>
          </div>
        </li>
      }
    </ul>
  `
})
export class GamerGruposComponent {
  private svc = inject(GamerGruposService);

  nombreGrupo = '';
  grupos: any[] = [];

  ngOnInit() { this.load(); }

  load() {
    this.svc.listar().subscribe({ next: d => this.grupos = d ?? [], error: () => this.grupos = [] });
  }

  crear() {
    this.svc.crear(this.nombreGrupo.trim()).subscribe({ next: () => { this.nombreGrupo=''; this.load(); }});
  }
}
