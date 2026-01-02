import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { BibliotecaItem } from '../models/gamer.models';

@Injectable({ providedIn: 'root' })
export class BibliotecaService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  listarMiBiblioteca(): Observable<BibliotecaItem[]> {
    return this.http.get<any>(`${this.base}/gamer/biblioteca`).pipe(
      map(r => Array.isArray(r) ? r : (r?.biblioteca ?? r?.data ?? []))
    );
  }

  setInstalacion(idVideojuego: number, instalado: boolean): Observable<any> {
    return this.http.put(`${this.base}/gamer/biblioteca/instalacion/${idVideojuego}`, { instalado });
  }
}
