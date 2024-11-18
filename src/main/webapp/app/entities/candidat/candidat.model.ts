export interface ICandidat {
  id: number;
  nom?: string | null;
  age?: number | null;
  springboot?: number | null;
  angular?: number | null;
  html?: number | null;
  css?: number | null;
  transport?: number | null;
  sport?: string | null;
}

export type NewCandidat = Omit<ICandidat, 'id'> & { id: null };
