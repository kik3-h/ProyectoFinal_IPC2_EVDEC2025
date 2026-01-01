import { CanMatchFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { Role } from '../auth/auth.types';

export const roleGuard: CanMatchFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const roles = (route.data?.['roles'] as Role[]) ?? [];
  const me = auth.user();

  // Si aún no cargó user, intenta refrescar (rápido)
  if (!me) {
    // dejamos pasar SOLO si no pidieron roles específicos
    if (roles.length === 0) return true;
    return router.parseUrl('/home');
  }

  if (roles.length === 0) return true;
  return roles.includes(me.rol) ? true : router.parseUrl('/home');
};
