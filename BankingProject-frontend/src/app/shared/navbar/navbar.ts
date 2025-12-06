import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  template: `
    <nav class="nav">
      <a routerLink="/">Bank</a>
      <div class="nav-right">
        <a routerLink="/user/account" *ngIf="auth.isLogged$ | async">Account</a>
        <a routerLink="/user/transactions" *ngIf="auth.isLogged$ | async">Transactions</a>
        <a routerLink="/admin/accounts" *ngIf="(auth.role$|async) === 'ADMIN'">Admin</a>
        <button *ngIf="auth.isLogged$ | async" (click)="logout()">Logout</button>
      </div>
    </nav>
  `,
  styles: [`
    .nav { display:flex; justify-content:space-between; padding:12px 0; }
    .nav-right a, .nav-right button { margin-left:12px; }
  `]
})
export class Navbar {
  auth = inject(AuthService);
  logout(){ this.auth.logout(); }
}
