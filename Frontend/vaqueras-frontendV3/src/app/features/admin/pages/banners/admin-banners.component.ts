import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminBannersService, BannerAdminDTO } from '../../../../core/services/admin-banners.service';

@Component({
  selector: 'admin-banners-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="text-vaq-primary fw-bold mb-0">Banners</h3>
    <button class="btn btn-vaq-secondary" (click)="openCreate()">+ Nuevo banner</button>
  </div>

  @if(alert){
    <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">
      {{alert.msg}}
    </div>
  }

  @if(loading){
    <div class="alert alert-info">Cargando banners...</div>
  }

  <!-- Modal simple (sin JS de Bootstrap) -->
  @if(showCreate){
    <div class="position-fixed top-0 start-0 w-100 h-100" style="background:rgba(0,0,0,.45); z-index:9999;">
      <div class="container h-100 d-flex align-items-center justify-content-center">
        <div class="card w-100" style="max-width:720px;">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <h5 class="mb-0 text-vaq-primary fw-bold">Crear banner</h5>
              <button class="btn btn-sm btn-outline-secondary" (click)="closeCreate()">X</button>
            </div>

            <div class="row g-2 mt-2">
              <div class="col-md-8">
                <label class="form-label">URL destino</label>
                <input class="form-control" [(ngModel)]="createUrl" name="createUrl" placeholder="https://... o /tienda">
              </div>
              <div class="col-md-4">
                <label class="form-label">Posición</label>
                <input class="form-control" type="number" [(ngModel)]="createPos" name="createPos">
              </div>
              <div class="col-12">
                <label class="form-label">Imagen (PNG/JPG/WEBP)</label>
                <input class="form-control" type="file" (change)="onCreateFile($event)">
                <small class="text-muted">Se crea el banner y luego se sube la imagen.</small>
              </div>

              <div class="col-12 d-flex gap-2">
                <button class="btn btn-vaq" (click)="create()" [disabled]="savingCreate">
                  {{ savingCreate ? 'Creando...' : 'Crear' }}
                </button>
                <button class="btn btn-outline-secondary" (click)="closeCreate()">Cancelar</button>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  }

  @if(!loading){
    <div class="d-flex gap-2 mb-2">
      <button class="btn btn-sm btn-vaq" (click)="load()">Refrescar</button>
      <span class="text-muted small align-self-center">Tip: usa ↑ ↓ para reordenar.</span>
    </div>

    <div class="row g-3">
      @for(b of banners; track b.idBanner){
        <div class="col-md-4">
          <div class="card shadow-sm h-100">
            <img class="card-img-top"
                 [src]="imgSrc(b)"
                 (error)="imgFallback($event)"
                 style="height:170px; object-fit:cover;"
            />
            <div class="card-body">
              <div class="d-flex justify-content-between">
                <div>
                  <div class="fw-bold">ID: {{b.idBanner}}</div>
                  <div class="text-muted small">Pos: {{b.posicion}}</div>
                </div>
                <div class="d-flex gap-1">
                  <button class="btn btn-sm btn-outline-secondary" title="Subir" (click)="moveUp(b)">↑</button>
                  <button class="btn btn-sm btn-outline-secondary" title="Bajar" (click)="moveDown(b)">↓</button>
                </div>
              </div>

              <label class="form-label mt-2">URL destino</label>
              <input class="form-control form-control-sm" [(ngModel)]="b.urlDestino" name="u{{b.idBanner}}">

              <label class="form-label mt-2">Posición</label>
              <input class="form-control form-control-sm" type="number" [(ngModel)]="b.posicion" name="p{{b.idBanner}}">

              <div class="d-flex gap-2 mt-2">
                <button class="btn btn-sm btn-vaq" (click)="save(b)">Guardar</button>
                <button class="btn btn-sm btn-danger" (click)="remove(b.idBanner)">Eliminar</button>
              </div>

              <hr>

              <label class="form-label">Actualizar imagen</label>
              <input class="form-control form-control-sm" type="file" (change)="onFile(b.idBanner, $event)">
              <button class="btn btn-sm btn-vaq-secondary mt-2 w-100" (click)="upload(b.idBanner)">
                Subir imagen
              </button>
            </div>
          </div>
        </div>
      }
    </div>
  }
  `
})
export class AdminBannersComponent {
  private svc = inject(AdminBannersService);

  loading = true;
  banners: BannerAdminDTO[] = [];

  alert: {type:'ok'|'err', msg:string} | null = null;

  // create modal
  showCreate = false;
  savingCreate = false;
  createUrl = '';
  createPos = 1;
  createFile: File | null = null;

  // cache bust por banner
  imgTs = new Map<number, number>();

  // file staging
  files = new Map<number, File>();

  ngOnInit(){ this.load(); }

  load(){
    this.alert = null;
    this.loading = true;

    this.svc.list().subscribe({
      next: (d) => {
        const arr = (d ?? []).slice();
        arr.sort((a,b) => (a.posicion ?? 0) - (b.posicion ?? 0));
        this.banners = arr;
        this.loading = false;
      },
      error: (e) => {
        this.loading = false;
        this.alert = {type:'err', msg: e?.error?.error || 'Error cargando banners'};
      }
    });
  }

  imgSrc(b: BannerAdminDTO){
    const t = this.imgTs.get(b.idBanner);
    return this.svc.publicImageUrl(b.idBanner, t);
  }

  imgFallback(ev: Event){
    const img = ev.target as HTMLImageElement;
    img.src = 'assets/banner-placeholder.jpg'; // crea este asset o cambia el nombre
  }

  openCreate(){
    this.createUrl = '';
    this.createFile = null;
    this.createPos = this.nextPos();
    this.showCreate = true;
  }
  closeCreate(){ this.showCreate = false; }

  onCreateFile(ev: Event){
    const input = ev.target as HTMLInputElement;
    this.createFile = input.files && input.files[0] ? input.files[0] : null;
  }

  nextPos(){
    const max = this.banners.reduce((m,b)=> Math.max(m, b.posicion ?? 0), 0);
    return max + 1;
  }

  create(){
    this.alert = null;
    if(!this.createUrl || this.createUrl.trim().length === 0){
      this.alert = {type:'err', msg:'URL destino es requerida'};
      return;
    }
    if(this.createPos < 1){
      this.alert = {type:'err', msg:'Posición debe ser >= 1'};
      return;
    }
    if(!this.createFile){
      this.alert = {type:'err', msg:'Debes seleccionar una imagen'};
      return;
    }

    this.savingCreate = true;

    this.svc.create({urlDestino: this.createUrl.trim(), posicion: this.createPos}).subscribe({
      next: (r) => {
        const id = r.idBanner;

        // subimos imagen
        this.svc.uploadImage(id, this.createFile!).subscribe({
          next: () => {
            this.savingCreate = false;
            this.showCreate = false;
            this.imgTs.set(id, Date.now());
            this.alert = {type:'ok', msg:'Banner creado y imagen subida'};
            this.load();
          },
          error: (e) => {
            this.savingCreate = false;
            this.alert = {type:'err', msg: e?.error?.error || 'Se creó el banner pero falló la imagen'};
            this.load();
          }
        });
      },
      error: (e) => {
        this.savingCreate = false;
        this.alert = {type:'err', msg: e?.error?.error || 'Error creando banner'};
      }
    });
  }

  save(b: BannerAdminDTO){
    this.alert = null;
    this.svc.update(b.idBanner, {urlDestino: b.urlDestino, posicion: b.posicion}).subscribe({
      next: () => {
        this.alert = {type:'ok', msg:'Banner actualizado'};
        this.load();
      },
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error actualizando banner'}
    });
  }

  remove(id: number){
    this.alert = null;
    this.svc.remove(id).subscribe({
      next: () => { this.alert = {type:'ok', msg:'Banner eliminado'}; this.load(); },
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error eliminando banner'}
    });
  }

  onFile(id: number, ev: Event){
    const input = ev.target as HTMLInputElement;
    const f = input.files && input.files[0] ? input.files[0] : null;
    if(f) this.files.set(id, f);
  }

  upload(id: number){
    this.alert = null;
    const f = this.files.get(id);
    if(!f){
      this.alert = {type:'err', msg:'Selecciona un archivo primero'};
      return;
    }
    this.svc.uploadImage(id, f).subscribe({
      next: () => {
        this.imgTs.set(id, Date.now());
        this.alert = {type:'ok', msg:'Imagen actualizada'};
      },
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error subiendo imagen'}
    });
  }

  moveUp(b: BannerAdminDTO){
    const idx = this.banners.findIndex(x => x.idBanner === b.idBanner);
    if(idx <= 0) return;
    this.swapPositions(idx, idx-1);
  }

  moveDown(b: BannerAdminDTO){
    const idx = this.banners.findIndex(x => x.idBanner === b.idBanner);
    if(idx < 0 || idx >= this.banners.length-1) return;
    this.swapPositions(idx, idx+1);
  }

  private swapPositions(i: number, j: number){
    const a = this.banners[i];
    const c = this.banners[j];
    const tmp = a.posicion;
    a.posicion = c.posicion;
    c.posicion = tmp;

    // Guardamos ambos
    this.svc.update(a.idBanner, {urlDestino:a.urlDestino, posicion:a.posicion}).subscribe({
      next: () => {
        this.svc.update(c.idBanner, {urlDestino:c.urlDestino, posicion:c.posicion}).subscribe({
          next: () => this.load(),
          error: () => this.load()
        });
      },
      error: () => this.load()
    });
  }
}
