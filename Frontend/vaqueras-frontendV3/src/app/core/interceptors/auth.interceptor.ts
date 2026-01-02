import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenStorage } from '../auth/token.storage';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenStorage = inject(TokenStorage);
  const token = tokenStorage.get();
  let headers = req.headers;

  // Inyectar Token si existe
  if (token) {
    headers = headers.set('Authorization', `Bearer ${token}`);
  }

  //Manejo de Content-Type header
  const isFormData = req.body instanceof FormData;
  if (!isFormData && !headers.has('Content-Type')) {
    headers = headers.set('Content-Type', 'application/json');
  }

  // Clonar y pasar al siguiente manejador
  const clonedReq = req.clone({ headers });
  return next(clonedReq);
};
