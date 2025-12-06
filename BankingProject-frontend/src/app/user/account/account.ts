import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-account',
  templateUrl: './account.html',
  styleUrls: ['./account.scss']
})
export class Account {
  account: any = null;
  constructor(private api: ApiService){}
  ngOnInit(){ this.api.get('/account/my-account').subscribe(a => this.account = a, e=> console.error(e)); }
}
