import { signalStore, withMethods, withState } from '@ngrx/signals';
import { OrderItem } from '../types/get-all-orders-response.type';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe } from 'rxjs';
import { OrderService } from '../services/order.service';
import { inject } from '@angular/core';

export type TInbox = {
  orders: OrderItem[];
  error: null | string;
  isLoading: boolean;
};

export const inboxStore = signalStore(
  withState<TInbox>({
    orders: [],
    error: null,
    isLoading: false,
  }),

  withMethods((store, orderService = inject(OrderService)) => ({
    loadOrders: rxMethod<void>(pipe()),
  }))
);
