import { Component } from '@angular/core';
import { ApiService } from '../../services/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-transfer',
  imports: [CommonModule, FormsModule],
  templateUrl: './transfer.html',
  styleUrls: ['./transfer.scss']
})
export class Transfer {
  senderAccount = '';
  receiverAccount = '';
  amount = 0;
  note = '';

  otpSent = false;     // switch UI to OTP mode
  transactionId = '';  // backend returns transaction tracking ID

  constructor(private api: ApiService, private router: Router) {}

  startTransfer() {
    if (!this.senderAccount || !this.receiverAccount || this.amount <= 0) {
      alert("Fill all fields properly");
      return;
    }

    this.api.post('/transaction/transfer', {
      senderAccount: this.senderAccount,
      receiverAccount: this.receiverAccount,
      amount: this.amount,
      note: this.note
    }).subscribe({
      next: (res: any) => {
        this.otpSent = true;
        this.transactionId = res.transactionId; // must match backend response
        alert("OTP sent for confirmation");
      },
      error: err => alert(err.error || "Transfer failed")
    });
  }

  confirmTransfer(otp: string) {
    if (!otp) {
      alert("Enter OTP");
      return;
    }

    this.api.post('/transaction/confirm-transfer', {
      transactionId: this.transactionId,
      otp
    }).subscribe({
      next: () => {
        alert("Transfer successful!");
        this.router.navigate(['/user/transactions']);
      },
      error: err => alert(err.error || "OTP verification failed")
    });
  }
}
