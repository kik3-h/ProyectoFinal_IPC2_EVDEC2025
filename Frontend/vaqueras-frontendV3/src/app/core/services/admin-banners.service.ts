import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Banner } from './banners.service';

@Injectable({ providedIn: 'root' })
export class AdminBannersService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  list() { return this.http.get<Banner[]>(`${this.base}/admin/banners/`); }
  create(body: any) { return this.http.post(`${this.base}/admin/banners/`, body); }
  update(id: number, body: any) { return this.http.put(`${this.base}/admin/banners/${id}`, body); }
  delete(id: number) { return this.http.delete(`${this.base}/admin/banners/${id}`); }

  uploadImage(id: number, file: File) {
    const fd = new FormData();
    fd.append('file', file);
    return this.http.put(`${this.base}/admin/banners/imagen/${id}`, fd);
  }
}
