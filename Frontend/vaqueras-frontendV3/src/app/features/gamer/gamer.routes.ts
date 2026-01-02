import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth.guard';
import { roleGuard } from '../../core/guards/role.guard';
import { GamerLayoutComponent } from './layout/gamer-layout.component';

export const GAMER_ROUTES: Routes = [
  {
    path: '',
    component: GamerLayoutComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['GAMER'] },
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'tienda' },

      { path: 'tienda', loadComponent: () => import('./pages/tienda/gamer-tienda.component').then(m => m.GamerTiendaComponent) },
      { path: 'tienda/:id', loadComponent: () => import('./pages/tienda/gamer-juego-detalle.component').then(m => m.GamerJuegoDetalleComponent) },

      { path: 'biblioteca', loadComponent: () => import('./pages/biblioteca/gamer-biblioteca.component').then(m => m.GamerBibliotecaComponent) },
      { path: 'cartera', loadComponent: () => import('./pages/cartera/gamer-cartera.component').then(m => m.GamerCarteraComponent) },
      { path: 'grupos', loadComponent: () => import('./pages/grupos/gamer-grupos.component').then(m => m.GamerGruposComponent) },
      { path: 'perfil', loadComponent: () => import('./pages/perfil/gamer-perfil.component').then(m => m.GamerPerfilComponent) },
    ],
  },
];

