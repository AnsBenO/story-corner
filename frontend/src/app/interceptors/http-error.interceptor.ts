import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
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
      let errorMessage = 'An unexpected error occurred.';
      const errorResponse = error.error as ErrorResponse;

      // Handle session expiration
      if (error.url?.includes('/refresh-token')) {
        notificationStore.notify(
          'Your session has expired',
          NotificationType.ERROR
        );
        authService.currentUser.set(null);
        localStorage.removeItem('accessToken');
      }

      const shouldAttemptRefresh =
        error.status === 401 &&
        !['/login', '/logout', '/refresh-token'].some((url) =>
          error.url?.includes(url)
        );

      const isForbiddenUserEndpoint =
        error.status === 403 && error.url?.includes('/user');

      // Retry with refreshed token if applicable
      if (shouldAttemptRefresh || isForbiddenUserEndpoint) {
        return authService.refreshToken().pipe(
          switchMap((_response) => {
            return next(req);
          })
        );
      }

      // Handle client-side errors
      if (error.error instanceof ErrorEvent) {
        console.error('Client-side error:', error.error.message);
        errorMessage = error.error.message;
        notificationStore.notify(errorMessage, NotificationType.ERROR);
      } else {
        // Handle server-side errors
        const errorMessageArray = Object.values(errorResponse.detail || {});

        notificationStore.notify(errorMessageArray, NotificationType.ERROR);
      }
      // Return the error to be handled by the component or other interceptors
      return throwError(() => error);
    })
  );
};
