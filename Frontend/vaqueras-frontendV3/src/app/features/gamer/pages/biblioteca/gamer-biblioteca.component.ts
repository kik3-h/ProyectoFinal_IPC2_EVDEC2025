import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GamerBibliotecaService, ItemBiblioteca } from '../../../../core/services/gamer-biblioteca.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <h3 class="text-vaq-primary fw-bold">Biblioteca</h3>

    @if (loading) { <div class="alert alert-info">Cargando...</div> }

    <div class="table-responsive">
      <table class="table table-sm table-striped align-middle">
        <thead><tr><th>ID</th><th>Título</th><th>Instalación</th><th></th></tr></thead>
        <tbody>
          @for (i of items; track i.idVideojuego) {
            <tr>
              <td>{{i.idVideojuego}}</td>
              <td class="fw-bold">{{i.titulo}}</td>
              <td>{{i.estadoInstalacion || 'N/A'}}</td>
              <td class="text-end">
                <button class="btn btn-sm btn-vaq" (click)="set(i.idVideojuego, 'INSTALADO')">Instalar</button>
              </td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  `
})
export class GamerBibliotecaComponent {
  private svc = inject(GamerBibliotecaService);

  loading = true;
  items: ItemBiblioteca[] = [];

  ngOnInit() { this.load(); }

  load() {
    this.svc.list().subscribe({
      next: d => { this.items = d ?? []; this.loading = false; },
      error: () => { this.items = []; this.loading = false; }
    });
  }

  set(id: number, estado: string) {
    this.svc.setInstalacion(id, estado).subscribe({ next: () => this.load() });
  }
}
