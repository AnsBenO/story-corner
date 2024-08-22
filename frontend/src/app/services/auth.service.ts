import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { CurrentUser } from '../types/current-user.type';
import { LoginPayload } from '../types/login-payload.type';
import { HttpClient } from '@angular/common/http';
import { Observable, take, tap } from 'rxjs';
import { AuthResponse } from '../types/auth-response.type';
import { environment } from '../../environments/environment';
import { RegisterPayload } from '../types/register-payload';
import { AuthTokenPayload } from '../types/auth-token-payload.type';
import {
  NotificationStore,
  NotificationType,
} from '../store/notification.store';
import { Router } from '@angular/router';

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

  login(formData: LoginPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/auth/login`, formData)
      .pipe(
        tap((response) => {
          this.setAccessToken(response.accessToken);
          this.setRefreshToken(response.refreshToken);
          this.startRefreshTimer();
          this.currentUser.set(response.user);
        })
      );
  }

  register(formData: RegisterPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/auth/register`, formData)
      .pipe(
        tap((response) => {
          this.setAccessToken(response.accessToken);
          this.setRefreshToken(response.refreshToken);
          this.startRefreshTimer();
          this.currentUser.set(response.user);
        })
      );
  }

  logout() {
    this.currentUser.set(null);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('cartItems');
    this.stopRefreshTokenTimer();
    this.notificationStore.notify('You are logged out', NotificationType.INFO);
    this.router.navigate(['/']);
  }

  refreshToken(): void {
    const refreshToken = localStorage.getItem('refreshToken');
    this.http
      .post<AuthResponse>(
        `${environment.API_URL}/auth/refresh-token`,
        refreshToken
      )
      .pipe(
        take(1),
        tap((response) => {
          this.setAccessToken(response.accessToken);
          this.setRefreshToken(response.refreshToken);
          this.startRefreshTimer();
          this.currentUser.set(response.user);
        })
      )
      .subscribe();
  }

  getCurrentUser(): Observable<CurrentUser> {
    return this.http.get<CurrentUser>('/auth/user').pipe(
      tap((response) => {
        this.currentUser.set(response);
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

  private startRefreshTimer() {
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

      // refresh token if it is near expiration
      if (timeout < 60000) {
        this.refreshToken();
      }

      this.refreshTokenTimeout = setTimeout(() => {
        this.refreshToken();
      }, timeout - 60000);
    } catch (error) {
      console.error('Failed to parse JWT:', error);
      this.logout();
    }
  }
}
