import { IEffectif } from 'app/entities/effectif/effectif.model';

export interface IChampEffectif {
  id: number;
  nom?: string | null;
  valeur?: number | null;
  effectif?: Pick<IEffectif, 'id'> | null;
}

export type NewChampEffectif = Omit<IChampEffectif, 'id'> & { id: null };
