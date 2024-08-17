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
import { Router } from '@angular/router';

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
  router = inject(Router);
  destroy = inject(DestroyRef);

  xMarkIcon = faXmark;
  plusIcon = faPlus;
  minusIcon = faMinus;
  closingCart: WritableSignal<boolean> = signal(false);

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
