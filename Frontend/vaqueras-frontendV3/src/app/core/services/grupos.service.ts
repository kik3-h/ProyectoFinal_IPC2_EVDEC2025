import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Grupo {
  idGrupo: number;
  nombreGrupo: string;
  adminId?: number;
}

export interface Miembro {
  idUser: number;
  nickname: string;
}

export interface GrupoCreateRequest {
  
  nombreGrupo: string;
}

export interface MiembroAddRequest {
  
  nickname: string;
}

export interface PrestamoCreateRequest {
  idVideojuego: number;
  nicknameReceptor: string; 
}

export interface EstadoInstalacionRequest {
  estado: 'NO_INSTALADO' | 'INSTALADO';
}

@Injectable({ providedIn: 'root' })
export class GruposService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  listarMisGrupos(): Observable<Grupo[]> {
    return this.http.get<Grupo[]>(`${this.base}/gamer/grupos`, { withCredentials: true });
  }

  crearGrupo(body: GrupoCreateRequest): Observable<any> {
    return this.http.post(`${this.base}/gamer/grupos`, body, { withCredentials: true });
  }

  listarMiembros(grupoId: number): Observable<Miembro[]> {
    return this.http.get<Miembro[]>(`${this.base}/gamer/grupos/${grupoId}/miembros`, { withCredentials: true });
  }

  agregarMiembro(grupoId: number, body: MiembroAddRequest): Observable<any> {
    return this.http.post(`${this.base}/gamer/grupos/${grupoId}/miembros`, body, { withCredentials: true });
  }

  prestar(grupoId: number, body: any): Observable<any> {
   
    return this.http.post(`${this.base}/gamer/grupos/${grupoId}/prestamos`, body, { withCredentials: true });
  }

  cambiarInstalacion(grupoId: number, idVideojuego: number, body: EstadoInstalacionRequest): Observable<any> {
    return this.http.put(`${this.base}/gamer/grupos/${grupoId}/prestamos/${idVideojuego}/instalacion`, body, { withCredentials: true });
  }

  devolver(grupoId: number, idVideojuego: number): Observable<any> {
    return this.http.delete(`${this.base}/gamer/grupos/${grupoId}/prestamos/${idVideojuego}`, { withCredentials: true });
  }
}

