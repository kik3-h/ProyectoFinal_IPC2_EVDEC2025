import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GamerPerfilService } from '../../../../core/services/gamer-perfil.service';
import { GamerPerfil } from '../../../../core/models/gamer.models';

@Component({
  selector: 'app-gamer-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gamer-perfil.component.html',
})
export class GamerPerfilComponent implements OnInit {
  loading = true;
  perfil: GamerPerfil = {};

  alert: { type: 'success' | 'danger'; msg: string } | null = null;

  constructor(private api: GamerPerfilService) {}

  ngOnInit(): void {
    this.api.obtener().subscribe({
      next: (p) => { this.perfil = p ?? {}; this.loading = false; },
      error: (e) => { this.loading = false; this.alert = { type:'danger', msg: this.errMsg(e) }; }
    });
  }

  guardar() {
    this.api.actualizar(this.perfil).subscribe({
      next: () => this.alert = { type:'success', msg:'Perfil actualizado.' },
      error: (e) => this.alert = { type:'danger', msg: this.errMsg(e) }
    });
  }

  private errMsg(e: any): string {
    return e?.error?.error ?? e?.error?.mensaje ?? e?.message ?? 'Error';
  }
}
