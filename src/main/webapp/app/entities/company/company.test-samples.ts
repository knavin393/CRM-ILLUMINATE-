import { SocMed } from 'app/entities/enumerations/soc-med.model';

import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 32440,
  industry: 'Djibouti Washington',
  location: 'Awesome Avon Avon',
  established: 'Borders',
};

export const sampleWithPartialData: ICompany = {
  id: 45660,
  industry: 'Intelligent Hat',
  location: 'Mouse',
  established: 'Chips Ford',
  engage: SocMed['Twitter'],
};

export const sampleWithFullData: ICompany = {
  id: 11438,
  industry: '1080p transition',
  location: 'withdrawal Tala payment',
  established: 'syndicate Grocery',
  engage: SocMed['Website'],
};

export const sampleWithNewData: NewCompany = {
  industry: 'Coordinator',
  location: 'neural Account',
  established: 'models Plastic target',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
