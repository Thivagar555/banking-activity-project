import { Component } from '@angular/core';
import { ApiService } from '../../services/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-withdraw',
  imports: [CommonModule, FormsModule],
  templateUrl: './withdraw.html',
  styleUrls: ['./withdraw.scss']
})
export class Withdraw {
  accountNumber = '';
  amount = 0;

  constructor(private api: ApiService, private router: Router) {}

  withdraw() {
    if (!this.accountNumber || this.amount <= 0) {
      alert("Enter valid details");
      return;
    }

    this.api.post('/account/withdraw', {
      accountNumber: this.accountNumber,
      amount: this.amount
    }).subscribe({
      next: () => {
        alert('Withdrawal Successful');
        this.router.navigate(['/user/account']);
      },
      error: err => {
        alert(err.error || "Withdraw failed");
      }
    });
  }
}
