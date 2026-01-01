import { Component } from '@angular/core';

@Component({
  standalone: true,
  template: `
    <h3 class="text-vaq-primary fw-bold">Comisiones</h3>
    <div class="alert alert-warning">
      Pendiente de UI completa. Endpoints: /api/admin/comisiones/global y /api/admin/comisiones/empresa/:id
    </div>
  `
})
export class AdminComisionesComponent {}
