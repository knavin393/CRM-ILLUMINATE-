import { SocMed } from 'app/entities/enumerations/soc-med.model';

export interface ICompany {
  id: number;
  industry?: string | null;
  location?: string | null;
  established?: string | null;
  engage?: SocMed | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
