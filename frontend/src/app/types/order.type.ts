// request body for POST new order

import { Item } from './book-item.type';
import { Customer, DeliveryAddress } from './customer.type';

export interface OrderEntity {
  customer: Customer;
  deliveryAddress: DeliveryAddress;
  items: Item[];
}
