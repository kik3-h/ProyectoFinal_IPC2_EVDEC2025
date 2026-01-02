import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export type EstadoUsuario = string; // en backend es String por alguna razon XD

@Injectable({ providedIn: 'root' })
export class AdminUsuariosService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  list() {
    return this.http.get<any[]>(`${this.base}/admin/usuarios/`);
  }

  get(id: number) {
    return this.http.get<any>(`${this.base}/admin/usuarios/${id}`);
  }

  cambiarEstado(id: number, estado: EstadoUsuario) {
    // backend: PUT /api/admin/usuarios/{id}/estado body: {"estado":"ACTIVO"}
    return this.http.put<any>(`${this.base}/admin/usuarios/${id}/estado`, { estado });
  }
}
