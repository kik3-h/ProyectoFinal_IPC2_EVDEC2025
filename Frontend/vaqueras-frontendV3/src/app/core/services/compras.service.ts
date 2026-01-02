import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ComprasService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  comprar(idVideojuego: number) {
    return this.http.post(`${this.base}/gamer/compras`, { idVideojuego });
  }
}
