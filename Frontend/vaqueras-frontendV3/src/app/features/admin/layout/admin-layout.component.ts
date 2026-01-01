import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="d-flex flex-wrap gap-2 mb-3">
      <a class="btn btn-sm btn-vaq" routerLink="categorias" routerLinkActive="active">Categorías</a>
      <a class="btn btn-sm btn-vaq" routerLink="banners" routerLinkActive="active">Banners</a>
      <a class="btn btn-sm btn-vaq" routerLink="comisiones" routerLinkActive="active">Comisiones</a>
      <a class="btn btn-sm btn-vaq" routerLink="usuarios" routerLinkActive="active">Usuarios</a>
      <a class="btn btn-sm btn-vaq" routerLink="reportes" routerLinkActive="active">Reportes</a>
      <a class="btn btn-sm btn-vaq" routerLink="moderacion" routerLinkActive="active">Moderación</a>
    </div>
    <router-outlet></router-outlet>
  `
})
export class AdminLayoutComponent {}
