import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GamerCarteraService } from '../../../../core/services/gamer-cartera.service';
import { CarteraResumen, RecargaItem } from '../../../../core/models/gamer.models';

@Component({
  selector: 'app-gamer-cartera',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gamer-cartera.component.html',
})
export class GamerCarteraComponent implements OnInit {
  loading = true;
  resumen: CarteraResumen | null = null;
  recargas: RecargaItem[] = [];
  monto = 0;

  alert: { type: 'success' | 'danger'; msg: string } | null = null;

  constructor(private api: GamerCarteraService) {}

  ngOnInit(): void {
    this.refrescar();
  }

  refrescar() {
    this.loading = true;
    this.alert = null;
    Promise.all([
      this.api.resumen().toPromise(),
      this.api.recargas().toPromise(),
    ]).then(([r, list]) => {
      this.resumen = r ?? null;
      this.recargas = list ?? [];
      this.loading = false;
    }).catch((e) => {
      this.loading = false;
      this.alert = { type:'danger', msg: this.errMsg(e) };
    });
  }

  recargar() {
    if (this.monto <= 0) {
      this.alert = { type:'danger', msg:'Monto inválido.' };
      return;
    }
    this.api.recargar(this.monto).subscribe({
      next: () => {
        this.alert = { type:'success', msg:'Recarga realizada.' };
        this.monto = 0;
        this.refrescar();
      },
      error: (e) => this.alert = { type:'danger', msg: this.errMsg(e) }
    });
  }

  get saldo(): number {
    const s = this.resumen?.saldo ?? this.resumen?.saldoActual ?? 0;
    return Number(s) || 0;
  }

  private errMsg(e: any): string {
    return e?.error?.error ?? e?.error?.mensaje ?? e?.message ?? 'Error';
  }
}
