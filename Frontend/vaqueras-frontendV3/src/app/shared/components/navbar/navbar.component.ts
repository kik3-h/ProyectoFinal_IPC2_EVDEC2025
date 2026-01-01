import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {
  private auth = inject(AuthService);

  me = computed(() => this.auth.user());
  isLogged = computed(() => this.auth.isLoggedIn());

  logout() { this.auth.logout().subscribe(); }
}
