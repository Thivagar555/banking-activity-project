import { Routes } from '@angular/router';
import { AdminLayout } from './layout/admin-layout';
import { AdminDashboard } from './dashboard/dashboard';
import { AllAccounts } from './accounts/all-accounts/all-accounts';
import { LockedUsers } from './users/locked-users/locked-users';
import { AdminGuard } from '../guards/admin-guard';

export const adminRoutes: Routes = [
  {
    path: 'admin',
    component: AdminLayout,
    canActivate: [AdminGuard],
    children: [
      { path: 'dashboard', component: AdminDashboard },
      { path: 'accounts', component: AllAccounts },
      { path: 'locked-users', component: LockedUsers }
    ]
  }
];
