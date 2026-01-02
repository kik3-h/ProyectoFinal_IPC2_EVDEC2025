import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

import { VideojuegosService } from '../../../../core/services/videojuegos.service';
import { GamerBibliotecaService } from '../../../../core/services/gamer-biblioteca.service';
import { GamerComprasService } from '../../../../core/services/gamer-compras.service';
import { VideojuegoPublic } from '../../../../core/models/videojuego.models';

@Component({
  selector: 'app-gamer-tienda',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './gamer-tienda.component.html',
})
export class GamerTiendaComponent implements OnInit {
  loading = true;
  q = '';
  juegos: VideojuegoPublic[] = [];
  owned = new Set<number>();

  alert: { type: 'success' | 'danger'; msg: string } | null = null;

  constructor(
    private juegosApi: VideojuegosService,
    private biblioApi: GamerBibliotecaService,
    private comprasApi: GamerComprasService
  ) {}

  ngOnInit(): void {
    forkJoin({
      juegos: this.juegosApi.listPublic(),
      biblio: this.biblioApi.listar()
    }).subscribe({
      next: ({ juegos, biblio }) => {
        this.juegos = juegos;
        this.owned = new Set(biblio.map(x => x.idVideojuego));
        this.loading = false;
      },
      error: (e) => {
        this.loading = false;
        this.alert = { type: 'danger', msg: this.errMsg(e) };
      }
    });
  }

  get filtered(): VideojuegoPublic[] {
    const t = this.q.trim().toLowerCase();
    if (!t) return this.juegos;
    return this.juegos.filter(j => (j.titulo ?? '').toLowerCase().includes(t));
  }

  comprar(j: VideojuegoPublic) {
    this.alert = null;
    this.comprasApi.comprar(j.idVideojuego).subscribe({
      next: () => {
        this.owned.add(j.idVideojuego);
        this.alert = { type: 'success', msg: `Compra exitosa: ${j.titulo}` };
      },
      error: (e) => {
        this.alert = { type: 'danger', msg: this.errMsg(e) };
      }
    });
  }

  private errMsg(e: any): string {
    return e?.error?.error ?? e?.error?.mensaje ?? e?.message ?? 'Error';
  }
}
