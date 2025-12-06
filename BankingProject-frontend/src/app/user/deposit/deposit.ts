import { Component } from '@angular/core';
import { ApiService } from '../../services/api';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-deposit',
  templateUrl: './deposit.html'
})
export class Deposit {
  accountNumber = '';
  amount = 0;
  constructor(private api: ApiService, private router: Router) {}
  submit(){
    this.api.post('/account/deposit', {accountNumber: this.accountNumber, amount: this.amount}).subscribe(()=> {
      alert('Deposited'); this.router.navigate(['/user/account']);
    }, e=> alert('Error'));
  }
}
