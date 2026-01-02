import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class GamerComprasService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  // POST /api/gamer/compras
  comprar(idVideojuego: number) {
    return this.http.post(`${this.base}/gamer/compras`, { idVideojuego });
  }
}
