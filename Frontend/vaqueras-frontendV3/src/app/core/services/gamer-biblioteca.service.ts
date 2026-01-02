import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { BibliotecaItem } from '../models/gamer.models';
import { normalizeApiUrl } from '../utils/url.util';

@Injectable({ providedIn: 'root' })
export class GamerBibliotecaService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  // GET /api/gamer/biblioteca
  listar(): Observable<BibliotecaItem[]> {
    return this.http.get<any>(`${this.base}/gamer/biblioteca`).pipe(
      map((r) => Array.isArray(r) ? r : (r?.biblioteca ?? r?.data ?? [])),
      map((arr: BibliotecaItem[]) => arr.map(x => ({ ...x, portadaUrl: normalizeApiUrl(x.portadaUrl ?? null) })))
    );
  }

  // PUT /api/gamer/biblioteca/instalacion/{id}
  setInstalado(idVideojuego: number, instalado: boolean) {
    return this.http.put(`${this.base}/gamer/biblioteca/instalacion/${idVideojuego}`, { instalado });
  }

  instalar(idVideojuego: number) {
  const body = { idVideojuego, instalado: true };
  return this.http.put(`${this.base}/gamer/biblioteca/instalacion`, body, { withCredentials: true });
}
}
