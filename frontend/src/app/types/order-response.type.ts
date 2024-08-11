// response of get order by code

import { Item } from './book-item.type';
import { Customer, DeliveryAddress } from './customer.type';

export interface OrderResponse {
  orderNumber: string;
  user: string;
  items: Item[];
  customer: Customer;
  deliveryAddress: DeliveryAddress;
  status: string;
  comments?: string | null;
  createdAt: Date;
}
