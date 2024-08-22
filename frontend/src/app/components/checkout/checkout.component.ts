import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
  OnInit,
} from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { OrderService } from '../../services/order.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CartStore } from '../../store/cart.store';
import { Customer, NewOrderPayload } from '../../types/new-order-payload.type';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NotificationStore,
  NotificationType,
} from '../../store/notification.store';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, ReactiveFormsModule, RouterModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckoutComponent implements OnInit {
  ngOnInit(): void {
    this.notificationStore.notify(
      'Fill in the form to proceed',
      NotificationType.INFO
    );
  }
  router = inject(Router);
  cartStore = inject(CartStore);
  destroy = inject(DestroyRef);
  orderService = inject(OrderService);
  notificationStore = inject(NotificationStore);
  fb = inject(FormBuilder);
  xMarkIcon = faXmark;
  authService = inject(AuthService);

  navigateHome() {
    this.router.navigate(['/']);
  }
  checkoutForm = this.fb.nonNullable.group({
    addressLine1: ['', [Validators.required]],
    addressLine2: [''],
    city: ['', [Validators.required]],
    state: ['', [Validators.required]],
    zipCode: ['', [Validators.required]],
    country: ['', [Validators.required]],
  });

  submitOrder() {
    const currentUser = this.authService.currentUser();
    if (currentUser !== null && currentUser !== undefined) {
      const items = this.cartStore.items();
      items.forEach((i) => {
        delete i.imageUrl;
      });
      const customer: Customer = {
        username: currentUser.username,
        email: currentUser.email,
        phone: currentUser.phone,
      };
      if (this.cartStore.items().length !== 0) {
        const NewOrderPayload: NewOrderPayload = {
          deliveryAddress: this.checkoutForm.getRawValue(),
          items,
          customer,
        };
        this.orderService
          .submitOrder(NewOrderPayload)
          .pipe(takeUntilDestroyed(this.destroy))
          .subscribe({
            next: (response) => {
              this.notificationStore.notify(
                `order created with number ${response.orderNumber}`,
                NotificationType.SUCCESS
              );
            },
          });
      }
    }
  }
}
