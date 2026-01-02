import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ComentariosService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  crearComentario(idVideojuego: number, body: { texto: string; estrellas: number }) {
    return this.http.post(`${this.base}/gamer/videojuegos/${idVideojuego}/comentarios`, body);
  }
}
