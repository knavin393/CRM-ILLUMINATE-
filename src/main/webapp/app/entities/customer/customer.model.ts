import { ICompany } from 'app/entities/company/company.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { SocMed } from 'app/entities/enumerations/soc-med.model';

export interface ICustomer {
  id: number;
  custName?: string | null;
  gender?: Gender | null;
  custIC?: string | null;
  custEmail?: string | null;
  custPhone?: string | null;
  companyName?: string | null;
  custJobTitle?: string | null;
  engage?: SocMed | null;
  company?: Pick<ICompany, 'id'> | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
