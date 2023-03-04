import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

import { IProductOrder, NewProductOrder } from './product-order.model';

export const sampleWithRequiredData: IProductOrder = {
  id: 84064,
  placedDate: 'Czech olive explicit',
  status: OrderItemStatus['AVAILABLE'],
  quantity: 48044,
  totalPrice: 26525,
};

export const sampleWithPartialData: IProductOrder = {
  id: 29907,
  placedDate: 'turquoise',
  status: OrderItemStatus['BACK_ORDER'],
  invoiceId: 'pixel silver',
  quantity: 22688,
  totalPrice: 91762,
};

export const sampleWithFullData: IProductOrder = {
  id: 61208,
  placedDate: 'Personal Towels Granite',
  status: OrderItemStatus['BACK_ORDER'],
  invoiceId: 'Incredible invoice',
  quantity: 90277,
  totalPrice: 5766,
};

export const sampleWithNewData: NewProductOrder = {
  placedDate: 'Minnesota workforce compress',
  status: OrderItemStatus['OUT_OF_STOCK'],
  quantity: 9201,
  totalPrice: 85532,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
