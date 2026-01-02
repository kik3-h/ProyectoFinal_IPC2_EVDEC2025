import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface BannerAdminDTO {
  idBanner: number;
  urlDestino: string;
  posicion: number;
  mime?: string | null;
}

export interface BannerCreateDTO {
  urlDestino: string;
  posicion: number;
}

@Injectable({ providedIn: 'root' })
export class AdminBannersService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  list() {
    return this.http.get<BannerAdminDTO[]>(`${this.base}/admin/banners/`);
  }

  create(body: BannerCreateDTO) {
    return this.http.post<{message:string; idBanner:number}>(`${this.base}/admin/banners/`, body);
  }

  update(id: number, body: BannerCreateDTO) {
    return this.http.put<{message:string}>(`${this.base}/admin/banners/${id}`, body);
  }

  remove(id: number) {
    return this.http.delete<{message:string}>(`${this.base}/admin/banners/${id}`);
  }

  uploadImage(id: number, file: File) {
    const fd = new FormData();
    fd.append('file', file);
    return this.http.put<{message:string}>(`${this.base}/admin/banners/imagen/${id}`, fd);
  }

  // para <img src>
  publicImageUrl(id: number, cacheBust?: number) {
    const q = cacheBust ? `?t=${cacheBust}` : '';
    return `${this.base}/banners/imagen/${id}${q}`;
  }
}
