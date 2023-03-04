import { IOpportunity } from 'app/entities/opportunity/opportunity.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IWinLoss {
  id: number;
  notes?: string | null;
  opportunity?: Pick<IOpportunity, 'id' | 'status'> | null;
  customer?: Pick<ICustomer, 'id' | 'custName'> | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewWinLoss = Omit<IWinLoss, 'id'> & { id: null };
