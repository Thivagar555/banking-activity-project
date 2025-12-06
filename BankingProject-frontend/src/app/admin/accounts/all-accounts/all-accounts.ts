import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../../services/api';

@Component({
  standalone: true,
  selector: 'app-all-accounts',
  imports: [CommonModule],
  templateUrl: './all-accounts.html',
  styleUrls: ['./all-accounts.scss']
})
export class AllAccounts {
  accounts: any[] = [];

  constructor(private api: ApiService) {
    this.loadAccounts();
  }

  loadAccounts() {
    this.api.get('/admin/accounts').subscribe({
      next: (res: any) => {
        this.accounts = res;
      },
      error: (err) => {
        console.error(err);
        alert("Failed to load accounts");
      }
    });
  }
}
