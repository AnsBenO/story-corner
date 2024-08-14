// request body for POST new order

import { BookCartItem } from './book-cart-item.type';
import { Customer, DeliveryAddress } from './customer.type';

export interface NewOrder {
  customer: Customer;
  deliveryAddress: DeliveryAddress;
  items: BookCartItem[];
}
