import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class EmpresaMultimediaService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  upload(idVideojuego: number, tipo: 'PORTADA' | 'GALERIA', file: File) {
    const fd = new FormData();
    fd.append('idVideojuego', String(idVideojuego));
    fd.append('tipo', tipo);
    fd.append('file', file);
    return this.http.post(`${this.base}/empresa/multimedia`, fd);
  }

  delete(idMultimedia: number) {
    return this.http.delete(`${this.base}/empresa/multimedia/${idMultimedia}`);
  }
}
