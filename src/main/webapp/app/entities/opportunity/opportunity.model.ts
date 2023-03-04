import { ICustomer } from 'app/entities/customer/customer.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IProduct } from 'app/entities/product/product.model';
import { OppStatus } from 'app/entities/enumerations/opp-status.model';

export interface IOpportunity {
  id: number;
  followUp?: string | null;
  status?: OppStatus | null;
  customer?: Pick<ICustomer, 'id' | 'custName'> | null;
  employee?: Pick<IEmployee, 'id' | 'empName'> | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewOpportunity = Omit<IOpportunity, 'id'> & { id: null };
