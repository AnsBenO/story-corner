import { Injectable } from '@angular/core';
import { Item } from '../types/book-item.type';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  constructor() {}

  loadItemsFromLocalStorage(): Observable<Item[]> {
    const itemsJson = localStorage.getItem('cartItems');
    let items: Item[] = [];

    if (itemsJson) {
      try {
        items = JSON.parse(itemsJson);
      } catch (e) {
        console.error('Error parsing items from localStorage', e);
      }
    }

    return of(items);
  }
}
