import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export type MultimediaTipo = 'PORTADA' | 'GALERIA';

@Injectable({ providedIn: 'root' })
export class EmpresaMultimediaService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  upload(idVideojuego: number, tipo: MultimediaTipo, file: File) {
    const fd = new FormData();
    fd.append('idVideojuego', String(idVideojuego));
    fd.append('tipo', tipo);
    fd.append('file', file);

    // backend /api/empresa/multimedia (multipart)
    return this.http.post<{message:string; idMultimedia:number}>(`${this.base}/empresa/multimedia`, fd);
  }

  delete(idMultimedia: number) {
    // backend /api/empresa/multimedia/{id}
    return this.http.delete<{message:string}>(`${this.base}/empresa/multimedia/${idMultimedia}`);
  }

  imageUrl(idMultimedia: number, cacheBust?: number) {
    const t = cacheBust ? `?t=${cacheBust}` : '';
    return `${this.base}/multimedia/imagen/${idMultimedia}${t}`;
  }
}
