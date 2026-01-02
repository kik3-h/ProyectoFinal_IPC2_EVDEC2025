import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { catchError, map, of, tap, lastValueFrom } from 'rxjs'; 
import { LoginRequest, LoginResponse, SessionResponse, TokenUser, Role } from './auth.types';
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
    this.syncStateFromStorage();
  }

  private syncStateFromStorage() {
    const t = this.storage.get();
    if (t) {
      const p = decodeJwtPayload(t);
      // Validamos que existan los datos mínimos
      if (p?.rol && p?.idUser) {
        this._user.set({ 
            idUser: p.idUser, 
            //Forzamos el tipo aquí también por seguridad
            rol: p.rol as Role, 
            nickname: p.nickname, 
            correo: p.correo || p.email // Backup por si en el token se llama email
        });
        return;
      }
    }
    this._user.set(null);
  }

  token(): string | null { return this.storage.get(); }

  isLoggedIn(): boolean {
    const t = this.token();
    if (!t) return false;
    const p = decodeJwtPayload(t);
    const exp = typeof p?.exp === 'number' ? p.exp * 1000 : 0;
    return exp ? Date.now() < exp : true;
  }

  bootstrapSession(): Promise<void> {
    return lastValueFrom(this.refreshSession().pipe(map(() => undefined)));
  }

  refreshSession() {
    if (!this.storage.get()) {
        this._user.set(null);
        return of({ authenticated: false } as SessionResponse);
    }

    return this.http.get<SessionResponse>(`${this.base}/auth/session`).pipe(
      tap(res => {
        if (res.authenticated && res.user) {
            this._user.set(res.user);
        } else {
            this.logoutClientSide(); 
        }
      }),
      catchError(() => {
        this.logoutClientSide();
        return of({ authenticated: false } as SessionResponse);
      })
    );
  }

  login(identifier: string, password: string) {
    const body = { identifier, password };

    return this.http.post<LoginResponse>(`${this.base}/auth/login`, body).pipe(
      tap(res => {
        this.storage.set(res.token);
        // Uso 'as Role' para decirle a TS que el string es un Rol válido
        this._user.set({
            idUser: res.idUser,
            nickname: res.nickname,
            correo: res.email, 
            rol: res.rol as Role 
        });
        
        console.log('✅ Login exitoso. Usuario:', this._user());
      }),
      tap(res => {
        // Normalizo a mayúsculas por si acaso
        const rol = (res.rol || '').toUpperCase(); 

        console.log('🔄 Redirigiendo según rol:', rol);

        if (rol === 'ADMIN') {
            this.router.navigateByUrl('/admin');
        } else if (rol === 'EMPRESA') {
            this.router.navigateByUrl('/empresa');
        } else {
            this.router.navigateByUrl('/gamer');
        }
      })
    );
  }

  logout() {
    return this.http.post(`${this.base}/auth/logout`, {}).pipe(
      catchError(() => of(null)),
      tap(() => this.logoutClientSide())
    );
  }

  private logoutClientSide() {
    this.storage.clear();
    this._user.set(null);
    this.router.navigateByUrl('/home');
  }
}