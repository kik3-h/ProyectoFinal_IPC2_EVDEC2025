import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminReportesService, ResumenVentas } from '../../../../core/services/admin-reportes.service';

@Component({
  selector: 'admin-reportes-page', 
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h3 class="text-vaq-primary fw-bold mb-0">Reportes</h3>
      <button class="btn btn-sm btn-vaq" (click)="loadAll()">Refrescar</button>
    </div>

    @if(alert){
      <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">
        {{alert.msg}}
      </div>
    }

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-2 align-items-end">
          <div class="col-md-3">
            <label class="form-label">Desde</label>
            <input class="form-control" type="date" [(ngModel)]="desde" name="desde">
          </div>
          <div class="col-md-3">
            <label class="form-label">Hasta</label>
            <input class="form-control" type="date" [(ngModel)]="hasta" name="hasta">
          </div>
          <div class="col-md-2">
            <label class="form-label">Top limit</label>
            <input class="form-control" type="number" [(ngModel)]="limit" name="limit">
          </div>
          <div class="col-md-4 d-flex gap-2">
            <button class="btn btn-vaq w-100" (click)="loadResumen()">Resumen</button>
            <button class="btn btn-vaq-secondary w-100" (click)="loadTops()">Top</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Resumen -->
    <div class="row g-3 mb-3">
      <div class="col-md-3">
        <div class="card shadow-sm">
          <div class="card-body">
            <div class="text-muted small">Total ventas</div>
            <div class="fs-4 fw-bold">{{ resumen?.totalVentas ?? 0 }}</div>
          </div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card shadow-sm">
          <div class="card-body">
            <div class="text-muted small">Monto total</div>
            <div class="fs-4 fw-bold">{{ (resumen?.montoTotal ?? 0) | currency:'GTQ' }}</div>
          </div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card shadow-sm">
          <div class="card-body">
            <div class="text-muted small">Ingreso plataforma</div>
            <div class="fs-4 fw-bold">{{ (resumen?.ingresoPlataforma ?? 0) | currency:'GTQ' }}</div>
          </div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card shadow-sm">
          <div class="card-body">
            <div class="text-muted small">Ingreso empresas</div>
            <div class="fs-4 fw-bold">{{ (resumen?.ingresoEmpresas ?? 0) | currency:'GTQ' }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-3">
      <div class="col-md-6">
        <div class="card shadow-sm">
          <div class="card-body">
            <h5 class="text-vaq-primary fw-bold">Top juegos</h5>
            <div class="table-responsive">
              <table class="table table-sm table-striped align-middle">
                <thead><tr><th>#</th><th>Juego</th><th>Ventas</th><th>Monto</th></tr></thead>
                <tbody>
                  @for(x of topJuegos; track $index){
                    <tr>
                      <td>{{$index+1}}</td>
                      <td class="fw-bold">{{ x.titulo ?? x.nombre ?? x.juego ?? ('ID ' + (x.idVideojuego ?? x.id ?? '—')) }}</td>
                      <td>{{ x.totalVentas ?? x.ventas ?? x.cantidad ?? '—' }}</td>
                      <td>{{ (x.montoTotal ?? x.monto ?? x.total ?? 0) | currency:'GTQ' }}</td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>

            @if(topJuegos.length===0){
              <div class="text-muted">Sin datos.</div>
            }
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="card shadow-sm">
          <div class="card-body">
            <h5 class="text-vaq-secondary fw-bold">Top empresas</h5>
            <div class="table-responsive">
              <table class="table table-sm table-striped align-middle">
                <thead><tr><th>#</th><th>Empresa</th><th>Ventas</th><th>Monto</th></tr></thead>
                <tbody>
                  @for(x of topEmpresas; track $index){
                    <tr>
                      <td>{{$index+1}}</td>
                      <td class="fw-bold">{{ x.nombreEmpresa ?? x.empresa ?? ('ID ' + (x.idEmpresa ?? x.id ?? '—')) }}</td>
                      <td>{{ x.totalVentas ?? x.ventas ?? x.cantidad ?? '—' }}</td>
                      <td>{{ (x.montoTotal ?? x.monto ?? x.total ?? 0) | currency:'GTQ' }}</td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>

            @if(topEmpresas.length===0){
              <div class="text-muted">Sin datos.</div>
            }
          </div>
        </div>
      </div>
    </div>
  `
})
export class AdminReportesComponent {
  private svc = inject(AdminReportesService);

  alert: {type:'ok'|'err', msg:string} | null = null;

  desde = '';
  hasta = '';
  limit = 10;

  resumen: ResumenVentas | null = null;
  topJuegos: any[] = [];
  topEmpresas: any[] = [];

  ngOnInit(){ this.loadAll(); }

  loadAll(){
    this.loadResumen();
    this.loadTops();
  }

  loadResumen(){
    this.alert = null;
    this.svc.resumen(this.desde || undefined, this.hasta || undefined).subscribe({
      next: (d) => this.resumen = d,
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error cargando resumen'}
    });
  }

  loadTops(){
    this.alert = null;
    const lim = Number(this.limit || 10);

    this.svc.topJuegos(lim).subscribe({
      next: (d) => this.topJuegos = d ?? [],
      error: () => this.topJuegos = []
    });

    this.svc.topEmpresas(lim).subscribe({
      next: (d) => this.topEmpresas = d ?? [],
      error: () => this.topEmpresas = []
    });
  }
}

