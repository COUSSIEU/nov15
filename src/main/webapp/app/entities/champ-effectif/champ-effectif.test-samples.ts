import { IChampEffectif, NewChampEffectif } from './champ-effectif.model';

export const sampleWithRequiredData: IChampEffectif = {
  id: 25846,
};

export const sampleWithPartialData: IChampEffectif = {
  id: 4072,
};

export const sampleWithFullData: IChampEffectif = {
  id: 12553,
  nom: 'sitôt que',
  valeur: 16931,
};

export const sampleWithNewData: NewChampEffectif = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
