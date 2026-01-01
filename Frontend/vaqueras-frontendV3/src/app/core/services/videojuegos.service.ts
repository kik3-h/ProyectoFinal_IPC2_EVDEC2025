import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Videojuego {
  idVideojuego: number;
  titulo: string;
  descripcion?: string;
  precio?: number;
  clasificacion?: string;
}

@Injectable({ providedIn: 'root' })
export class VideojuegosService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  listPublic() { return this.http.get<Videojuego[]>(`${this.base}/videojuegos`); }
  getById(id: number) { return this.http.get<Videojuego>(`${this.base}/videojuegos/${id}`); }

  imgUrl(idMultimedia: number) {
    return `${this.base}/multimedia/imagen/${idMultimedia}`;
  }
}
