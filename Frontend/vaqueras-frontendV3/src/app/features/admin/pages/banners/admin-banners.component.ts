import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminBannersService } from '../../../../core/services/admin-banners.service';
import { BannersService } from '../../../../core/services/banners.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h3 class="text-vaq-primary fw-bold">Banners</h3>

    @if (alert) { <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">{{alert.msg}}</div> }

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-2">
          <div class="col-md-4">
            <label class="form-label">URL destino</label>
            <input class="form-control" [(ngModel)]="form.url" name="url">
          </div>
          <div class="col-md-2">
            <label class="form-label">Posición</label>
            <input class="form-control" type="number" [(ngModel)]="form.posicion" name="posicion">
          </div>
          <div class="col-md-2 d-flex align-items-end">
            <button class="btn btn-vaq w-100" (click)="create()">Crear</button>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-3">
      @for (b of items; track b.idBanner) {
        <div class="col-12 col-md-6 col-lg-4">
          <div class="card shadow-sm h-100">
            <img [src]="publicBanners.bannerImgUrl(b.idBanner)" class="card-img-top" style="height:180px; object-fit:cover;">
            <div class="card-body">
              <div class="fw-bold">ID: {{b.idBanner}}</div>
              <div class="small text-muted">Pos: {{b.posicion}}</div>
              <a class="small" [href]="b.url" target="_blank">{{b.url}}</a>

              <div class="mt-2">
                <input type="file" class="form-control form-control-sm" (change)="onFile($event, b.idBanner)">
              </div>

              <div class="d-flex gap-2 mt-2">
                <button class="btn btn-sm btn-danger" (click)="remove(b.idBanner)">Eliminar</button>
                <button class="btn btn-sm btn-vaq" (click)="load()">Refrescar</button>
              </div>
            </div>
          </div>
        </div>
      }
    </div>
  `
})
export class AdminBannersComponent {
  private svc = inject(AdminBannersService);
  publicBanners = inject(BannersService);

  items: any[] = [];
  form: any = { url: '', posicion: 1 };
  alert: { type:'ok'|'err', msg:string } | null = null;

  ngOnInit() { this.load(); }

  load() {
    this.svc.list().subscribe({
      next: (d) => this.items = d ?? [],
      error: () => this.alert = { type:'err', msg:'Error cargando banners' }
    });
  }

  create() {
    this.svc.create(this.form).subscribe({
      next: () => { this.alert = { type:'ok', msg:'Banner creado' }; this.form = { url:'', posicion:1 }; this.load(); },
      error: () => this.alert = { type:'err', msg:'Error creando banner' }
    });
  }

  remove(id: number) {
    this.svc.delete(id).subscribe({
      next: () => { this.alert = { type:'ok', msg:'Banner eliminado' }; this.load(); },
      error: () => this.alert = { type:'err', msg:'Error eliminando banner' }
    });
  }

  onFile(ev: Event, idBanner: number) {
    const input = ev.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.svc.uploadImage(idBanner, file).subscribe({
      next: () => { this.alert = { type:'ok', msg:'Imagen subida' }; this.load(); },
      error: () => this.alert = { type:'err', msg:'Error subiendo imagen' }
    });
  }
}
