import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { CarteraResumen, RecargaItem } from '../models/gamer.models';

@Injectable({ providedIn: 'root' })
export class GamerCarteraService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  // GET /api/gamer/cartera
  resumen(): Observable<CarteraResumen> {
    return this.http.get<any>(`${this.base}/gamer/cartera`).pipe(map(r => r?.data ?? r));
  }

  // GET /api/gamer/cartera/recargas
  recargas(): Observable<RecargaItem[]> {
    return this.http.get<any>(`${this.base}/gamer/cartera/recargas`).pipe(
      map((r) => Array.isArray(r) ? r : (r?.recargas ?? r?.data ?? []))
    );
  }

  // POST /api/gamer/cartera/recargas
  recargar(monto: number) {
    return this.http.post(`${this.base}/gamer/cartera/recargas`, { monto });
  }
}
