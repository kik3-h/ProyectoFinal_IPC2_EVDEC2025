import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class GamerGruposService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  listar() { return this.http.get<any[]>(`${this.base}/gamer/grupos/`); }
  crear(nombreGrupo: string) { return this.http.post(`${this.base}/gamer/grupos/`, { nombreGrupo }); }

  listarMiembros(idGrupo: number) { return this.http.get<any[]>(`${this.base}/gamer/grupos/${idGrupo}/miembros`); }
  agregarMiembro(idGrupo: number, payload: { idUser?: number; nickname?: string }) {
    return this.http.post(`${this.base}/gamer/grupos/${idGrupo}/miembros`, payload);
  }

  prestar(idGrupo: number, payload: { idUserReceptor: number; idVideojuego: number }) {
    return this.http.post(`${this.base}/gamer/grupos/${idGrupo}/prestamos`, payload);
  }

  actualizarInstalacionPrestamo(idGrupo: number, idVideojuego: number, estado: string) {
    return this.http.put(`${this.base}/gamer/grupos/${idGrupo}/prestamos/${idVideojuego}/instalacion`, { estado });
  }

  devolverPrestamo(idGrupo: number, idVideojuego: number) {
    return this.http.delete(`${this.base}/gamer/grupos/${idGrupo}/prestamos/${idVideojuego}`);
  }
}
