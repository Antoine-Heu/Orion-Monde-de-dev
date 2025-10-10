import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserDetail, UserUpdate } from '../interfaces/user.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = environment.production
    ? 'http://localhost:8080/api/user'
    : 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/me`);
  }

  getCurrentUserDetails(): Observable<UserDetail> {
    return this.http.get<UserDetail>(`${this.API_URL}/me/details`);
  }

  updateCurrentUser(userData: UserUpdate): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/me`, userData);
  }
}
