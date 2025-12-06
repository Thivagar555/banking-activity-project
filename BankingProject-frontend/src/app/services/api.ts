import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {

  private BASE_URL = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  get<T>(url: string): Observable<T> {
    return this.http.get<T>(this.BASE_URL + url, {
      withCredentials: true   // ✅ REQUIRED for HttpOnly cookie
    });
  }

  post<T>(url: string, body: any): Observable<T> {
    return this.http.post<T>(this.BASE_URL + url, body, {
      withCredentials: true
    });
  }
}
