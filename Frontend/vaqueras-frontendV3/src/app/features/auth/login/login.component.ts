import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="row justify-content-center">
      <div class="col-12 col-md-6 col-lg-4">
        <div class="card shadow-sm">
          <div class="card-body">
            <h3 class="mb-3 text-vaq-primary fw-bold">Iniciar sesión</h3>

            @if (error) {
              <div class="alert alert-danger">{{ error }}</div>
            }

            <label class="form-label">Correo o Nickname</label>
            <input class="form-control mb-2" [(ngModel)]="identifier" name="identifier" />

            <label class="form-label">Contraseña</label>
            <input class="form-control mb-3" type="password" [(ngModel)]="password" name="password" />

            <button class="btn btn-vaq w-100" (click)="submit()" [disabled]="loading">
              {{ loading ? 'Entrando...' : 'Entrar' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  private auth = inject(AuthService);

  identifier = '';
  password = '';
  loading = false;
  error = '';

  submit() {
    this.error = '';
    this.loading = true;

    this.auth.login(this.identifier.trim(), this.password).subscribe({
      next: () => this.loading = false,
      error: (e) => {
        this.loading = false;
        this.error = e?.error?.error || e?.error?.mensaje || 'Error al iniciar sesión';
      }
    });
  }
}
