import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminUsuariosService } from '../../../../core/services/admin-usuarios.service';

@Component({
  selector: 'admin-usuarios-page', // <- evita NG0912
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h3 class="text-vaq-primary fw-bold mb-0">Usuarios</h3>
      <button class="btn btn-sm btn-vaq" (click)="load()">Refrescar</button>
    </div>

    @if(alert){
      <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">
        {{alert.msg}}
      </div>
    }

    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-2">
          <div class="col-md-4">
            <label class="form-label">Buscar (correo/nickname)</label>
            <input class="form-control" [(ngModel)]="q" name="q" placeholder="texto...">
          </div>
          <div class="col-md-3">
            <label class="form-label">Rol</label>
            <select class="form-select" [(ngModel)]="rol" name="rol">
              <option value="">Todos</option>
              <option value="ADMIN">ADMIN</option>
              <option value="EMPRESA">EMPRESA</option>
              <option value="GAMER">GAMER</option>
            </select>
          </div>
          <div class="col-md-3">
            <label class="form-label">Estado</label>
            <select class="form-select" [(ngModel)]="estado" name="estado">
              <option value="">Todos</option>
              <option value="ACTIVO">ACTIVO</option>
              <option value="INACTIVO">INACTIVO</option>
              <option value="BLOQUEADO">BLOQUEADO</option>
              <option value="SUSPENDIDO">SUSPENDIDO</option>
            </select>
          </div>
          <div class="col-md-2 d-flex align-items-end">
            <button class="btn btn-vaq-secondary w-100" (click)="apply()">Aplicar</button>
          </div>
        </div>
      </div>
    </div>

    @if(loading){
      <div class="alert alert-info">Cargando usuarios...</div>
    } @else {
      <div class="table-responsive">
        <table class="table table-sm table-striped align-middle">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nickname</th>
              <th>Correo</th>
              <th>Rol</th>
              <th>Estado</th>
              <th style="width:280px">Acciones</th>
            </tr>
          </thead>
          <tbody>
            @for(u of view; track getId(u)){
              <tr>
                <td>{{getId(u)}}</td>
                <td class="fw-bold">{{u.nickname ?? u.nick ?? '—'}}</td>
                <td>{{u.correo ?? u.email ?? '—'}}</td>
                <td><span class="badge bg-vaq-primary">{{u.rol ?? u.role ?? '—'}}</span></td>
                <td>
                  <span class="badge"
                        [class.bg-success]="(u.estado ?? u.status) === 'ACTIVO'"
                        [class.bg-secondary]="(u.estado ?? u.status) !== 'ACTIVO'">
                    {{u.estado ?? u.status ?? '—'}}
                  </span>
                </td>
                <td class="d-flex gap-2">
                  <button class="btn btn-sm btn-outline-secondary" (click)="detail(getId(u))">Detalle</button>

                  <select class="form-select form-select-sm" [(ngModel)]="estadoDraft[getId(u)]" name="ed{{getId(u)}}">
                    <option value="ACTIVO">ACTIVO</option>
                    <option value="INACTIVO">INACTIVO</option>
                    <option value="BLOQUEADO">BLOQUEADO</option>
                    <option value="SUSPENDIDO">SUSPENDIDO</option>
                  </select>

                  <button class="btn btn-sm btn-vaq" (click)="saveEstado(getId(u))">Guardar</button>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>

      @if(detailJson){
        <div class="card shadow-sm mt-3">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <h5 class="text-vaq-primary fw-bold mb-0">Detalle (JSON)</h5>
              <button class="btn btn-sm btn-outline-secondary" (click)="detailJson=''">Cerrar</button>
            </div>
            <pre class="mb-0 mt-2" style="white-space:pre-wrap">{{detailJson}}</pre>
          </div>
        </div>
      }
    }
  `
})
export class AdminUsuariosComponent {
  private svc = inject(AdminUsuariosService);

  loading = true;
  all: any[] = [];
  view: any[] = [];

  q = '';
  rol = '';
  estado = '';

  estadoDraft: Record<number, string> = {};
  detailJson = '';

  alert: {type:'ok'|'err', msg:string} | null = null;

  ngOnInit(){ this.load(); }

  load(){
    this.alert = null;
    this.loading = true;
    this.svc.list().subscribe({
      next: (d) => {
        this.all = d ?? [];
        // preload drafts
        for (const u of this.all) {
          const id = this.getId(u);
          if (id) this.estadoDraft[id] = (u.estado ?? u.status ?? 'ACTIVO');
        }
        this.apply();
        this.loading = false;
      },
      error: (e) => {
        this.loading = false;
        this.alert = {type:'err', msg: e?.error?.error || 'Error cargando usuarios'};
      }
    });
  }

  getId(u: any): number {
    return Number(u?.idUser ?? u?.idUsuario ?? u?.id_usuario ?? u?.id ?? 0);
  }

  apply(){
    const q = this.q.trim().toLowerCase();
    this.view = this.all.filter(u => {
      const r = (u.rol ?? u.role ?? '').toString();
      const st = (u.estado ?? u.status ?? '').toString();
      const nick = (u.nickname ?? u.nick ?? '').toString().toLowerCase();
      const mail = (u.correo ?? u.email ?? '').toString().toLowerCase();

      const okQ = !q || nick.includes(q) || mail.includes(q);
      const okR = !this.rol || r === this.rol;
      const okS = !this.estado || st === this.estado;
      return okQ && okR && okS;
    });
  }

  detail(id: number){
    this.alert = null;
    this.svc.get(id).subscribe({
      next: (d) => this.detailJson = JSON.stringify(d, null, 2),
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error obteniendo detalle'}
    });
  }

  saveEstado(id: number){
    const estado = (this.estadoDraft[id] || '').trim();
    if (!estado) return;

    this.alert = null;
    this.svc.cambiarEstado(id, estado).subscribe({
      next: (u) => {
        this.alert = {type:'ok', msg:'Estado actualizado'};
        // refresca el item local
        const idx = this.all.findIndex(x => this.getId(x) === id);
        if (idx >= 0) this.all[idx] = u;
        this.apply();
      },
      error: (e) => this.alert = {type:'err', msg: e?.error?.error || 'Error cambiando estado'}
    });
  }
}
