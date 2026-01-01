import { Component } from '@angular/core';

@Component({
  standalone: true,
  template: `
    <h3 class="text-vaq-primary fw-bold">Mis Videojuegos (Empresa)</h3>

    <div class="alert alert-danger">
      Para listar “mis videojuegos” necesitas backend: <b>GET /api/empresa/videojuegos</b>.
      Sin ese endpoint, esta pantalla solo puede ser “crear/editar/eliminar” si ya conoces IDs.
    </div>

    <div class="alert alert-secondary">
      Endpoints existentes: POST/PUT/DELETE /api/empresa/videojuegos
    </div>
  `
})
export class EmpresaVideojuegosComponent {}
