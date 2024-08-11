import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { BooksPage } from '../types/books-page.type';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  http = inject(HttpClient);

  getAllBooks(page?: number) {
    console.log('Current environment:', environment);

    return page
      ? this.http.get<BooksPage>(
          `${environment.BOOKS_API_URL}/books?page=${page}`
        )
      : this.http.get<BooksPage>(`${environment.BOOKS_API_URL}/books`);
  }
}
