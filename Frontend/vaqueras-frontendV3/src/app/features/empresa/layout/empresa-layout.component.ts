import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="d-flex flex-wrap gap-2 mb-3">
      <a class="btn btn-sm btn-vaq" routerLink="videojuegos" routerLinkActive="active">Videojuegos</a>
      <a class="btn btn-sm btn-vaq" routerLink="multimedia" routerLinkActive="active">Multimedia</a>
    </div>
    <router-outlet></router-outlet>
  `
})
export class EmpresaLayoutComponent {}
