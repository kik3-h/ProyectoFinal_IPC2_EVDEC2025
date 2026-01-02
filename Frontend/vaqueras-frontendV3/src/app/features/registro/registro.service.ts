import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { RegistroUsuarioRequest } from './registro.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RegistroService {
  private base = environment.apiBaseUrl; // "/api"

  constructor(private http: HttpClient) {}

  crearCuenta(body: RegistroUsuarioRequest): Observable<any> {
    // POST /api/usuarios  -> proxy -> /vaqueras-backend/api/usuarios
    return this.http.post(`${this.base}/usuarios`, body);
  }
}
