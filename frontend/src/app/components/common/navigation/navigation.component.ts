import { CommonModule } from '@angular/common';
import {
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
import { InboxComponent } from '../../inbox/inbox.component';
import { InboxService } from '../../../services/inbox.service';
import { InboxStore } from '../../../store/inbox.store';

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    CartComponent,
    InboxComponent,
    FontAwesomeModule,
  ],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.scss',
})
export class NavigationComponent {
  cartStore = inject(CartStore);
  inboxService = inject(InboxService);
  authService = inject(AuthService);
  destroy = inject(DestroyRef);
  router = inject(Router);
  notificationStore = inject(NotificationStore);
  inboxStore = inject(InboxStore);
  showCart: WritableSignal<boolean> = signal(false);
  showInbox: WritableSignal<boolean> = signal(false);
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
  openInbox() {
    this.showInbox.set(true);
  }

  closeInbox() {
    this.showInbox.set(false);
  }

  logout() {
    this.authService
      .logout()
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe((response) => {
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
