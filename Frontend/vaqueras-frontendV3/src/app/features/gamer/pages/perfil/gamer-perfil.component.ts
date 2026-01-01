import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GamerPerfilService } from '../../../../core/services/gamer-perfil.service';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <h3 class="text-vaq-primary fw-bold">Perfil</h3>

    @if (alert) { <div class="alert" [class.alert-success]="alert.type==='ok'" [class.alert-danger]="alert.type==='err'">{{alert.msg}}</div> }

    <div class="card shadow-sm">
      <div class="card-body">
        <label class="form-label">Nickname</label>
        <input class="form-control mb-2" [(ngModel)]="perfil.nickname" name="nickname">

        <label class="form-label">Correo</label>
        <input class="form-control mb-3" [(ngModel)]="perfil.correo" name="correo">

        <button class="btn btn-vaq" (click)="save()">Guardar</button>
      </div>
    </div>
  `
})
export class GamerPerfilComponent {
  private svc = inject(GamerPerfilService);

  perfil: any = {};
  alert: { type:'ok'|'err', msg:string } | null = null;

  ngOnInit() {
    this.svc.get().subscribe({
      next: d => this.perfil = d ?? {},
      error: () => this.alert = { type:'err', msg:'Error cargando perfil' }
    });
  }

  save() {
    this.svc.update(this.perfil).subscribe({
      next: () => this.alert = { type:'ok', msg:'Perfil actualizado' },
      error: () => this.alert = { type:'err', msg:'Error guardando perfil' }
    });
  }
}
