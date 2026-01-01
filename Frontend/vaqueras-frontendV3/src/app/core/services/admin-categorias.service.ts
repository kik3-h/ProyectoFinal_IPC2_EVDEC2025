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

  list() { return this.http.get<Categoria[]>(`${this.base}/admin/categorias/`); }
  create(body: Categoria) { return this.http.post(`${this.base}/admin/categorias/`, body); }
  update(id: number, body: Categoria) { return this.http.put(`${this.base}/admin/categorias/${id}`, body); }
  delete(id: number) { return this.http.delete(`${this.base}/admin/categorias/${id}`); }
}
