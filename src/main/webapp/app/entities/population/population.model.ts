export interface IPopulation {
  id: number;
  tableName?: string | null;
  attributNames?: string | null;
  typeNames?: string | null;
}

export type NewPopulation = Omit<IPopulation, 'id'> & { id: null };
