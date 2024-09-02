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
          await authService.initializeUser();
        } catch (error) {
          console.error(error);
        }
      },
      deps: [AuthService],
      multi: true,
    },
  ],
};
