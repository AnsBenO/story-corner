import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
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