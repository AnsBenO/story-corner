import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Book } from '../../types/book.type';
import { CartStore } from '../../store/cart.store';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './book-detail.component.html',
  styleUrl: './book-detail.component.scss',
})
export class BookDetailComponent {
  @Input() book!: Book;
  @Output() onClose = new EventEmitter<void>();
  @Output() onAddToCart = new EventEmitter<Book>();
  cartStore = inject(CartStore);
  closingModal: WritableSignal<boolean> = signal(false);
  xMark = faXmark;

  toggleModalOutsideClick($event: MouseEvent) {
    const modalDiv = document.querySelector('#modalDiv');
    const targetElement = $event.target as HTMLElement;
    if (!modalDiv?.contains(targetElement)) {
      this.closeModal();
    }
  }
  closeModal() {
    this.closingModal.set(true);
    setTimeout(() => {
      this.closingModal.set(false);
      this.onClose.emit();
    }, 500);
  }

  addToCart(book: Book) {
    this.onAddToCart.emit(book);
  }
}
