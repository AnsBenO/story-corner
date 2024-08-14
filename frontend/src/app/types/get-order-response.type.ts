import { BookCartItem } from './book-cart-item.type';
import { Customer, DeliveryAddress } from './customer.type';

export interface OrderResponse {
  orderNumber: string;
  user: string;
  items: BookCartItem[];
  customer: Customer;
  deliveryAddress: DeliveryAddress;
  status: string;
  comments?: string | null;
  createdAt: Date;
}
