import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenStorage } from '../auth/token.storage';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const storage = inject(TokenStorage);
  const token = storage.get();

  if (!token) return next(req);

  const authReq = req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  });

  return next(authReq);
};
