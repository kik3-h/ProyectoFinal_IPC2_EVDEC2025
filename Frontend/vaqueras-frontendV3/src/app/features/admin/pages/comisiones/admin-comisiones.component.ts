import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminComisionesService } from '../../../../core/services/admin-comisiones.service';
import { EmpresasService, EmpresaDTO } from '../../../../core/services/empresas.service';

@Component({
  selector: 'admin-comisiones-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h3 class="text-vaq-primary fw-bold mb-3">Comisiones</h3>

  @if(alert){
    <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">
      {{alert.msg}}
    </div>
  }

  <div class="row g-3">
    <div class="col-md-6">
      <div class="card shadow-sm">
        <div class="card-body">
          <h5 class="fw-bold text-vaq-primary">Global</h5>

          <div class="d-flex gap-2 align-items-end">
            <div class="flex-grow-1">
              <label class="form-label">Porcentaje (%)</label>
              <input class="form-control" type="number" [(ngModel)]="globalPct">
            </div>
            <button class="btn btn-vaq" (click)="saveGlobal()">Guardar</button>
          </div>

          <button class="btn btn-sm btn-outline-secondary mt-2" (click)="loadGlobal()">Refrescar</button>
        </div>
      </div>
    </div>

    <div class="col-md-6">
      <div class="card shadow-sm">
        <div class="card-body">
          <h5 class="fw-bold text-vaq-secondary">Por empresa</h5>

          <label class="form-label">Empresa</label>
          <select class="form-select" [(ngModel)]="selectedEmpresaId" (change)="loadEmpresaPct()">
            <option [ngValue]="null">-- Selecciona --</option>
            @for(e of empresas; track e.idEmpresa){
              <option [ngValue]="e.idEmpresa">{{e.nombreEmpresa}} (ID {{e.idEmpresa}})</option>
            }
          </select>

          <div class="mt-2">
            <label class="form-label">Porcentaje (%) (vacío = null)</label>
            <input class="form-control" [(ngModel)]="empresaPctInput" placeholder="Ej: 12.5">
          </div>

          <div class="d-flex gap-2 mt-2">
            <button class="btn btn-vaq-secondary" (click)="saveEmpresa()">Guardar</button>
            <button class="btn btn-outline-secondary" (click)="loadEmpresaPct()">Refrescar</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  `
})
export class AdminComisionesComponent {
  private com = inject(AdminComisionesService);
  private emp = inject(EmpresasService);

  alert: {type:'ok'|'err', msg:string} | null = null;

  globalPct = 0;

  empresas: EmpresaDTO[] = [];
  selectedEmpresaId: number|null = null;
  empresaPctInput = '';

  ngOnInit(){
    this.loadGlobal();
    this.emp.list().subscribe({ next: d => this.empresas = d ?? [] });
  }

  loadGlobal(){
    this.com.getGlobal().subscribe({
      next: d => this.globalPct = d.porcentaje ?? 0,
      error: e => this.alert = {type:'err', msg: e?.error?.error || 'Error cargando global'}
    });
  }

  saveGlobal(){
    this.alert = null;
    this.com.setGlobal(Number(this.globalPct)).subscribe({
      next: () => this.alert = {type:'ok', msg:'Comisión global actualizada'},
      error: e => this.alert = {type:'err', msg: e?.error?.error || 'Error guardando global'}
    });
  }

  loadEmpresaPct(){
    if(!this.selectedEmpresaId) return;
    this.com.getEmpresa(this.selectedEmpresaId).subscribe({
      next: d => this.empresaPctInput = (d.porcentaje === null || d.porcentaje === undefined) ? '' : String(d.porcentaje),
      error: e => this.alert = {type:'err', msg: e?.error?.error || 'Error cargando comisión empresa'}
    });
  }

  saveEmpresa(){
    if(!this.selectedEmpresaId){
      this.alert = {type:'err', msg:'Selecciona una empresa'};
      return;
    }
    const v = this.empresaPctInput.trim();
    const pct = v === '' ? null : Number(v);

    this.com.setEmpresa(this.selectedEmpresaId, pct).subscribe({
      next: () => this.alert = {type:'ok', msg:'Comisión de empresa actualizada'},
      error: e => this.alert = {type:'err', msg: e?.error?.error || 'Error guardando comisión empresa'}
    });
  }
}

