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
import { Item } from '../../types/book-item.type';

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

  showModal: WritableSignal<boolean> = signal(false);
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
        error: (error) => console.error('Error fetching books:', error),
      });
  }

  private updatePageState(pageData: BooksPage) {
    const updatedBooks = [...this.pageState().data, ...pageData.data];
    patchState(this.pageState, { ...pageData, data: updatedBooks });
  }

  openModal(book: Book) {
    this.selectedBook = book;
    this.showModal.set(true);
  }

  closeModal() {
    this.showModal.set(false);
    this.selectedBook = null;
  }

  loadMore() {
    if (this.pageState().hasNext) {
      const nextPage = this.pageState().pageNumber + 1;
      this.fetchBooks(nextPage);
    }
  }

  addToCart(book: Book) {
    const updatedItem: Item = {
      code: book.code,
      name: book.name,
      price: book.price,
      quantity: 1,
    };
    this.cartStore.addItem(updatedItem);
  }
}
