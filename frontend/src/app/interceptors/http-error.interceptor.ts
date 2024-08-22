import {
  HttpErrorResponse,
  type HttpInterceptorFn,
} from '@angular/common/http';
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
      let errorMessage = '';

      if (error.error instanceof ErrorEvent) {
        errorMessage = 'An unexpected error occurred. Please try again later.';
      } else {
        switch (error.status) {
          case 400: {
            const errorDetails = (error.error as ErrorResponse).detail.split(
              '|'
            );
            notificationStore.notify(errorDetails, NotificationType.ERROR);
            break;
          }
          case 401: {
            errorMessage = 'Unauthorized. Please log in to continue.';
            notificationStore.notify(errorMessage, NotificationType.ERROR);
            break;
          }
          case 403: {
            errorMessage =
              'Forbidden. You do not have permission to perform this action.';
            notificationStore.notify(errorMessage, NotificationType.ERROR);
            break;
          }
          case 404: {
            errorMessage = 'Not Found. The requested resource was not found.';
            notificationStore.notify(errorMessage, NotificationType.ERROR);
            break;
          }
          case 500: {
            errorMessage = 'Internal Server Error. Please try again later.';
            notificationStore.notify(errorMessage, NotificationType.ERROR);
            break;
          }
          default: {
            errorMessage =
              'An unexpected error occurred. Please try again later.';
            notificationStore.notify(errorMessage, NotificationType.ERROR);
            break;
          }
        }
      }

      console.error(`Error Code: ${error.status}\nMessage: ${errorMessage}`);

      return throwError(() => new Error(errorMessage || 'An error occurred'));
    })
  );
};
