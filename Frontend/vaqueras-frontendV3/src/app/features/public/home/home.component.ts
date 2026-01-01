import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BannersService, Banner } from '../../../core/services/banners.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="mb-4">
      <h2 class="text-vaq-primary fw-bold">Bienvenido a Vaqueras</h2>
      <p class="text-muted mb-0">Tu tienda de videojuegos estilo Steam.</p>
    </div>

    @if (loading) {
      <div class="alert alert-info">Cargando banners...</div>
    } @else {
      @if (banners.length === 0) {
        <div class="alert alert-secondary">No hay banners activos.</div>
      } @else {
        <div id="carouselBanners" class="carousel slide" data-bs-ride="carousel">
          <div class="carousel-inner rounded shadow-sm">
            @for (b of banners; track b.idBanner; let i = $index) {
              <div class="carousel-item" [class.active]="i === 0">
                <img class="d-block w-100" [src]="imgUrl(b.idBanner)" style="max-height:360px; object-fit:cover;">
              </div>
            }
          </div>

          <button class="carousel-control-prev" type="button" data-bs-target="#carouselBanners" data-bs-slide="prev">
            <span class="carousel-control-prev-icon"></span>
          </button>
          <button class="carousel-control-next" type="button" data-bs-target="#carouselBanners" data-bs-slide="next">
            <span class="carousel-control-next-icon"></span>
          </button>
        </div>
      }
    }

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

  ngOnInit() {
    this.bannersSvc.listPublic().subscribe({
      next: (data) => { this.banners = data ?? []; this.loading = false; },
      error: () => { this.banners = []; this.loading = false; }
    });
  }

  imgUrl(id: number) { return this.bannersSvc.bannerImgUrl(id); }
}
