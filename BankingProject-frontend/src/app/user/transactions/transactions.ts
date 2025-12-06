import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api';

@Component({
  standalone: true,
  imports:[CommonModule],
  selector:'app-transactions',
  templateUrl:'./transactions.html'
})
export class Transactions {
  transactions:any[] = [];
  constructor(private api: ApiService) { this.load(); }
  load(){ this.api.get<any[]>('/transaction/history').subscribe(t=>this.transactions=t); }
}
