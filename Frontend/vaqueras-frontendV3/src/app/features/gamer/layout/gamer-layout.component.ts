import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="d-flex flex-wrap gap-2 mb-3">
      <a class="btn btn-sm btn-vaq" routerLink="biblioteca" routerLinkActive="active">Biblioteca</a>
      <a class="btn btn-sm btn-vaq" routerLink="cartera" routerLinkActive="active">Cartera</a>
      <a class="btn btn-sm btn-vaq" routerLink="grupos" routerLinkActive="active">Grupos</a>
      <a class="btn btn-sm btn-vaq" routerLink="perfil" routerLinkActive="active">Perfil</a>
    </div>
    <router-outlet></router-outlet>
  `
})
export class GamerLayoutComponent {}
