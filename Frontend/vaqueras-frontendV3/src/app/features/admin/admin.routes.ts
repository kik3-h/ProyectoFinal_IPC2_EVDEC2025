import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layout/admin-layout.component';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'categorias' },
      { path: 'categorias', loadComponent: () => import('./pages/categorias/admin-categorias.component').then(m => m.AdminCategoriasComponent) },
      { path: 'banners', loadComponent: () => import('./pages/banners/admin-banners.component').then(m => m.AdminBannersComponent) },
      { path: 'comisiones', loadComponent: () => import('./pages/comisiones/admin-comisiones.component').then(m => m.AdminComisionesComponent) },
      { path: 'usuarios', loadComponent: () => import('./pages/usuarios/admin-usuarios.component').then(m => m.AdminUsuariosComponent) },
      { path: 'reportes', loadComponent: () => import('./pages/reportes/admin-reportes.component').then(m => m.AdminReportesComponent) },
      { path: 'moderacion', loadComponent: () => import('./pages/moderacion/admin-moderacion.component').then(m => m.AdminModeracionComponent) },
    ]
  }
];
