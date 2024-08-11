import { Book } from './book.type';

// response for get books with optional page parameter
export interface BooksPage {
  data: Book[];
  totalElements: number;
  pageNumber: number;
  totalPages: number;
  isFirst: boolean;
  isLast: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}
