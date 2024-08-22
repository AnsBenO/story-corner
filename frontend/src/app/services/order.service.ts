import { inject, Injectable } from '@angular/core';
import { BookCartItem } from '../types/book-cart-item.type';
import { Observable, of, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment';
import { NewOrderPayload } from '../types/new-order-payload.type';
import { NewOrderResponse } from '../types/new-order-response.type';
import {
  NotificationStore,
  NotificationType,
} from '../store/notification.store';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  http = inject(HttpClient);
  notificationStore = inject(NotificationStore);

  loadItemsFromLocalStorage(): Observable<BookCartItem[]> {
    const itemsJson = localStorage.getItem('cartItems');
    let items: BookCartItem[] = [];

    if (itemsJson) {
      try {
        items = JSON.parse(itemsJson);
      } catch (e) {
        console.error('Error parsing items from localStorage', e);
      }
    }

    return of(items);
  }

  submitOrder(order: NewOrderPayload) {
    return this.http
      .post<NewOrderResponse>(`${environment.API_URL}/orders`, order)
      .pipe(
        tap((response) => {
          const message = `Order submitted successfully! Your order number is ${response.orderNumber}.`;
          this.notificationStore.notify(message, NotificationType.SUCCESS);
        })
      );
  }
}
