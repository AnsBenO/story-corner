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

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationStore = inject(NotificationStore);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = '';

      if (error.error instanceof ErrorEvent) {
        errorMessage = 'An unexpected error occurred. Please try again later.';
      } else {
        switch (error.status) {
          case 400:
            errorMessage =
              'Bad Request. Please check the data you have entered.';
            break;
          case 401:
            errorMessage = 'Unauthorized. Please log in to continue.';
            break;
          case 403:
            errorMessage =
              'Forbidden. You do not have permission to perform this action.';
            break;
          case 404:
            errorMessage = 'Not Found. The requested resource was not found.';
            break;
          case 500:
            errorMessage = 'Internal Server Error. Please try again later.';
            break;
          default:
            errorMessage =
              'An unexpected error occurred. Please try again later.';
            break;
        }
      }

      notificationStore.notify(errorMessage, NotificationType.ERROR);

      console.error(`Error Code: ${error.status}\nMessage: ${errorMessage}`);

      return throwError(() => new Error(errorMessage));
    })
  );
};
