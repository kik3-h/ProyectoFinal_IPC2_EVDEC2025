import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  template: `
  <app-navbar></app-navbar> 
  
  <div class="container py-3">
    <div class="vaq-surface">
      <router-outlet></router-outlet>
    </div>
  </div>
  `,
})
export class AppComponent {}