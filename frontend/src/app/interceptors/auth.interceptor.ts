import type {
  HttpErrorResponse,
  HttpInterceptorFn,
} from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import {
  NotificationStore,
  NotificationType,
} from '../store/notification.store';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const notificationStore = inject(NotificationStore);

  // Add Authorization header if the request is not for login, register, or refresh token
  if (
    !['/login', '/register', '/refresh-token'].some((url) =>
      req.url?.includes(url)
    )
  ) {
    const accessToken = localStorage.getItem('accessToken') ?? '';
    req = req.clone({
      setHeaders: {
        Authorization: accessToken ? `Bearer ${accessToken}` : '',
      },
    });
  }

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.url?.includes('/refresh-token')) {
        notificationStore.notify(
          'Your session has expired',
          NotificationType.ERROR
        );
        authService.currentUser.set(null);
        localStorage.removeItem('accessToken');
      }
      const shouldAttemptRefresh =
        err.status === 401 &&
        !['/login', '/logout', '/refresh-token'].some((url) =>
          err.url?.includes(url)
        );

      const isForbiddenUserEndpoint =
        err.status === 403 && err.url?.includes('/user');

      if (
        shouldAttemptRefresh ||
        isForbiddenUserEndpoint ||
        err.url?.includes('/logout')
      ) {
        // Attempt to refresh the token
        return authService.refreshToken().pipe(
          switchMap((response) => {
            const newAccessToken = response.accessToken;

            const authReq = req.clone({
              setHeaders: {
                Authorization: `Bearer ${newAccessToken}`,
              },
            });

            // Retry the original request with the new token
            return next(authReq);
          }),
          catchError((refreshError) => {
            // Handle refresh token failure
            notificationStore.notify(
              'Your session has expired',
              NotificationType.ERROR
            );
            localStorage.removeItem('accessToken');
            authService.currentUser.set(null);
            return throwError(() => refreshError);
          })
        );
      }

      // If the error is not handled, rethrow it
      return throwError(() => err);
    })
  );
};
