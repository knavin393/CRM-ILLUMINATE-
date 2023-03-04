import { OppStatus } from 'app/entities/enumerations/opp-status.model';

import { IOpportunity, NewOpportunity } from './opportunity.model';

export const sampleWithRequiredData: IOpportunity = {
  id: 97784,
  status: OppStatus['WIN'],
};

export const sampleWithPartialData: IOpportunity = {
  id: 39909,
  followUp: 'Licensed',
  status: OppStatus['LOSS'],
};

export const sampleWithFullData: IOpportunity = {
  id: 83697,
  followUp: 'reintermediate IB',
  status: OppStatus['LOSS'],
};

export const sampleWithNewData: NewOpportunity = {
  status: OppStatus['WIN'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
