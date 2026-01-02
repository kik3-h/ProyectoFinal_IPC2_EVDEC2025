import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface ResumenVentas {
  totalVentas: number;
  montoTotal: number;
  ingresoPlataforma: number;
  ingresoEmpresas: number;
}

@Injectable({ providedIn: 'root' })
export class AdminReportesService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  resumen(desde?: string, hasta?: string){
    const params: any = {};
    if (desde) params.desde = desde;
    if (hasta) params.hasta = hasta;
    return this.http.get<ResumenVentas>(`${this.base}/admin/reportes/ventas/resumen`, { params });
  }

  topJuegos(limit = 10){
    return this.http.get<any[]>(`${this.base}/admin/reportes/top-juegos`, { params: { limit } as any });
  }

  topEmpresas(limit = 10){
    return this.http.get<any[]>(`${this.base}/admin/reportes/top-empresas`, { params: { limit } as any });
  }
}
