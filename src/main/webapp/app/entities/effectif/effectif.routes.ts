import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EffectifResolve from './route/effectif-routing-resolve.service';
import EffectifAttributResolve from './route/effectif-routing-attribut-resolve.service';

const effectifRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/effectif.component').then(m => m.EffectifComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':tab/:lin/:col',
    loadComponent: () => import('./detail/effectif-detail.component').then(m => m.EffectifDetailComponent),
    resolve: {
      effectif: EffectifResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/effectif-detail.component').then(m => m.EffectifDetailComponent),
    resolve: {
      effectif: EffectifResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/effectif-update.component').then(m => m.EffectifUpdateComponent),
    resolve: {
      effectif: EffectifResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/effectif-update.component').then(m => m.EffectifUpdateComponent),
    resolve: {
      effectif: EffectifResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default effectifRoute;
