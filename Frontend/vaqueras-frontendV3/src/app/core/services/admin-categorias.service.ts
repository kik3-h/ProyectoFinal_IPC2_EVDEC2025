import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Categoria {
  idCategoria?: number;
  nombre: string;
  descripcion?: string;
  activo?: boolean; 
}

@Injectable({ providedIn: 'root' })
export class AdminCategoriasService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  // Listar todas
  list() {
    return this.http.get<Categoria[]>(`${this.base}/admin/categorias/`);
  }

  // Crear
  create(body: Categoria) {
    return this.http.post<Categoria>(`${this.base}/admin/categorias/`, body);
  }

  // Actualizar
  update(id: number, body: Categoria) {
    return this.http.put<Categoria>(`${this.base}/admin/categorias/${id}`, body);
  }

  // Eliminar 
  remove(id: number) {
    return this.http.delete<{message: string}>(`${this.base}/admin/categorias/${id}`);
  }
}