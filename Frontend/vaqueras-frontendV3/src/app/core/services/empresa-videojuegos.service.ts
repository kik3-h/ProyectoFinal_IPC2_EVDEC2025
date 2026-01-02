import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface VideojuegoEmpresaDTO {
  idVideojuego: number;
  titulo: string;
  precio: number;
  estado?: string;              // ACTIVO/SUSPENDIDO
  clasificacionEdad?: string;   // M, T, E
  edadMinima?: number;
  portadaUrl?: string;          
}

export interface VideojuegoCreateRequest {
  titulo: string;
  descripcion?: string;
  precio: number;
  clasificacionEdad?: string;
  edadMinima?: number;
}

export interface VideojuegoUpdateRequest {
  titulo?: string;
  descripcion?: string;
  precio?: number;
  clasificacionEdad?: string;
  edadMinima?: number;
}

@Injectable({ providedIn: 'root' })
export class EmpresaVideojuegosService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  listMine() {
    return this.http.get<VideojuegoEmpresaDTO[]>(`${this.base}/empresa/videojuegos/`);
  }

  detailMine(id: number) {
    return this.http.get<any>(`${this.base}/empresa/videojuegos/${id}`);
  }

  create(body: VideojuegoCreateRequest) {
    return this.http.post(`${this.base}/empresa/videojuegos/`, body);
  }

  update(id: number, body: VideojuegoUpdateRequest) {
    return this.http.put(`${this.base}/empresa/videojuegos/${id}`, body);
  }

  suspend(id: number) {
    return this.http.delete(`${this.base}/empresa/videojuegos/${id}`);
  }
}
