import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { CurrentUser } from '../types/current-user.type';
import { LoginPayload } from '../types/login-payload.type';
import { HttpClient } from '@angular/common/http';
import {
  catchError,
  filter,
  firstValueFrom,
  Observable,
  of,
  take,
  tap,
  throwError,
} from 'rxjs';
import { AuthResponse } from '../types/auth-response.type';
import { environment } from '../../environments/environment';
import { RegisterPayload } from '../types/register-payload';
import { AuthTokenPayload } from '../types/auth-token-payload.type';
import { NotificationStore } from '../store/notification.store';
import { Router } from '@angular/router';
import { LogoutResponse } from '../types/logout-response.type';
import { InboxService } from './inbox.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { InboxStore } from '../store/inbox.store';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient);

  inboxService = inject(InboxService);

  inboxStore = inject(InboxStore);

  router = inject(Router);
  notificationStore = inject(NotificationStore);

  currentUser: WritableSignal<CurrentUser | null | undefined> =
    signal(undefined);
  user$ = toObservable(this.currentUser);
  private refreshTokenTimeout!: ReturnType<typeof setTimeout>;

  initializeUser(): Promise<unknown> {
    if (localStorage.getItem('accessToken')) {
      return firstValueFrom(this.getCurrentUser());
    }
    return Promise.resolve();
  }

  constructor() {
    this.user$
      .pipe(
        filter(
          (user): user is CurrentUser => user !== null && user !== undefined
        ),
        take(1)
      )
      .subscribe((user) => {
        this.inboxService.initSocketConnection(user.username);
        // these two return subscriptions
        this.inboxStore.getNewNotifications();
        this.inboxStore.getNotifications({});
      });
  }
  login(formData: LoginPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.API_URL}/auth/login`, formData)
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
      .post<AuthResponse>(`${environment.API_URL}/auth/register`, formData)
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
      .post<LogoutResponse>(`${environment.API_URL}/auth/logout`, {})
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
      .post<AuthResponse>(`${environment.API_URL}/auth/refresh-token`, {})
      .pipe(
        tap((response) => {
          this.setAccessToken(response.accessToken);
          this.currentUser.set(response.user);
          this.startRefreshTimer();
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

      console.log('refresh timer started, timeout: ', timeout - 60000);

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
        this.startRefreshTimer();
      }),
      catchError((error) => {
        localStorage.removeItem('accessToken');
        console.error('Failed to load user:', error);
        this.currentUser.set(undefined);
        return throwError(() => error);
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
