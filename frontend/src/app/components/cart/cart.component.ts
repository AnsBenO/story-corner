import { CommonModule } from '@angular/common';
import {
  Component,
  DestroyRef,
  EventEmitter,
  HostListener,
  Output,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faMinus, faPlus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { CartStore } from '../../store/cart.store';
import { Router } from '@angular/router';
import {
  NotificationStore,
  NotificationType,
} from '../../store/notification.store';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, ReactiveFormsModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent {
  @Output() onClose = new EventEmitter<void>();
  cartStore = inject(CartStore);
  router = inject(Router);
  destroy = inject(DestroyRef);
  notificationStore = inject(NotificationStore);
  plusIcon = faPlus;
  minusIcon = faMinus;
  closingCart: WritableSignal<boolean> = signal(false);
  xMarkIcon = faXmark;

  toggleCartOutsideClick($event: MouseEvent) {
    const CartDiv = document.querySelector('#cartDiv');
    const targetElement = $event.target as HTMLElement;
    if (!CartDiv?.contains(targetElement)) {
      this.closeCart();
    }
  }
  closeCart() {
    this.closingCart.set(true);
    setTimeout(() => {
      this.closingCart.set(false);
      this.onClose.emit();
    }, 500);
  }

  increaseQuantity(code: string) {
    this.cartStore.increaseQuantity(code);
  }

  decreaseQuantity(code: string) {
    this.cartStore.decreaseQuantity(code);
  }

  removeItem(code: string) {
    this.cartStore.removeItem(code);
  }

  removeAllItems() {
    this.cartStore.removeAll();
  }

  checkout() {
    if (this.cartStore.items().length > 0) {
      this.closeCart();
      this.cartStore.checkout();
    } else {
      this.notificationStore.notify(
        'Your cart is empty',
        NotificationType.INFO
      );
    }
  }
}
