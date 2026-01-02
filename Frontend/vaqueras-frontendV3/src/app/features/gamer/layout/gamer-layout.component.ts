import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-gamer-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './gamer-layout.component.html',
})
export class GamerLayoutComponent {}

