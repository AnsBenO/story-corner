// request body for POST new order

import { BookCartItem } from './book-cart-item.type';

export interface NewOrderPayload {
  customer: Customer;
  deliveryAddress: DeliveryAddress;
  items: BookCartItem[];
}

export interface Customer {
  username: string;
  email: string;
  phone: string;
}

export interface DeliveryAddress {
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
}
