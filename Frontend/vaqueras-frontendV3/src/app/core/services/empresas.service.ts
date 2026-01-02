import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface EmpresaDTO {
  idEmpresa: number;
  nombreEmpresa: string;
}

@Injectable({ providedIn: 'root' })
export class EmpresasService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  list(){
    return this.http.get<EmpresaDTO[]>(`${this.base}/empresas/`);
  }
}
