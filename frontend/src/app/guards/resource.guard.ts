import type { CanActivateFn } from '@angular/router';

export const resourceGuard: CanActivateFn = (route, state) => {
  return true;
};
