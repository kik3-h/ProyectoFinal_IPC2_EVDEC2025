import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'empresa-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
  <div class="d-flex gap-2 mb-3 flex-wrap">
    <a class="btn btn-sm btn-vaq" routerLink="/empresa/videojuegos" routerLinkActive="active">Mis Videojuegos</a>
    <a class="btn btn-sm btn-vaq-secondary" routerLink="/empresa/multimedia" routerLinkActive="active">Multimedia</a>
  </div>

  <router-outlet></router-outlet>
  `
})
export class EmpresaLayoutComponent {}

