import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ChampEffectifResolve from './route/champ-effectif-routing-resolve.service';

const champEffectifRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/champ-effectif.component').then(m => m.ChampEffectifComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/champ-effectif-detail.component').then(m => m.ChampEffectifDetailComponent),
    resolve: {
      champEffectif: ChampEffectifResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/champ-effectif-update.component').then(m => m.ChampEffectifUpdateComponent),
    resolve: {
      champEffectif: ChampEffectifResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/champ-effectif-update.component').then(m => m.ChampEffectifUpdateComponent),
    resolve: {
      champEffectif: ChampEffectifResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default champEffectifRoute;
