import { ICustomer } from 'app/entities/customer/customer.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IProduct } from 'app/entities/product/product.model';
import { LeadStatus } from 'app/entities/enumerations/lead-status.model';

export interface ILead {
  id: number;
  followUp?: string | null;
  status?: LeadStatus | null;
  customer?: Pick<ICustomer, 'id' | 'custName'> | null;
  employee?: Pick<IEmployee, 'id' | 'empName'> | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewLead = Omit<ILead, 'id'> & { id: null };
