import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EmpresaMultimediaService } from '../../../../core/services/empresa-multimedia.service';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <h3 class="text-vaq-primary fw-bold">Multimedia</h3>

    @if (alert) {
      <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">{{alert.msg}}</div>
    }

    <div class="card shadow-sm">
      <div class="card-body">
        <div class="row g-2">
          <div class="col-md-3">
            <label class="form-label">ID Videojuego</label>
            <input class="form-control" type="number" [(ngModel)]="idVideojuego" name="idVideojuego">
          </div>
          <div class="col-md-3">
            <label class="form-label">Tipo</label>
            <select class="form-select" [(ngModel)]="tipo" name="tipo">
              <option value="PORTADA">PORTADA</option>
              <option value="GALERIA">GALERIA</option>
            </select>
          </div>
          <div class="col-md-6">
            <label class="form-label">Archivo</label>
            <input type="file" class="form-control" (change)="onFile($event)">
          </div>
          <div class="col-12">
            <button class="btn btn-vaq" (click)="upload()" [disabled]="!file || !idVideojuego">Subir</button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class EmpresaMultimediaComponent {
  private svc = inject(EmpresaMultimediaService);

  idVideojuego = 0;
  tipo: 'PORTADA' | 'GALERIA' = 'PORTADA';
  file: File | null = null;

  alert: { type:'ok'|'err', msg:string } | null = null;

  onFile(ev: Event) {
    const input = ev.target as HTMLInputElement;
    this.file = input.files?.[0] ?? null;
  }

  upload() {
    if (!this.file || !this.idVideojuego) return;

    this.svc.upload(this.idVideojuego, this.tipo, this.file).subscribe({
      next: () => this.alert = { type:'ok', msg:'Multimedia subida correctamente' },
      error: () => this.alert = { type:'err', msg:'Error subiendo multimedia' }
    });
  }
}
