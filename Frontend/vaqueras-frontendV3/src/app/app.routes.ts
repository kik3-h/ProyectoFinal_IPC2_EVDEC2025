import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },

  // PUBLIC
  { path: 'home', loadComponent: () => import('./features/public/home/home.component').then(m => m.HomeComponent) },
  { path: 'tienda', loadComponent: () => import('./features/public/store/store.component').then(m => m.StoreComponent) },
  { path: 'acerca', loadComponent: () => import('./features/public/about/about.component').then(m => m.AboutComponent) },

  // AUTH
  { path: 'login', canActivate: [guestGuard], loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'register', canActivate: [guestGuard], loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent) },

  // ROLES 
  {
    path: 'admin',
    canMatch: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadChildren: () => import('./features/admin/admin.routes').then(m => m.ADMIN_ROUTES),
  },
  {
    path: 'empresa',
    canMatch: [authGuard, roleGuard],
    data: { roles: ['EMPRESA'] },
    loadChildren: () => import('./features/empresa/empresa.routes').then(m => m.EMPRESA_ROUTES),
  },
  {
    path: 'gamer',
    canMatch: [authGuard, roleGuard],
    data: { roles: ['GAMER'] },
    loadChildren: () => import('./features/gamer/gamer.routes').then(m => m.GAMER_ROUTES),
  },

  { path: '**', redirectTo: 'home' },
];