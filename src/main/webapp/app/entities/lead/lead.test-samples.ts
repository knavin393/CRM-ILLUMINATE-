import { LeadStatus } from 'app/entities/enumerations/lead-status.model';

import { ILead, NewLead } from './lead.model';

export const sampleWithRequiredData: ILead = {
  id: 6061,
  status: LeadStatus['DELETE'],
};

export const sampleWithPartialData: ILead = {
  id: 11314,
  followUp: 'Phased',
  status: LeadStatus['MAINTAIN_LEAD'],
};

export const sampleWithFullData: ILead = {
  id: 99708,
  followUp: 'neural Multi-layered',
  status: LeadStatus['MAINTAIN_LEAD'],
};

export const sampleWithNewData: NewLead = {
  status: LeadStatus['DELETE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
