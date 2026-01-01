import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class GamerPerfilService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  get() { return this.http.get<any>(`${this.base}/gamer/perfil`); }
  update(body: any) { return this.http.put(`${this.base}/gamer/perfil`, body); }
}
