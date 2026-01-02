import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GruposService, Grupo, Miembro } from '../../../../core/services/grupos.service';

@Component({
  selector: 'app-gamer-grupos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gamer-grupos.component.html',
})
export class GamerGruposComponent implements OnInit {
  loading = false;
  error: string | null = null;

  grupos: Grupo[] = [];
  grupoSeleccionado: Grupo | null = null;

  miembros: Miembro[] = [];

  // forms
  nombreGrupo = '';
  nicknameInvitado = '';

  constructor(private gruposService: GruposService) {}

  ngOnInit(): void {
    this.cargarGrupos();
  }

  cargarGrupos() {
    this.loading = true;
    this.error = null;

    this.gruposService.listarMisGrupos().subscribe({
      next: (data) => {
        this.grupos = Array.isArray(data) ? data : [];
        this.loading = false;

        if (this.grupos.length) {
          this.seleccionarGrupo(this.grupos[0]);
        }
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.error || 'No se pudieron cargar tus grupos';
      },
    });
  }

  crearGrupo() {
    this.error = null;
    const nombre = this.nombreGrupo.trim();
    if (!nombre) return;

    // OJO: si tu backend espera "nombre" y no "nombreGrupo", cambia aquí.
    this.gruposService.crearGrupo({ nombreGrupo: nombre }).subscribe({
      next: () => {
        this.nombreGrupo = '';
        this.cargarGrupos();
      },
      error: (err) => {
        this.error = err?.error?.error || 'Error creando grupo (revisa el body esperado por backend)';
      },
    });
  }

  seleccionarGrupo(g: Grupo) {
    this.grupoSeleccionado = g;
    this.cargarMiembros();
  }

  cargarMiembros() {
    if (!this.grupoSeleccionado) return;

    this.error = null;
    this.gruposService.listarMiembros(this.grupoSeleccionado.idGrupo).subscribe({
      next: (data) => (this.miembros = Array.isArray(data) ? data : []),
      error: (err) => (this.error = err?.error?.error || 'No se pudieron cargar miembros'),
    });
  }

  invitar() {
    if (!this.grupoSeleccionado) return;
    const nick = this.nicknameInvitado.trim();
    if (!nick) return;

    this.error = null;
    this.gruposService.agregarMiembro(this.grupoSeleccionado.idGrupo, { nickname: nick }).subscribe({
      next: () => {
        this.nicknameInvitado = '';
        this.cargarMiembros();
      },
      error: (err) => (this.error = err?.error?.error || 'No se pudo invitar (nickname inválido o ya tiene grupo)'),
    });
  }
}
