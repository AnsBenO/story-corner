import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  EventEmitter,
  inject,
  Output,
  signal,
  WritableSignal,
} from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { OrderService } from '../../services/order.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CartStore } from '../../store/cart.store';
import { NewOrder } from '../../types/new-order.type';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NotificationStore,
  NotificationType,
} from '../../store/notification.store';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, ReactiveFormsModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckoutComponent {
  navigateHome() {
    throw new Error('Method not implemented.');
  }
  @Output() onClose = new EventEmitter<void>();
  cartStore = inject(CartStore);
  destroy = inject(DestroyRef);
  orderService = inject(OrderService);
  notificationStore = inject(NotificationStore);
  fb = inject(FormBuilder);
  xMarkIcon = faXmark;

  checkoutForm = this.fb.nonNullable.group({
    customer: this.fb.nonNullable.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: [
        '',
        [Validators.required, Validators.pattern(/^\+?[1-9]\d{1,14}$/)],
      ],
    }),
    deliveryAddress: this.fb.nonNullable.group({
      addressLine1: ['', [Validators.required]],
      addressLine2: [''],
      city: ['', [Validators.required]],
      state: ['', [Validators.required]],
      zipCode: ['', [Validators.required]],
      country: ['', [Validators.required]],
    }),
  });

  submitOrder() {
    if (this.cartStore.items().length !== 0) {
      const newOrder: NewOrder = {
        ...this.checkoutForm.getRawValue(),
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
}
