import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MaterielResolve from './route/materiel-routing-resolve.service';

const materielRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/materiel.component').then(m => m.MaterielComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/materiel-detail.component').then(m => m.MaterielDetailComponent),
    resolve: {
      materiel: MaterielResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/materiel-update.component').then(m => m.MaterielUpdateComponent),
    resolve: {
      materiel: MaterielResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/materiel-update.component').then(m => m.MaterielUpdateComponent),
    resolve: {
      materiel: MaterielResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materielRoute;
