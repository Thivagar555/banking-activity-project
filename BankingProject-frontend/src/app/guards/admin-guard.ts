import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { map, take } from 'rxjs/operators';

export const AdminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return auth.role$.pipe(
    take(1),
    map(role => {
      if (role === 'ADMIN') {
        return true;
      }
      router.navigate(['/login']);
      return false;
    })
  );
};
