import { IPopulation, NewPopulation } from './population.model';

export const sampleWithRequiredData: IPopulation = {
  id: 25639,
};

export const sampleWithPartialData: IPopulation = {
  id: 16684,
};

export const sampleWithFullData: IPopulation = {
  id: 30539,
  tableName: 'mature',
  attributNames: 'électorat cadre',
  typeNames: 'clac',
};

export const sampleWithNewData: NewPopulation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
