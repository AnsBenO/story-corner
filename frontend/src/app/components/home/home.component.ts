import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import { BookService } from '../../services/book.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BooksPage } from '../../types/books-page.type';
import { patchState, signalState } from '@ngrx/signals';
import { Book } from '../../types/book.type';
import { BookDetailComponent } from '../book-detail/book-detail.component';
import { CartStore } from '../../store/cart.store';
import { BookCartItem } from '../../types/book-cart-item.type';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, BookDetailComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomeComponent implements OnInit {
  cartStore = inject(CartStore);
  bookService = inject(BookService);
  destroy = inject(DestroyRef);
  pageState = signalState<BooksPage>({
    data: [],
    totalElements: 0,
    pageNumber: 0,
    totalPages: 0,
    isFirst: false,
    isLast: false,
    hasNext: false,
    hasPrevious: false,
  });

  showBookDetailModal: WritableSignal<boolean> = signal(false);
  selectedBook: Book | null = null;

  ngOnInit(): void {
    this.fetchBooks(1);
  }

  private fetchBooks(page: number) {
    this.bookService
      .getAllBooks(page)
      .pipe(takeUntilDestroyed(this.destroy))
      .subscribe({
        next: (pageData) => this.updatePageState(pageData),
      });
  }

  private updatePageState(pageData: BooksPage) {
    const updatedBooks = [...this.pageState().data, ...pageData.data];
    patchState(this.pageState, { ...pageData, data: updatedBooks });
  }

  openModal(book: Book) {
    this.selectedBook = book;
    this.showBookDetailModal.set(true);
  }

  closeModal() {
    this.showBookDetailModal.set(false);
    this.selectedBook = null;
  }

  loadMore() {
    if (this.pageState().hasNext) {
      const nextPage = this.pageState().pageNumber + 1;
      this.fetchBooks(nextPage);
    }
  }

  addToCart(book: Book) {
    const updatedItem: BookCartItem = {
      imageUrl: book.imageUrl,
      code: book.code,
      name: book.name,
      price: book.price,
      quantity: 1,
    };
    this.cartStore.addItem(updatedItem);
  }
}
