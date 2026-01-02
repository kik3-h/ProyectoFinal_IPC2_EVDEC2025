import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { GamerBibliotecaService } from '../../../../core/services/gamer-biblioteca.service';
import { BibliotecaItem } from '../../../../core/models/gamer.models';

@Component({
  selector: 'app-gamer-biblioteca',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './gamer-biblioteca.component.html',
})
export class GamerBibliotecaComponent implements OnInit {
  loading = true;
  items: BibliotecaItem[] = [];
  alert: { type: 'success' | 'danger'; msg: string } | null = null;

  constructor(private api: GamerBibliotecaService) {}

  ngOnInit(): void {
    this.refrescar();
  }

  refrescar() {
    this.loading = true;
    this.api.listar().subscribe({
      next: (r) => { this.items = r; this.loading = false; },
      error: (e) => { this.loading = false; this.alert = { type:'danger', msg: this.errMsg(e) }; }
    });
  }

  toggle(i: BibliotecaItem) {
    const next = !Boolean(i.instalado);
    this.api.setInstalado(i.idVideojuego, next).subscribe({
      next: () => {
        i.instalado = next;
        this.alert = { type:'success', msg: next ? 'Instalado' : 'Desinstalado' };
      },
      error: (e) => this.alert = { type:'danger', msg: this.errMsg(e) }
    });
  }

  private errMsg(e: any): string {
    return e?.error?.error ?? e?.error?.mensaje ?? e?.message ?? 'Error';
  }
}
