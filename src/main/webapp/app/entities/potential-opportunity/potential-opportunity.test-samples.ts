import { POStatus } from 'app/entities/enumerations/po-status.model';

import { IPotentialOpportunity, NewPotentialOpportunity } from './potential-opportunity.model';

export const sampleWithRequiredData: IPotentialOpportunity = {
  id: 50917,
  status: POStatus['DELETE'],
};

export const sampleWithPartialData: IPotentialOpportunity = {
  id: 64942,
  status: POStatus['MAINTAIN_PO'],
};

export const sampleWithFullData: IPotentialOpportunity = {
  id: 42442,
  followUp: 'collaboration indigo 1080p',
  status: POStatus['OPPORTUNITY'],
};

export const sampleWithNewData: NewPotentialOpportunity = {
  status: POStatus['DELETE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
