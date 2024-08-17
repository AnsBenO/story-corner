import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';
import { CartStore } from '../store/cart.store';

export const checkoutGuard: CanActivateFn = (route, state) => {
  const cartStore = inject(CartStore);
  const router = inject(Router);

  if (cartStore.items().length > 0) {
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};
