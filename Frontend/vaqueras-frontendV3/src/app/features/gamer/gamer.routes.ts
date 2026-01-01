import { Routes } from '@angular/router';
import { GamerLayoutComponent } from './layout/gamer-layout.component';

export const GAMER_ROUTES: Routes = [
  {
    path: '',
    component: GamerLayoutComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'biblioteca' },
      { path: 'biblioteca', loadComponent: () => import('./pages/biblioteca/gamer-biblioteca.component').then(m => m.GamerBibliotecaComponent) },
      { path: 'cartera', loadComponent: () => import('./pages/cartera/gamer-cartera.component').then(m => m.GamerCarteraComponent) },
      { path: 'grupos', loadComponent: () => import('./pages/grupos/gamer-grupos.component').then(m => m.GamerGruposComponent) },
      { path: 'perfil', loadComponent: () => import('./pages/perfil/gamer-perfil.component').then(m => m.GamerPerfilComponent) },
    ]
  }
];
