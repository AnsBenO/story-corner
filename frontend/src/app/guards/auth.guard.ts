import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (_route, _state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (
    authService.currentUser() === null ||
    authService.currentUser() === undefined
  ) {
    return true;
  } else {
    router.navigateByUrl('/');
    return false;
  }
};
