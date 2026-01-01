import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Banner {
  idBanner: number;
  url: string;
  posicion: number;
  activo: boolean;
}

@Injectable({ providedIn: 'root' })
export class BannersService {
  private base = environment.apiBaseUrl;
  constructor(private http: HttpClient) {}

  listPublic() {
    return this.http.get<Banner[]>(`${this.base}/banner`);
  }

  bannerImgUrl(idBanner: number) {
    return `${this.base}/banners/imagen/${idBanner}`;
  }
}
