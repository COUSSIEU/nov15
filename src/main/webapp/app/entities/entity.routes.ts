import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'population',
    data: { pageTitle: 'Populations' },
    loadChildren: () => import('./population/population.routes'),
  },
  {
    path: 'champ-effectif',
    data: { pageTitle: 'ChampEffectifs' },
    loadChildren: () => import('./champ-effectif/champ-effectif.routes'),
  },
  {
    path: 'effectif',
    data: { pageTitle: 'Effectifs' },
    loadChildren: () => import('./effectif/effectif.routes'),
  },
  {
    path: 'candidat',
    data: { pageTitle: 'Candidats' },
    loadChildren: () => import('./candidat/candidat.routes'),
  },
  {
    path: 'materiel',
    data: { pageTitle: 'Materiels' },
    loadChildren: () => import('./materiel/materiel.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
