import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AdminComisionesService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  getGlobal(){
    return this.http.get<{porcentaje:number}>(`${this.base}/admin/comisiones/global`);
  }
  setGlobal(porcentaje: number){
    return this.http.post(`${this.base}/admin/comisiones/global`, { porcentaje });
  }

  getEmpresa(idEmpresa: number){
    return this.http.get<{porcentaje:number|null}>(`${this.base}/admin/comisiones/empresa/${idEmpresa}`);
  }
  setEmpresa(idEmpresa: number, porcentaje: number | null){
    return this.http.post(`${this.base}/admin/comisiones/empresa/${idEmpresa}`, { porcentaje });
  }
}
