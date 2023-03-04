import { Gender } from 'app/entities/enumerations/gender.model';
import { SocMed } from 'app/entities/enumerations/soc-med.model';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
  empName: 'transmitting',
  empAge: 37,
  gender: Gender['MALE'],
  empJobTitle: 'Quality Cambridgeshire asymmetric',
  empPhone: 'Administrator ADP',
  empEmail: '?SNKz4@/q5>com',
};

export const sampleWithPartialData: IEmployee = {
  id: 46107,
  empName: 'Accountability invoice Berkshire',
  empAge: 44,
  gender: Gender['MALE'],
  empJobTitle: 'GB open-source',
  empPhone: 'Loan challenge Iranian',
  empEmail: ',V9@jRJkT_com',
  engage: SocMed['Instagram'],
};

export const sampleWithFullData: IEmployee = {
  id: 27248,
  empName: 'Music Divide',
  empAge: 44,
  gender: Gender['FEMALE'],
  empJobTitle: 'feed',
  empPhone: 'Bhutanese',
  empEmail: 'xB@5`o<50com',
  engage: SocMed['Twitter'],
};

export const sampleWithNewData: NewEmployee = {
  empName: 'International Table',
  empAge: 37,
  gender: Gender['FEMALE'],
  empJobTitle: 'web Missouri Salad',
  empPhone: 'global XML',
  empEmail: 'Cl@U7sTAcom',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
