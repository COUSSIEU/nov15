export interface IMateriel {
  id: number;
  etat?: string | null;
  release?: string | null;
  modele?: string | null;
  sorte?: string | null;
  site?: string | null;
  region?: string | null;
  mission?: string | null;
  entite?: string | null;
}

export type NewMateriel = Omit<IMateriel, 'id'> & { id: null };
