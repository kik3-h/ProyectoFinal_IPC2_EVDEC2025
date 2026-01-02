import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AdminModeracionService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  // PUT /api/admin/videojuegos/{id}/categorias  body: {"categorias":[...]}
  setCategorias(idVideojuego: number, categorias: number[]){
    return this.http.put(`${this.base}/admin/videojuegos/${idVideojuego}/categorias`, { categorias });
  }

  // para cargar detalle del juego por ID (público)
  getJuego(idVideojuego: number){
    return this.http.get<any>(`${this.base}/videojuegos/${idVideojuego}`);
  }
}
