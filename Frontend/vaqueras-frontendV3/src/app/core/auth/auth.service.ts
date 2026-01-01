import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { catchError, map, of, tap } from 'rxjs';
import { LoginRequest, LoginResponse, SessionResponse, TokenUser } from './auth.types';
import { TokenStorage } from './token.storage';
import { Router } from '@angular/router';

function decodeJwtPayload(token: string): any | null {
  try {
    const payload = token.split('.')[1];
    const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(json);
  } catch { return null; }
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private base = environment.apiBaseUrl;
  private _user = signal<TokenUser | null>(null);

  user = this._user.asReadonly();

  constructor(
    private http: HttpClient,
    private storage: TokenStorage,
    private router: Router
  ) {
    // bootstrap rápido: si hay token, intentamos inferir rol por payload
    const t = this.storage.get();
    if (t) {
      const p = decodeJwtPayload(t);
      if (p?.rol && p?.idUser) this._user.set({ idUser: p.idUser, rol: p.rol, nickname: p.nickname, correo: p.correo });
    }
  }

  token(): string | null { return this.storage.get(); }

  isLoggedIn(): boolean {
    const t = this.token();
    if (!t) return false;
    const p = decodeJwtPayload(t);
    const exp = typeof p?.exp === 'number' ? p.exp * 1000 : 0;
    return exp ? Date.now() < exp : true; // si no hay exp, lo damos por válido
  }

  refreshSession() {
    return this.http.get<SessionResponse>(`${this.base}/auth/session`).pipe(
      tap(res => this._user.set(res.authenticated ? (res.user ?? null) : null)),
      catchError(() => {
        this._user.set(null);
        return of({ authenticated: false } as SessionResponse);
      })
    );
  }

  login(identifier: string, password: string) {
    const body = { identifier, password };

    return this.http.post<LoginResponse>(`${this.base}/auth/login`, body).pipe(
      tap(res => this.storage.set(res.token)),
      tap(() => {
        this.refreshSession().subscribe(() => {
          const u = this._user();
          if (u?.rol === 'ADMIN') this.router.navigateByUrl('/admin');
          else if (u?.rol === 'EMPRESA') this.router.navigateByUrl('/empresa');
          else this.router.navigateByUrl('/gamer');
        });
      })
    );
  }

  logout() {
    // revoke server-side (blacklist) + limpiar local
    return this.http.post(`${this.base}/auth/logout`, {}).pipe(
      catchError(() => of(null)),
      tap(() => {
        this.storage.clear();
        this._user.set(null);
        this.router.navigateByUrl('/home');
      })
    );
  }
}
