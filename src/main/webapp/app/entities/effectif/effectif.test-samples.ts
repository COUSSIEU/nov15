import { IEffectif, NewEffectif } from './effectif.model';

export const sampleWithRequiredData: IEffectif = {
  id: 17742,
};

export const sampleWithPartialData: IEffectif = {
  id: 27115,
  cumul: 8487,
};

export const sampleWithFullData: IEffectif = {
  id: 23212,
  name: 'membre du personnel groin groin',
  cumul: 12649,
};

export const sampleWithNewData: NewEffectif = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
