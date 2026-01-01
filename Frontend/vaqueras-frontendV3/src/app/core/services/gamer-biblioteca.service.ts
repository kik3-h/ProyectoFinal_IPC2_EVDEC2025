import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface ItemBiblioteca {
  idVideojuego: number;
  titulo: string;
  estadoInstalacion?: string; // lo que devuelva backend
}

@Injectable({ providedIn: 'root' })
export class GamerBibliotecaService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  list() { return this.http.get<ItemBiblioteca[]>(`${this.base}/gamer/biblioteca`); }
  setInstalacion(idVideojuego: number, estado: string) {
    return this.http.put(`${this.base}/gamer/biblioteca/${idVideojuego}/instalacion`, { estado });
  }
}
