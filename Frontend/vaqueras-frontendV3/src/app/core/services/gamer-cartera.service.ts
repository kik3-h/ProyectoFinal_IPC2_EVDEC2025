import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Cartera { saldo: number; }
export interface Recarga { monto: number; fecha: string; }

@Injectable({ providedIn: 'root' })
export class GamerCarteraService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  getCartera() { return this.http.get<Cartera>(`${this.base}/gamer/cartera/`); }
  historial(limit = 50) { return this.http.get<Recarga[]>(`${this.base}/gamer/cartera/recargas?limit=${limit}`); }
  recargar(monto: number) { return this.http.post(`${this.base}/gamer/cartera/recargas`, { monto }); }
}
