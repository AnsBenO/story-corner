import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { CartStore } from '../store/cart.store';
import { AuthService } from '../services/auth.service';
import {
  NotificationStore,
  NotificationType,
} from '../store/notification.store';

export const checkoutGuard: CanActivateFn = (_route, _state) => {
  const cartStore = inject(CartStore);
  const authService = inject(AuthService);
  const notificationStore = inject(NotificationStore);
  const router = inject(Router);

  if (cartStore.items().length > 0) {
    if (
      authService.currentUser() !== null &&
      authService.currentUser() !== undefined
    ) {
      return true;
    } else {
      router.navigate(['/login']);
      notificationStore.notify(
        'Please login to proceed',
        NotificationType.INFO
      );
      return false;
    }
  } else {
    router.navigate(['/']);
    return false;
  }
};
