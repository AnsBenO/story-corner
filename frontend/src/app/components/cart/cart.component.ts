import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
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
import { NewOrder } from '../../types/new-order.type';
import { OrderService } from '../../services/order.service';
import {
  NotificationStore,
  NotificationType,
} from '../../store/notification.store';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, ReactiveFormsModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CartComponent {
  @Output() onClose = new EventEmitter<void>();
  cartStore = inject(CartStore);
  orderService = inject(OrderService);
  destroy = inject(DestroyRef);
  notificationStore = inject(NotificationStore);
  closingCart: WritableSignal<boolean> = signal(false);
  xMarkIcon = faXmark;
  plusIcon = faPlus;
  minusIcon = faMinus;

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
    if (this.cartStore.items().length !== 0) {
      const newOrder: NewOrder = {
        customer: {
          email: 'john.doe@example.com',
          name: 'John Doe',
          phone: '+1234567890',
        },
        deliveryAddress: {
          addressLine1: '123 Main Street',
          addressLine2: 'Apt 4B',
          city: 'Springfield',
          country: 'USA',
          state: 'IL',
          zipCode: '62701',
        },
        items: this.cartStore.items(),
      };
      this.orderService
        .submitOrder(newOrder)
        .pipe(takeUntilDestroyed(this.destroy))
        .subscribe({
          next: (response) => {
            this.notificationStore.notify(
              `order created with number ${response.orderNumber}`,
              NotificationType.SUCCESS
            );
          },
        });
      console.log(newOrder);
    }
  }

  @HostListener('document:click', ['$event'])
  @HostListener('document:touchstart', ['$event'])
  handleOutsideClick(event: Event) {
    const cartDiv = document.querySelector('#cartDiv');
    const closeButton = document.querySelector('#closeCart');

    if (
      cartDiv &&
      !cartDiv.contains(event.target as Node) &&
      !closeButton?.contains(event.target as Node)
    ) {
      this.closeCart();
    }
  }
}
