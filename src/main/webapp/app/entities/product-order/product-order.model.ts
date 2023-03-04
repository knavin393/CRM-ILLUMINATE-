import { IProduct } from 'app/entities/product/product.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

export interface IProductOrder {
  id: number;
  placedDate?: string | null;
  status?: OrderItemStatus | null;
  invoiceId?: string | null;
  quantity?: number | null;
  totalPrice?: number | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
  customer?: Pick<ICustomer, 'id' | 'custEmail'> | null;
}

export type NewProductOrder = Omit<IProductOrder, 'id'> & { id: null };
