import { IWinLoss, NewWinLoss } from './win-loss.model';

export const sampleWithRequiredData: IWinLoss = {
  id: 31769,
};

export const sampleWithPartialData: IWinLoss = {
  id: 1395,
};

export const sampleWithFullData: IWinLoss = {
  id: 95264,
  notes: 'Account',
};

export const sampleWithNewData: NewWinLoss = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
