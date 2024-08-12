import { inject, Injectable } from '@angular/core';
import { Item } from '../types/book-item.type';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
import { NewOrder } from '../types/new-order.type';
import { NewOrderResponse } from '../types/new-order-response.type';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  http = inject(HttpClient);

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

  submitOrder(order: NewOrder) {
    this.http.post<NewOrderResponse>(`${environment.API_URL}/orders`, order);
  }
}
