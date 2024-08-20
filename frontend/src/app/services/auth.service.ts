import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { CurrentUser } from '../types/current-user.type';
import { ValidationErrorResponse } from '../types/validation-error-response.type';
import { LoginPayload } from '../types/login-payload.type';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponse } from '../types/auth-response.type';
import { environment } from '../../environments/environment';
import { RegisterPayload } from '../types/register-payload';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  currentUser: WritableSignal<CurrentUser | null | undefined> =
    signal(undefined);

  validationError: WritableSignal<ValidationErrorResponse | null> =
    signal(null);

  http = inject(HttpClient);

  login(formData: LoginPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/auth/login`, formData)
      .pipe(
        tap((response) => {
          this.setAuthToken(response.jwt);
          this.currentUser.set(response.user);
        })
      );
  }

  register(formData: RegisterPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/auth/register`, formData)
      .pipe(
        tap((response) => {
          this.setAuthToken(response.jwt);
          this.currentUser.set(response.user);
        })
      );
  }

  refreshToken(): Observable<AuthResponse> {
    return this.http
      .get<AuthResponse>(`${environment.API_URL}/auth/refresh-token`)
      .pipe(
        tap((response) => {
          this.setAuthToken(response.jwt);
          this.currentUser.set(response.user);
        })
      );
  }

  getCurrentUser(): Observable<CurrentUser> {
    return this.http.get<CurrentUser>('/auth/user').pipe(
      tap((response) => {
        this.currentUser.set(response);
      })
    );
  }
  setAuthToken(token: string): void {
    localStorage.setItem('authToken', token);
  }
}
