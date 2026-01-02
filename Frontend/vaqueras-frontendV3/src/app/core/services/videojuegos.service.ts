import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Comentario, VideojuegoDetalle, VideojuegoPublic } from '../models/videojuego.models';
import { normalizeApiUrl } from '../utils/url.util';

@Injectable({ providedIn: 'root' })
export class VideojuegosService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  listPublic(): Observable<VideojuegoPublic[]> {
    return this.http.get<any>(`${this.base}/videojuegos`).pipe(
      map((r) => Array.isArray(r) ? r : (r?.videojuegos ?? r?.data ?? [])),
      map((arr: VideojuegoPublic[]) => arr.map(v => ({ ...v, portadaUrl: normalizeApiUrl(v.portadaUrl ?? null) })))
    );
  }

  detallePublico(id: number): Observable<VideojuegoDetalle> {
    return this.http.get<any>(`${this.base}/videojuegos/${id}`).pipe(
      map((r) => (r?.data ?? r)),
      map((d: VideojuegoDetalle) => ({ ...d, portadaUrl: normalizeApiUrl(d.portadaUrl ?? null) }))
    );
  }

  comentariosPublicos(id: number): Observable<Comentario[]> {
    return this.http.get<any>(`${this.base}/videojuegos/${id}/comentarios`).pipe(
      map((r) => Array.isArray(r) ? r : (r?.comentarios ?? r?.data ?? []))
    );
  }

  // POST /api/gamer/videojuegos/{id}/comentarios
  comentarComoGamer(id: number, body: { texto: string; estrellas: number }): Observable<any> {
    return this.http.post(`${this.base}/gamer/videojuegos/${id}/comentarios`, body);
  }
}
