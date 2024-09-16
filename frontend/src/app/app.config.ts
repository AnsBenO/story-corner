import {
  APP_INITIALIZER,
  ApplicationConfig,
  provideExperimentalZonelessChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { httpErrorInterceptor } from './interceptors/http-error.interceptor';
import { authInterceptor } from './interceptors/auth.interceptor';
import { AuthService } from './services/auth.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideExperimentalZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([httpErrorInterceptor, authInterceptor])
    ),
    {
      provide: APP_INITIALIZER,
      useFactory: (authService: AuthService) => async () => {
        try {
          // Try to load the user from http call to the server
          // if some error happens, We catch this error and do nothing, so the app doesn't crash
          await authService.initializeUser();
        } catch (error) {
          // if there is an error, log it
          console.error('Failed to load user:', error);
        }
      },
      deps: [AuthService],
      multi: true,
    },
  ],
};
