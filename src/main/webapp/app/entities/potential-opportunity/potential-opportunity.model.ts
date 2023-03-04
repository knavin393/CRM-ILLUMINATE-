import { ICustomer } from 'app/entities/customer/customer.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IProduct } from 'app/entities/product/product.model';
import { POStatus } from 'app/entities/enumerations/po-status.model';

export interface IPotentialOpportunity {
  id: number;
  followUp?: string | null;
  status?: POStatus | null;
  customer?: Pick<ICustomer, 'id' | 'custName'> | null;
  employee?: Pick<IEmployee, 'id' | 'empName'> | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewPotentialOpportunity = Omit<IPotentialOpportunity, 'id'> & { id: null };
