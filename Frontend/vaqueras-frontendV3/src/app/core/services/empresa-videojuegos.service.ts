import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface VideojuegoEmpresaDTO {
  idVideojuego: number;
  titulo: string;
  descripcion?: string;
  precio?: number;
  precioBase?: number; 

  estado?: string; // 'PENDIENTE', 'ACTIVO', 'SUSPENDIDO'
  clasificacionEdad?: string;
  edadMinima?: number;
  recursosMinimos?: string; 
  fechaPublicacion?: string;
  portadaUrl?: string;     
}

export interface VideojuegoCreateRequest {
  titulo: string;
  descripcion?: string;
  precio?: number;
  precioBase?: number;
  clasificacionEdad?: string;
  edadMinima?: number;
  recursosMinimos?: string;
  fechaPublicacion?: string; // Formato "YYYY-MM-DD"
}

// Partial hace que todos los campos de Create sean opcionales para Update
export interface VideojuegoUpdateRequest extends Partial<VideojuegoCreateRequest> {}

@Injectable({ providedIn: 'root' })
export class EmpresaVideojuegosService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  // Listar mis juegos
  listMine() {
    return this.http.get<VideojuegoEmpresaDTO[]>(`${this.base}/empresa/videojuegos/`);
  }

  // Detalle de un juego específico
  detailMine(id: number) {
    return this.http.get<VideojuegoEmpresaDTO>(`${this.base}/empresa/videojuegos/${id}`);
  }

  // Crear Devuelve el ID para luego subir la foto
  create(body: VideojuegoCreateRequest) {
    return this.http.post<{message:string; idVideojuego:number}>(`${this.base}/empresa/videojuegos/`, body);
  }

  // Actualizar info
  update(id: number, body: VideojuegoUpdateRequest) {
    return this.http.put<{message:string}>(`${this.base}/empresa/videojuegos/${id}`, body);
  }

  // Eliminar o suspender 
  suspend(id: number) {
    return this.http.delete<{message:string}>(`${this.base}/empresa/videojuegos/${id}`);
  }

  // Subir Portada 
  uploadCover(id: number, file: File) {
    const fd = new FormData();
    fd.append('file', file);
    return this.http.put<{message:string}>(`${this.base}/empresa/videojuegos/imagen/${id}`, fd);
  }

  // Generar URL de imagen para el HTML (con soporte para cache busting ?t=...)
  getCoverUrl(id: number): string {
    //return `${this.base}/empresa/videojuegos/imagen/${id}`;
    return `${this.base}/public/imagenes/${id}`;
  }
  // Metodo nuevo jss Eliminar permanentemente un videojuego
  deletePermanent(id: number) {
    // Enviamos el parámetro ?modo=PERMANENTE
    return this.http.delete<{message:string}>(`${this.base}/empresa/videojuegos/${id}?modo=PERMANENTE`);
  }
}