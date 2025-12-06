import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule // ✅ REQUIRED for routerLink
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {

  otpMode = false;

  form = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
    asAdmin: new FormControl(false)
  });

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  submit() {
    const { email, password } = this.form.value;

    this.auth.login(email!, password!).subscribe({
      next: () => {
        alert('OTP sent to email');
        this.otpMode = true;   // ✅ THIS WAS NOT WORKING BEFORE
      },
      error: err => alert(err.error || 'Login failed')
    });
  }

  verifyOtp(otp: string) {
    const email = this.form.value.email!;

    this.auth.verifyOtp(email, otp).subscribe({
      next: (res: any) => {
        this.auth.setAuthState(true, res.role);

        // ✅ ROLE BASED NAVIGATION
        if (res.role === 'ADMIN') {
          this.router.navigate(['/admin/accounts']);
        } else {
          this.router.navigate(['/user/account']);
        }
      },
      error: () => alert('Invalid OTP')
    });
  }
}
