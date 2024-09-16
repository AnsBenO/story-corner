import {
  patchState,
  signalStore,
  withComputed,
  withHooks,
  withMethods,
  withState,
} from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { BookCartItem } from '../types/book-cart-item.type';
import { computed, inject } from '@angular/core';
import { pipe, switchMap, tap } from 'rxjs';
import { OrderService } from '../services/order.service';
import { NotificationStore, NotificationType } from './notification.store';
import { Router } from '@angular/router';

type TCartState = {
  items: BookCartItem[];
};

const initialState: TCartState = {
  items: [],
};

export const CartStore = signalStore(
  { providedIn: 'root' },
  withState<TCartState>(initialState),
  withComputed((store) => ({
    itemsCount: computed(() => store.items().length),
    totalPrice: computed(() =>
      store.items().reduce((prev, item) => prev + item.price * item.quantity, 0)
    ),
  })),
  withMethods(
    (
      store,
      orderService = inject(OrderService),
      notificationStore = inject(NotificationStore),
      router = inject(Router)
    ) => ({
      addItem(newItem: BookCartItem) {
        const currentItems = store.items();
        const existingItemIndex = currentItems.findIndex(
          (item) => item.code === newItem.code
        );

        if (existingItemIndex === -1) {
          const updatedItems = [...currentItems, newItem];
          patchState(store, { items: updatedItems });
          notificationStore.notify('Item added to cart', NotificationType.INFO);
        } else {
          notificationStore.notify(
            'Item already in your cart',
            NotificationType.INFO
          );
        }
        localStorage.setItem('cartItems', JSON.stringify(store.items()));
      },
      removeItem(code: string) {
        const updatedItems = store.items().filter((item) => item.code !== code);
        patchState(store, { items: updatedItems });
        localStorage.setItem('cartItems', JSON.stringify(updatedItems));
        notificationStore.notify(
          'Item removed from cart',
          NotificationType.INFO
        );
      },
      removeAll() {
        if (store.items().length > 0) {
          patchState(store, { items: [] });
          localStorage.setItem('cartItems', JSON.stringify([]));
          notificationStore.notify(
            'Removed all items from cart',
            NotificationType.INFO
          );
        }
      },
      increaseQuantity(code: string) {
        const updatedItems = store
          .items()
          .map((item) =>
            item.code === code ? { ...item, quantity: item.quantity + 1 } : item
          );
        patchState(store, { items: updatedItems });
        localStorage.setItem('cartItems', JSON.stringify(updatedItems));
      },
      decreaseQuantity(code: string) {
        const updatedItems = store
          .items()
          .map((item) =>
            item.code === code && item.quantity > 1
              ? { ...item, quantity: item.quantity - 1 }
              : item
          );
        patchState(store, { items: updatedItems });
        localStorage.setItem('cartItems', JSON.stringify(updatedItems));
      },
      checkout() {
        // waiting for the animation (cart fade out) before navigating to checkout
        setTimeout(() => {
          router.navigate(['/checkout']);
        }, 500);
      },
      loadItems: rxMethod<void>(
        pipe(
          switchMap(() => {
            return orderService
              .loadItemsFromLocalStorage()
              .pipe(
                tap((loadedItems) => patchState(store, { items: loadedItems }))
              );
          })
        )
      ),
    })
  ),
  withHooks({
    onInit(store) {
      store.loadItems();
    },
  })
);
