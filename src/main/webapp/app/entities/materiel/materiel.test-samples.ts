import { IMateriel, NewMateriel } from './materiel.model';

export const sampleWithRequiredData: IMateriel = {
  id: 14769,
};

export const sampleWithPartialData: IMateriel = {
  id: 17458,
  release: 'au moyen de',
  modele: 'actionnaire',
  sorte: 'équipe de recherche redevenir chef de cuisine',
  entite: 'aux alentours de ouf',
};

export const sampleWithFullData: IMateriel = {
  id: 4495,
  etat: 'cacher de peur que ouf',
  release: 'lorsque',
  modele: 'hé',
  sorte: 'loin de réaliser',
  site: 'autour de d’autant que meuh',
  region: 'bang',
  mission: 'solitaire égoïste',
  entite: 'sympathique multiple',
};

export const sampleWithNewData: NewMateriel = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
