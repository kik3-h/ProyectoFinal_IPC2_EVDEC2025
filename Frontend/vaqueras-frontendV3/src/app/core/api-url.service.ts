import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiUrlService {
  /**
   * Normaliza URLs que vienen del backend:
   * - '/api/...' => se deja igual (proxy en dev)
   * - '/vaqueras-backend/api/...' => se convierte a '/api/...'
   * - 'http(s)://...' => se deja igual
   * - 'null/""' => null
   */
  resolve(url?: string | null): string | null {
    if (!url) return null;
    if (/^https?:\/\//i.test(url)) return url;

    // Si backend manda con context path:
    if (url.startsWith('/vaqueras-backend/api/')) {
      return url.replace('/vaqueras-backend', ''); // => '/api/...'
    }

    // Si backend manda directo:
    if (url.startsWith('/api/')) return url;

    // Fallback: arma absoluta con apiBaseUrl (por si prod no usa proxy)
    const base = environment.apiBaseUrl?.replace(/\/$/, '') ?? '';
    const path = url.startsWith('/') ? url : `/${url}`;
    return `${base}${path}`;
  }
}
