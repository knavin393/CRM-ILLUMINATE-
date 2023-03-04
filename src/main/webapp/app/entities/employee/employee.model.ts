import { Gender } from 'app/entities/enumerations/gender.model';
import { SocMed } from 'app/entities/enumerations/soc-med.model';

export interface IEmployee {
  id: number;
  empName?: string | null;
  empAge?: number | null;
  gender?: Gender | null;
  empJobTitle?: string | null;
  empPhone?: string | null;
  empEmail?: string | null;
  engage?: SocMed | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
