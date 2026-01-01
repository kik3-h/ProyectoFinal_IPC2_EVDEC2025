import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { TokenStorage } from '../auth/token.storage';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const storage = inject(TokenStorage);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        storage.clear();
        router.navigateByUrl('/login');
      }
      if (err.status === 403) {
        router.navigateByUrl('/home');
      }
      return throwError(() => err);
    })
  );
};
