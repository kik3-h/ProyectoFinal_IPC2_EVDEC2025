import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenStorage {
  // Definimos la clave una sola vez para evitar errores de dedo
  private readonly KEY = 'vaqueras_auth_token';

  set(token: string) {
    localStorage.setItem(this.KEY, token);
  }

  get(): string | null {
    return localStorage.getItem(this.KEY);
  }

  clear() {
    localStorage.removeItem(this.KEY);
  }
}
