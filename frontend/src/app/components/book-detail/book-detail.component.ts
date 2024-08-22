import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
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
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BookDetailComponent {
  @Input() book!: Book;
  @Output() onClose = new EventEmitter<void>();
  @Output() onAddToCart = new EventEmitter<Book>();
  cartStore = inject(CartStore);
  closingModal: WritableSignal<boolean> = signal(false);
  xMark = faXmark;

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
  @HostListener('document:click', ['$event'])
  @HostListener('document:touchstart', ['$event'])
  handleOutsideClick(event: Event) {
    const modalDiv = document.querySelector('#modalDiv');
    const closeButton = document.querySelector('#closeModal');

    if (
      modalDiv &&
      !modalDiv.contains(event.target as Node) &&
      !closeButton?.contains(event.target as Node)
    ) {
      this.closeModal();
    }
  }
}
