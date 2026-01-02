import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

import { VideojuegosService } from '../../../../core/services/videojuegos.service';
import { GamerBibliotecaService } from '../../../../core/services/gamer-biblioteca.service';
import { GamerComprasService } from '../../../../core/services/gamer-compras.service';
import { Comentario, VideojuegoDetalle } from '../../../../core/models/videojuego.models';

@Component({
  selector: 'app-gamer-juego-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './gamer-juego-detalle.component.html',
})
export class GamerJuegoDetalleComponent implements OnInit {
  id = 0;
  loading = true;

  juego: VideojuegoDetalle | null = null;
  comentarios: Comentario[] = [];

  owned = false;

  texto = '';
  estrellas = 5;

  alert: { type: 'success' | 'danger'; msg: string } | null = null;

  constructor(
    private route: ActivatedRoute,
    private juegosApi: VideojuegosService,
    private biblioApi: GamerBibliotecaService,
    private comprasApi: GamerComprasService
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    forkJoin({
      detalle: this.juegosApi.detallePublico(this.id),
      comentarios: this.juegosApi.comentariosPublicos(this.id),
      biblio: this.biblioApi.listar()
    }).subscribe({
      next: ({ detalle, comentarios, biblio }) => {
        this.juego = detalle;
        this.comentarios = comentarios;
        this.owned = biblio.some(x => x.idVideojuego === this.id);
        this.loading = false;
      },
      error: (e) => {
        this.loading = false;
        this.alert = { type: 'danger', msg: this.errMsg(e) };
      }
    });
  }

  comprar() {
    if (!this.juego) return;
    this.alert = null;
    this.comprasApi.comprar(this.id).subscribe({
      next: () => {
        this.owned = true;
        this.alert = { type: 'success', msg: 'Compra realizada. Ya puedes comentar e instalar.' };
      },
      error: (e) => this.alert = { type: 'danger', msg: this.errMsg(e) }
    });
  }

  comentar() {
    if (!this.owned) {
      this.alert = { type: 'danger', msg: 'Debes comprar el juego para comentar.' };
      return;
    }
    if (!this.texto.trim()) {
      this.alert = { type: 'danger', msg: 'Escribe un comentario.' };
      return;
    }

    this.juegosApi.comentarComoGamer(this.id, { texto: this.texto.trim(), estrellas: this.estrellas }).subscribe({
      next: () => {
        this.alert = { type: 'success', msg: 'Comentario publicado.' };
        this.texto = '';
        this.estrellas = 5;
        // recargar comentarios
        this.juegosApi.comentariosPublicos(this.id).subscribe(c => this.comentarios = c);
      },
      error: (e) => this.alert = { type: 'danger', msg: this.errMsg(e) }
    });
  }

  private errMsg(e: any): string {
    return e?.error?.error ?? e?.error?.mensaje ?? e?.message ?? 'Error';
  }
}
