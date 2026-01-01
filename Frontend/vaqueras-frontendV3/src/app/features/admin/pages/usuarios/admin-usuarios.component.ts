import { Component } from '@angular/core';

@Component({
  standalone: true,
  template: `
    <h3 class="text-vaq-primary fw-bold">Usuarios</h3>
    <div class="alert alert-warning">
      Pendiente de UI completa. Endpoints: GET /api/admin/usuarios y PUT /api/admin/usuarios/:id/estado
    </div>
  `
})
export class AdminUsuariosComponent {}
