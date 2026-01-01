import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GamerCarteraService } from '../../../../core/services/gamer-cartera.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h3 class="text-vaq-primary fw-bold">Cartera</h3>

    @if (alert) { <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">{{alert.msg}}</div> }

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <div class="text-muted small">Saldo actual</div>
            <div class="fs-4 fw-bold">{{ saldo | currency:'GTQ' }}</div>
          </div>
          <button class="btn btn-sm btn-vaq" (click)="load()">Refrescar</button>
        </div>
      </div>
    </div>

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-2">
          <div class="col-md-4">
            <label class="form-label">Monto a recargar</label>
            <input class="form-control" type="number" [(ngModel)]="monto" name="monto">
          </div>
          <div class="col-md-3 d-flex align-items-end">
            <button class="btn btn-vaq w-100" (click)="recargar()">Recargar</button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class GamerCarteraComponent {
  private svc = inject(GamerCarteraService);

  saldo = 0;
  monto = 0;
  alert: { type:'ok'|'err', msg:string } | null = null;

  ngOnInit() { this.load(); }

  load() {
    this.svc.getCartera().subscribe({
      next: d => this.saldo = d?.saldo ?? 0,
      error: () => this.alert = { type:'err', msg:'Error cargando cartera' }
    });
  }

  recargar() {
    this.alert = null;
    this.svc.recargar(this.monto).subscribe({
      next: () => { this.alert = { type:'ok', msg:'Recarga realizada' }; this.load(); },
      error: () => this.alert = { type:'err', msg:'Error en recarga' }
    });
  }
}
