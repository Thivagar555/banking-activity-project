// src/app/app.routes.ts

import { Routes } from '@angular/router';

// Auth
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';

// User
import { Dashboard } from './user/dashboard/dashboard';
import { Account } from './user/account/account';
import { Transactions } from './user/transactions/transactions';
import { Transfer } from './user/transfer/transfer';
import { Withdraw } from './user/withdraw/withdraw';
import { Deposit } from './user/deposit/deposit';

// Admin
import { AdminDashboard } from './admin/dashboard/dashboard';
import { AllAccounts } from './admin/accounts/all-accounts/all-accounts';
import { LockedUsers } from './admin/users/locked-users/locked-users';

// Guards
import { AuthGuard } from './guards/auth-guard';
import { AdminGuard } from './guards/admin-guard';

export const routes: Routes = [
  { path: '', component: Login },
  { path: 'login', component: Login },
  { path: 'register', component: Register },

  {
    path: 'user',
    component: Dashboard,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'account', pathMatch: 'full' },
      { path: 'account', component: Account },
      { path: 'transactions', component: Transactions },
      { path: 'transfer', component: Transfer },
      { path: 'withdraw', component: Withdraw },
      { path: 'deposit', component: Deposit }
    ]
  },

  {
    path: 'admin',
    component: AdminDashboard,
    canActivate: [AdminGuard],
    children: [
      { path: 'accounts', component: AllAccounts },
      { path: 'locked', component: LockedUsers }
    ]
  },

  { path: '**', redirectTo: '' }
];
