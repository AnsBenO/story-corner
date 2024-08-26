import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  HostListener,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CartStore } from '../../../store/cart.store';
import { CartComponent } from '../../cart/cart.component';
import {
  faBars,
  faBell,
  faCartShopping,
  faXmark,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthService } from '../../../services/auth.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NotificationStore,
  NotificationType,
} from '../../../store/notification.store';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [CommonModule, RouterModule, CartComponent, FontAwesomeModule],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.scss',

  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NavigationComponent {
  cartStore = inject(CartStore);
  authService = inject(AuthService);
  destroy = inject(DestroyRef);
  router = inject(Router);
  notificationStore = inject(NotificationStore);
  showCart: WritableSignal<boolean> = signal(false);
  isMenuOpen: WritableSignal<boolean> = signal(false);
  menuIcon = faBars;
  xMarkIcon = faXmark;
  cartIcon = faCartShopping;
  bellIcon = faBell;

  toggleMenu() {
    this.isMenuOpen.set(!this.isMenuOpen());
  }

  toggleMenuOutsideClick($event: MouseEvent) {
    const menuDiv = document.querySelector('#menuDiv');
    const targetElement = $event.target as HTMLElement;
    if (!menuDiv?.contains(targetElement)) {
      this.toggleMenu();
    }
  }

  openCart() {
    this.showCart.set(true);
  }

  closeCart() {
    this.showCart.set(false);
  }

  logout() {
    this.authService
      .logout()
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((response) => {
        this.notificationStore.notify(
          'You are logged out',
          NotificationType.INFO
        );
        this.router.navigate(['/']);
        this.notificationStore.notify(
          response.message,
          NotificationType.SUCCESS
        );
      });
  }

  @HostListener('document:scroll')
  handleScrolling() {
    this.isMenuOpen.set(false);
  }
}
