import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BannersService, Banner } from '../../../core/services/banners.service';

@Component({
  standalone: true,
  imports: [CommonModule , RouterLink],
  template: `
    <div class="mb-4 d-flex justify-content-between align-items-center">
      <div>
        <h2 class="text-vaq-primary fw-bold">Bienvenido al proyecto Final de kike</h2>
        <p class="text-muted mb-0">Tu tienda de videojuegos al estilo Steam.</p>
      </div>

      <a routerLink="/registro" class="btn btn-lg btn-vaq-secondary shadow-sm">
        ¡Crear cuenta gratis!
      </a>
    </div>

@if (loading) {
  <div class="alert alert-info">Cargando banners...</div>
} @else {
  @if (banners.length === 0) {
    <div class="alert alert-secondary">No hay banners activos.</div>
  } @else {
    <div id="carouselBanners" class="carousel slide" data-bs-ride="carousel">
      
      <!-- Indicadores del carrusel -->
      <div class="carousel-indicators">
        @for (b of banners; track b.idBanner; let i = $index) {
          <button type="button" data-bs-target="#carouselBanners" 
                  [attr.data-bs-slide-to]="i" 
                  [class.active]="i === 0"></button>
        }
      </div>

      <!-- Imágenes del carrusel -->
      <div class="carousel-inner rounded shadow-sm">
        @for (b of banners; track b.idBanner; let i = $index) {
          <div class="carousel-item" [class.active]="i === 0">
            <img class="d-block w-100" 
                 [src]="imgUrl(b.idBanner)" 
                 (error)="imgFallback($event)"
                 style="max-height:360px; object-fit:cover;">
          </div>
        }
      </div>

      <!-- Controles del carrusel -->
      <button class="carousel-control-prev" type="button" data-bs-target="#carouselBanners" data-bs-slide="prev">
        <span class="carousel-control-prev-icon"></span>
      </button>
      <button class="carousel-control-next" type="button" data-bs-target="#carouselBanners" data-bs-slide="next">
        <span class="carousel-control-next-icon"></span>
      </button>
    </div>
  }
}

<!-- Tarjetas de tienda y biblioteca -->
<div class="row mt-4 g-3">
  <div class="col-md-6">
    <div class="card shadow-sm h-100">
      <div class="card-body">
        <h5 class="text-vaq-primary fw-bold">Explora la Tienda</h5>
        <p class="text-muted">Busca videojuegos, detalles y compra.</p>
        <a class="btn btn-vaq" routerLink="/tienda">Ir a tienda</a>
      </div>
    </div>
  </div>
  <div class="col-md-6">
    <div class="card shadow-sm h-100">
      <div class="card-body">
        <h5 class="text-vaq-secondary fw-bold">Tu Biblioteca</h5>
        <p class="text-muted">Instala y administra tus juegos (GAMER).</p>
        <a class="btn btn-vaq-secondary" routerLink="/gamer/biblioteca">Ir a biblioteca</a>
      </div>
    </div>
  </div>
</div>

  `
})
export class HomeComponent {
  private bannersSvc = inject(BannersService);

  loading = true;
  banners: Banner[] = [];

  private readonly ts = Date.now();

  ngOnInit() {
    this.bannersSvc.listPublic().subscribe({
      next: (data) => {
        const arr = data ?? [];

        arr.sort((a, b) => {
            const posA = Number(a.posicion) || 0;
            const posB = Number(b.posicion) || 0;
            return posA - posB;
        });

        this.banners = arr; 
        this.loading = false;
      },
      error: () => { this.banners = []; this.loading = false; }
    });
  }

  imgUrl(id: number) { 
    return this.bannersSvc.bannerImgUrl(id) + '?t=' + this.ts;
  }

  // Fallback por si la imagen no carga
  imgFallback(ev: Event){
    const img = ev.target as HTMLImageElement;
    img.src = 'assets/banner-placeholder.jpg'; 
  }
}
