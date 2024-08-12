// get all orders response

export interface OrderItem {
  orderNumber: string;
  status: string;
}

export type GetAllOrdersResponse = OrderItem[];
