import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../../services/api';

@Component({
  standalone: true,
  imports: [CommonModule],
  templateUrl: './locked-users.html',
  styleUrls: ['./locked-users.scss']
})
export class LockedUsers {

  users: any[] = [];

  constructor(private api: ApiService) {
    this.load();
  }

  load() {
    this.api.get<any[]>('/admin/locked-users')
      .subscribe(users => {
        this.users = users;
      });
  }

  unlock(id: number) {
    this.api.post(`/admin/unlock/${id}`, {})
      .subscribe(() => {
        alert('User unlocked');
        this.load();
      });
  }
}
