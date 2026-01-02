import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideojuegosService } from '../../../core/services/videojuegos.service';
import { VideojuegoPublic } from '../../../core/models/videojuego.models';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2 class="text-vaq-primary fw-bold mb-0">Tienda</h2>
    </div>

    @if (loading) {
      <div class="alert alert-info">Cargando videojuegos...</div>
    } @else {
      @if (items.length === 0) {
        <div class="alert alert-secondary">No hay videojuegos disponibles.</div>
      } @else {
        <div class="row g-3">
          @for (v of items; track v.idVideojuego) {
            <div class="col-12 col-md-6 col-lg-4">
              <div class="card shadow-sm h-100">
                <div class="card-body">
                  <h5 class="fw-bold text-vaq-primary">{{ v.titulo }}</h5>
                  <p class="text-muted small">{{ v.nombreEmpresa || 'Sin descripción' }}</p>

                  <div class="d-flex justify-content-between align-items-center">
                    <span class="fw-bold">{{ (v.precio ?? 0) | currency:'GTQ' }}</span>
                    <span class="badge bg-vaq-secondary">{{ v.clasificacionEdad || 'N/A' }}</span>
                  </div>
                </div>
              </div>
            </div>
          }
        </div>
      }
    }
  `
})
export class StoreComponent {
  private svc = inject(VideojuegosService);

  loading = true;
  items: VideojuegoPublic[] = [];

  ngOnInit() {
    this.svc.listPublic().subscribe({
      next: (data) => { this.items = data ?? []; this.loading = false; },
      error: () => { this.items = []; this.loading = false; }
    });
  }
}
