import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { GamerPerfil } from '../models/gamer.models';

@Injectable({ providedIn: 'root' })
export class PerfilGamerService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  obtener(): Observable<GamerPerfil> {
    return this.http.get<any>(`${this.base}/gamer/perfil`).pipe(map(r => r?.data ?? r));
  }

  actualizar(body: Partial<GamerPerfil>) {
    return this.http.put(`${this.base}/gamer/perfil`, body);
  }
}
