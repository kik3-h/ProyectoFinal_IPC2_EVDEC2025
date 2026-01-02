import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { RegistroService } from './registro.service';
import { RegistroUsuarioRequest, RolRegistro } from './registro.model';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './registro.component.html',
})
export class RegistroComponent {
  loading = false;

  form: RegistroUsuarioRequest = {
    nickname: '',
    email: '',
    password: '',
    telefono: '',
    fechaNacimiento: '',
    pais: 'GT',
    rol: 'GAMER',
  };

  roles: RolRegistro[] = ['GAMER', 'EMPRESA', 'ADMIN'];

  alert: { type: 'success' | 'danger'; msg: string } | null = null;

  constructor(private api: RegistroService, private router: Router) {}

  registrar() {
    this.alert = null;

    // validaciones mínimas
    if (!this.form.nickname.trim()) return this.fail('Nickname requerido');
    if (!this.form.email.trim()) return this.fail('Email requerido');
    if (!this.form.password.trim()) return this.fail('Password requerido');
    if (!this.form.telefono.trim()) return this.fail('Teléfono requerido');
    if (!this.form.fechaNacimiento) return this.fail('Fecha de nacimiento requerida');
    if (!this.form.pais.trim()) return this.fail('País requerido');

    this.loading = true;

    const body: RegistroUsuarioRequest = {
      ...this.form,
      nickname: this.form.nickname.trim(),
      email: this.form.email.trim(),
      telefono: this.form.telefono.trim(),
      pais: this.form.pais.trim().toUpperCase(),
    };

    this.api.crearCuenta(body).subscribe({
      next: () => {
        this.loading = false;
        this.alert = { type: 'success', msg: 'Cuenta creada correctamente. Ahora inicia sesión.' };
        // redirigir a login
        setTimeout(() => this.router.navigateByUrl('/login'), 800);
      },
      error: (e) => {
        this.loading = false;
        const msg =
          e?.error?.error ??
          e?.error?.message ??
          e?.message ??
          'Error creando cuenta';
        // tu backend devuelve 409 con {"error":"Usuario ya existe"}
        this.alert = { type: 'danger', msg };
      },
    });
  }

  private fail(msg: string) {
    this.alert = { type: 'danger', msg };
  }
}
