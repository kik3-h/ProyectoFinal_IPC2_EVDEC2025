import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface RegistroUsuarioRequest {
  correo: string;
  password: string;
  nickname: string;
  // agrega campos si tu backend los requiere
}

@Injectable({ providedIn: 'root' })
export class UsuariosService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  register(body: RegistroUsuarioRequest) {
    return this.http.post(`${this.base}/usuarios`, body);
  }
}
