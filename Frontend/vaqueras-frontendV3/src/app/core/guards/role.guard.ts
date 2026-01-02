import { CanMatchFn, Route, UrlSegment } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { Role } from '../auth/auth.types';

export const roleGuard: CanMatchFn = (route: Route, segments: UrlSegment[]) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // Obtenemos el usuario actual
  const user = auth.user();

  // Si no hay usuario al Login
  if (!user) {
    return router.parseUrl('/login');
  }

  //Obtenemos los roles esperados desde la ruta
  const expectedRoles = (route.data?.['roles'] as Role[]) || [];

  // Si la ruta no pide roles, dejamos pasar
  if (expectedRoles.length === 0) return true;

  // Verificamos si el rol del usuario está en la lista permitida
  const hasRole = expectedRoles.includes(user.rol);

  // Si tiene rol -> Pasa. Si no -> Home o página de "Acceso Denegado"
  return hasRole ? true : router.parseUrl('/home');
};