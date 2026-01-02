import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Grupo, Miembro } from '../models/gamer.models';

@Injectable({ providedIn: 'root' })
export class GamerGruposService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  // GET /api/gamer/grupos
  listar(): Observable<Grupo[]> {
    return this.http.get<any>(`${this.base}/gamer/grupos`).pipe(
      map((r) => Array.isArray(r) ? r : (r?.grupos ?? r?.data ?? []))
    );
  }

  // GET /api/gamer/grupos/miembros?grupoId=1
  miembros(grupoId: number): Observable<Miembro[]> {
    return this.http.get<any>(`${this.base}/gamer/grupos/miembros?grupoId=${grupoId}`).pipe(
      map((r) => Array.isArray(r) ? r : (r?.miembros ?? r?.data ?? []))
    );
  }

  // GET /api/gamer/grupos/juegos?grupoId=1
  juegos(grupoId: number) {
    return this.http.get<any>(`${this.base}/gamer/grupos/juegos?grupoId=${grupoId}`).pipe(
      map((r) => Array.isArray(r) ? r : (r?.juegos ?? r?.data ?? []))
    );
  }

  // POST /api/gamer/grupos
  crear(nombre: string) {
    return this.http.post(`${this.base}/gamer/grupos`, { nombre });
  }

  // POST /api/gamer/grupos/miembros
  agregarMiembro(grupoId: number, gamerId: number) {
    return this.http.post(`${this.base}/gamer/grupos/miembros`, { grupoId, gamerId });
  }

  // DELETE /api/gamer/grupos/miembros?grupoId=...&gamerId=...
  quitarMiembro(grupoId: number, gamerId: number) {
    return this.http.delete(`${this.base}/gamer/grupos/miembros?grupoId=${grupoId}&gamerId=${gamerId}`);
  }

  // POST /api/gamer/grupos/prestamos
  crearPrestamo(grupoId: number, gamerId: number, idVideojuego: number) {
    return this.http.post(`${this.base}/gamer/grupos/prestamos`, { grupoId, gamerId, idVideojuego });
  }

  // Si tu backend maneja instalación de préstamos, ajusta la ruta si difiere
  actualizarInstalacionPrestamo(grupoId: number, gamerId: number, idVideojuego: number, instalado: boolean) {
    return this.http.put(`${this.base}/gamer/grupos/prestamos/instalacion`, { grupoId, gamerId, idVideojuego, instalado });
  }

  // DELETE /api/gamer/grupos/prestamos?grupoId=...&gamerId=...&idVideojuego=...
  devolverPrestamo(grupoId: number, gamerId: number, idVideojuego: number) {
    return this.http.delete(`${this.base}/gamer/grupos/prestamos?grupoId=${grupoId}&gamerId=${gamerId}&idVideojuego=${idVideojuego}`);
  }
}
