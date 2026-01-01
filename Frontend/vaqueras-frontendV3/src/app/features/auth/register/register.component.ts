import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuariosService } from '../../../core/services/usuarios.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="row justify-content-center">
      <div class="col-12 col-md-6 col-lg-5">
        <div class="card shadow-sm">
          <div class="card-body">
            <h3 class="mb-3 text-vaq-primary fw-bold">Registro</h3>

            @if (msg) { <div class="alert alert-success">{{msg}}</div> }
            @if (error) { <div class="alert alert-danger">{{error}}</div> }

            <label class="form-label">Correo</label>
            <input class="form-control mb-2" [(ngModel)]="correo" name="correo">

            <label class="form-label">Nickname</label>
            <input class="form-control mb-2" [(ngModel)]="nickname" name="nickname">

            <label class="form-label">Contraseña</label>
            <input class="form-control mb-3" type="password" [(ngModel)]="password" name="password">

            <button class="btn btn-vaq w-100" (click)="submit()" [disabled]="loading">
              {{loading ? 'Registrando...' : 'Crear cuenta'}}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RegisterComponent {
  private users = inject(UsuariosService);
  private router = inject(Router);

  correo = '';
  nickname = '';
  password = '';

  loading = false;
  msg = '';
  error = '';

  submit() {
    this.msg = ''; this.error = '';
    this.loading = true;

    this.users.register({ correo: this.correo.trim(), nickname: this.nickname.trim(), password: this.password })
      .subscribe({
        next: () => {
          this.loading = false;
          this.msg = 'Registro exitoso. Ahora inicia sesión.';
          setTimeout(() => this.router.navigateByUrl('/login'), 600);
        },
        error: (e) => {
          this.loading = false;
          this.error = e?.error?.error || e?.error?.mensaje || 'Error al registrar';
        }
      });
  }
}
