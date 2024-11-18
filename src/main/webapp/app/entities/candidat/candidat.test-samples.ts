import { ICandidat, NewCandidat } from './candidat.model';

export const sampleWithRequiredData: ICandidat = {
  id: 7719,
};

export const sampleWithPartialData: ICandidat = {
  id: 7282,
  nom: 'avant-hier avant que broum',
  springboot: 27397,
  css: 21270,
  sport: 'parlementaire',
};

export const sampleWithFullData: ICandidat = {
  id: 19754,
  nom: 'commis pff',
  age: 20945,
  springboot: 1845,
  angular: 25296,
  html: 26017,
  css: 497,
  transport: 26417,
  sport: 'fréquenter',
};

export const sampleWithNewData: NewCandidat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
