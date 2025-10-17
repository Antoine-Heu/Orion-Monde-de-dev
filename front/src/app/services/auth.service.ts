import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of } from 'rxjs';
import { LoginRequest, RegisterRequest, TokenResponse } from '../interfaces/auth.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = environment.production ? 'http://localhost:8080/api/auth' : 'http://localhost:8080/api/auth';

  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  public isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(private http: HttpClient) {
  }

  login(loginRequest: LoginRequest): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/login`, loginRequest, {
      withCredentials: true
    }).pipe(
        tap(() => {
          this.isLoggedInSubject.next(true);
        })
      );
  }

  register(registerRequest: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/register`, registerRequest, {
      withCredentials: true
    }).pipe(
        tap(() => {
          this.isLoggedInSubject.next(true);
        })
      );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => {
        this.isLoggedInSubject.next(false);
      })
    );
  }

  checkAuth(): Observable<boolean> {
    return this.http.get<any>(`${this.API_URL}/me`, {
      withCredentials: true
    }).pipe(
      tap(() => this.isLoggedInSubject.next(true)),
      catchError(() => {
        this.isLoggedInSubject.next(false);
        return of(false);
      })
    );
  }

  initializeAuth(): Promise<boolean> {
    return new Promise((resolve) => {
      this.checkAuth().subscribe({
        next: (isAuthenticated) => resolve(isAuthenticated),
        error: () => resolve(false)
      });
    });
  }

  isLoggedIn(): boolean {
    return this.isLoggedInSubject.value;
  }
}