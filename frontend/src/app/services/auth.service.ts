import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { CurrentUser } from '../types/current-user.type';
import { LoginPayload } from '../types/login-payload.type';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, take, tap, throwError } from 'rxjs';
import { AuthResponse } from '../types/auth-response.type';
import { environment } from '../../environments/environment';
import { RegisterPayload } from '../types/register-payload';
import { AuthTokenPayload } from '../types/auth-token-payload.type';
import { NotificationStore } from '../store/notification.store';
import { Router } from '@angular/router';
import { LogoutResponse } from '../types/logout-response.type';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  currentUser: WritableSignal<CurrentUser | null | undefined> =
    signal(undefined);

  http = inject(HttpClient);

  router = inject(Router);
  notificationStore = inject(NotificationStore);

  private refreshTokenTimeout!: ReturnType<typeof setTimeout>;

  constructor() {}
  login(formData: LoginPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/auth/login`, formData, {
        withCredentials: true,
      })
      .pipe(
        tap((response) => {
          this.setAccessToken(response.accessToken);
          this.startRefreshTimer();
          this.currentUser.set(response.user);
        })
      );
  }

  register(formData: RegisterPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/auth/register`, formData, {
        withCredentials: true,
      })
      .pipe(
        tap((response) => {
          this.setAccessToken(response.accessToken);
          this.startRefreshTimer();
          this.currentUser.set(response.user);
        })
      );
  }

  logout(): Observable<LogoutResponse> {
    return this.http
      .post<LogoutResponse>(
        `${environment.API_URL}/auth/logout`,
        {},
        { withCredentials: true }
      )
      .pipe(
        tap((_response) => {
          this.currentUser.set(null);
          localStorage.removeItem('accessToken');
          localStorage.removeItem('cartItems');
          this.stopRefreshTokenTimer();
        }),
        catchError((_err) => {
          localStorage.removeItem('accessToken');
          localStorage.removeItem('cartItems');
          return of();
        })
      );
  }

  refreshToken(): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(
        `${environment.API_URL}/auth/refresh-token`,
        {},
        {
          withCredentials: true,
        }
      )
      .pipe(
        tap((response) => {
          this.setAccessToken(response.accessToken);
          this.startRefreshTimer();
          this.currentUser.set(response.user);
        })
      );
  }
  public startRefreshTimer() {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) return;
    try {
      const tokenPayload = JSON.parse(
        atob(accessToken.split('.')[1])
      ) as AuthTokenPayload;
      // Calculate the expiration date of the JWT token.
      // The `exp` property is in seconds, so we multiply by 1000 to convert it to milliseconds.
      const expires = new Date(tokenPayload.exp * 1000);

      const timeout = expires.getTime() - Date.now();

      console.log('refresh timer started, timeout: ', timeout);

      // refresh token if it is near expiration
      if (timeout < 60000) {
        this.refreshToken().pipe(take(1)).subscribe();
      }

      this.refreshTokenTimeout = setTimeout(() => {
        this.refreshToken().pipe(take(1)).subscribe();
      }, timeout - 60000);
    } catch (error) {
      console.error('Failed to parse JWT:', error);
    }
  }

  getCurrentUser(): Observable<CurrentUser> {
    return this.http.get<CurrentUser>(`${environment.API_URL}/auth/user`).pipe(
      tap((response) => {
        this.currentUser.set(response);
        this.refreshToken()
          .pipe(take(1))
          .subscribe(() => {
            this.startRefreshTimer();
          });
      })
    );
  }

  setAccessToken(token: string): void {
    localStorage.setItem('accessToken', token);
  }
  setRefreshToken(token: string) {
    localStorage.setItem('refreshToken', token);
  }

  private stopRefreshTokenTimer() {
    clearTimeout(this.refreshTokenTimeout);
  }
}
