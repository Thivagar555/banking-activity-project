import { Component } from '@angular/core';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../../services/api';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule],
  selector: 'app-register',
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class Register {
  form = new FormGroup({  
    name: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
    role: new FormControl('USER') // default
  });

  constructor(private api: ApiService, private router: Router) {}

  submit() {
    const payload = { ...this.form.value };
    // Use different endpoint for admin or user
    const end = (payload.role === 'ADMIN') ? '/auth/register-admin' : '/auth/register';
    this.api.post(end, payload).subscribe({
      next: () => { alert('Registered'); this.router.navigate(['/login']); },
      error: e => alert('Failed: ' + JSON.stringify(e))
    });
  }
}
