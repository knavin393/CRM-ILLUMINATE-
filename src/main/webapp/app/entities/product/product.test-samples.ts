import { ProdName } from 'app/entities/enumerations/prod-name.model';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 77672,
  name: ProdName['PRODUCT_1'],
  price: 25019,
};

export const sampleWithPartialData: IProduct = {
  id: 82518,
  name: ProdName['PRODUCT_3'],
  price: 78560,
};

export const sampleWithFullData: IProduct = {
  id: 34739,
  name: ProdName['PRODUCT_2'],
  description: 'Networked',
  price: 40139,
};

export const sampleWithNewData: NewProduct = {
  name: ProdName[undefined],
  price: 304,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
