import { Injectable, inject } from '@angular/core';
import { ApiService } from './api';
import { Router } from '@angular/router';
import { BehaviorSubject, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private api = inject(ApiService);
  private router = inject(Router);

  isLogged$ = new BehaviorSubject<boolean>(false);
  role$ = new BehaviorSubject<string | null>(null);

  // -------- LOGIN STEP 1 --------
 login(email: string, password: string) {
  return this.api.post('/auth/login', {
    email,
    password
  });
}

  // -------- LOGIN STEP 2 (OTP) --------
  verifyOtp(email: string, otp: string) {
    return this.api.post<{ role: string }>('/auth/verify-otp', { email, otp })
      .pipe(
        tap(res => {
          this.isLogged$.next(true);
          this.role$.next(res.role); // ✅ ADMIN / USER
        })
      );
  }

  // -------- SESSION CHECK (OPTIONAL but recommended) --------
  checkSession() {
    return this.api.get<{ role: string }>('/auth/me')
      .pipe(
        tap(res => {
          this.isLogged$.next(true);
          this.role$.next(res.role);
        })
      );
  }

  // -------- LOGOUT --------
  logout() {
    this.api.post('/auth/logout', {}).subscribe({
      complete: () => {
        this.isLogged$.next(false);
        this.role$.next(null);
        this.router.navigate(['/login']);
      }
    });
  }

   setAuthState(logged: boolean, role: string) {   // ✅ ADD THIS
    this.isLogged$.next(logged);
    this.role$.next(role);
  }
}
