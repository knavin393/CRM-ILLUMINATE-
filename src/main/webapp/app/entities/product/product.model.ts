import { ProdName } from 'app/entities/enumerations/prod-name.model';

export interface IProduct {
  id: number;
  name?: ProdName | null;
  description?: string | null;
  price?: number | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
