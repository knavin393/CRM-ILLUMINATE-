import { Gender } from 'app/entities/enumerations/gender.model';
import { SocMed } from 'app/entities/enumerations/soc-med.model';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 24379,
  custName: 'Operations',
  gender: Gender['MALE'],
  custIC: 'Loan Gorgeous generate',
  custEmail: 'uPqX@%(com',
  custPhone: 'National e-business hard',
  companyName: 'input',
  custJobTitle: 'front-end Towels',
};

export const sampleWithPartialData: ICustomer = {
  id: 26504,
  custName: 'Euro North',
  gender: Gender['MALE'],
  custIC: 'seize silver',
  custEmail: "n7,'A@T!com",
  custPhone: 'parse',
  companyName: 'calculate up',
  custJobTitle: 'Orchestrator deposit Home',
  engage: SocMed['Website'],
};

export const sampleWithFullData: ICustomer = {
  id: 13258,
  custName: 'Benin',
  gender: Gender['FEMALE'],
  custIC: 'withdrawal Oval Centralized',
  custEmail: 'LM(@+icom',
  custPhone: 'Personal framework Bedfordshire',
  companyName: 'Saint bypass bandwidth',
  custJobTitle: 'primary Dollar',
  engage: SocMed['Twitter'],
};

export const sampleWithNewData: NewCustomer = {
  custName: 'Mouse optical',
  gender: Gender['MALE'],
  custIC: 'Wooden Metal',
  custEmail: 'fC{2@"y\'qI$com',
  custPhone: 'contingency lime Music',
  companyName: 'application PCI indexing',
  custJobTitle: 'Automotive',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
