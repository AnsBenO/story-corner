import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError, of } from 'rxjs';
import {
  NotificationStore,
  NotificationType,
} from '../store/notification.store';

import { ErrorResponse } from '../types/error-response.type';
import { AuthService } from '../services/auth.service';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationStore = inject(NotificationStore);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Handle network errors
      if (error.status === 0 || error.status === null) {
        notificationStore.notify(
          'Cannot reach the server. Please check your internet connection or try again later.',
          NotificationType.ERROR
        );
        return throwError(() => error);
      }

      // Handle session expiration
      if (error.url?.includes('/refresh-token')) {
        notificationStore.notify(
          'Your session has expired',
          NotificationType.ERROR
        );
        authService.currentUser.set(null);
        localStorage.removeItem('accessToken');
        return of();
      }

      // Retry with refreshed token if applicable
      if (shouldAttemptTokenRefresh(error)) {
        return authService.refreshToken().pipe(
          switchMap(() => next(req)),
          catchError((refreshError) => {
            console.error('Token refresh failed:', refreshError.message);
            return handleOtherErrors(refreshError);
          })
        );
      }

      function handleOtherErrors(error: HttpErrorResponse) {
        if (error.error instanceof ErrorEvent) {
          console.error('Client-side error:', error.error.message);
          notificationStore.notify(
            'An unexpected error occurred.',
            NotificationType.ERROR
          );
        } else {
          console.error('Server-side error:', error);
          const errorResponse = error.error as ErrorResponse;
          const errorMessageArray = Object.values(errorResponse.detail || {});
          notificationStore.notify(errorMessageArray, NotificationType.ERROR);
        }
        return throwError(() => error);
      }

      // Handle client-side and server-side errors
      return handleOtherErrors(error);
    })
  );
};

function shouldAttemptTokenRefresh(error: HttpErrorResponse) {
  const isUnauthorized = error.status === 401;
  const isForbiddenUserEndpoint =
    error.status === 403 && error.url?.includes('/user');
  const isNotAuthEndpoint = !['/login', '/logout', '/refresh-token'].some(
    (url) => error.url?.includes(url)
  );

  return (isUnauthorized && isNotAuthEndpoint) || isForbiddenUserEndpoint;
}
