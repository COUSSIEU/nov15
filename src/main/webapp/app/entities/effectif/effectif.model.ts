export interface IEffectif {
  id: number;
  name?: string | null;
  cumul?: number | null;
}

export type NewEffectif = Omit<IEffectif, 'id'> & { id: null };
