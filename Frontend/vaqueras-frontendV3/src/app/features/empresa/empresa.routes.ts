import { Routes } from '@angular/router';
import { EmpresaLayoutComponent } from './layout/empresa-layout.component';

export const EMPRESA_ROUTES: Routes = [
  {
    path: '',
    component: EmpresaLayoutComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'videojuegos' },
      { path: 'videojuegos', loadComponent: () => import('./pages/videojuegos/empresa-videojuegos.component').then(m => m.EmpresaVideojuegosComponent) },
      { path: 'multimedia', loadComponent: () => import('./pages/multimedia/empresa-multimedia.component').then(m => m.EmpresaMultimediaComponent) },
    ]
  }
];

