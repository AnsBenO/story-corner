import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { checkoutGuard } from './guards/checkout.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    loadComponent: () =>
      import('./components/home/home.component').then((m) => m.HomeComponent),
    data: { title: 'book store - Home' },
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./components/auth/login/login.component').then(
        (m) => m.LoginComponent
      ),
    data: { title: 'login' },
    canActivate: [authGuard],
  },
  {
    path: 'signup',
    loadComponent: () =>
      import('./components/auth/signup/signup.component').then(
        (m) => m.SignupComponent
      ),
    data: { title: 'signup' },
    canActivate: [authGuard],
  },
  {
    path: 'checkout',
    loadComponent: () =>
      import('./components/checkout/checkout.component').then(
        (m) => m.CheckoutComponent
      ),
    canActivate: [checkoutGuard],
  },
  {
    path: '**',
    loadComponent: () =>
      import('./components/common/not-found/not-found.component').then(
        (m) => m.NotFoundComponent
      ),
  },
];
