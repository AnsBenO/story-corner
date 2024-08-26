import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import {
  NotificationStore,
  NotificationType,
} from '../store/notification.store';

import { ErrorResponse } from '../types/error-response.type';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationStore = inject(NotificationStore);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'An unexpected error occurred.';
      const errorResponse = error.error as ErrorResponse;

      if (error.error instanceof ErrorEvent) {
        // Client-side error (e.g., network issue)
        console.error('Client-side error:', error.error.message);
        errorMessage = error.error.message;
        notificationStore.notify(errorMessage, NotificationType.ERROR);
      } else {
        const errorMessageArray = Object.values(
          (error.error as ErrorResponse).detail
        );
        // Server-side error
        switch (error.status) {
          case 400: {
            // Handle validation errors and other bad request scenarios
            const errorsArray = Object.values(errorResponse.detail);
            if (errorsArray.length > 0) {
              notificationStore.notify(errorsArray, NotificationType.ERROR);
            } else {
              notificationStore.notify(errorMessage, NotificationType.ERROR);
            }
            break;
          }
          case 401: {
            break;
          }

          case 404: {
            // Handle resource not found

            notificationStore.notify(errorMessageArray, NotificationType.ERROR);
            break;
          }
          case 500: {
            notificationStore.notify(errorMessageArray, NotificationType.ERROR);
            break;
          }
          default: {
            // Handle other unexpected server errors

            notificationStore.notify(errorMessageArray, NotificationType.ERROR);
            console.error('Unexpected error status:', error.status);
          }
        }
      }

      // Return the error to be handled by the component or other interceptors
      return throwError(() => error);
    })
  );
};
