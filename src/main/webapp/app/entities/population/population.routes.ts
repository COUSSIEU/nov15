import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PopulationResolve from './route/population-routing-resolve.service';

const populationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/population.component').then(m => m.PopulationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/population-detail.component').then(m => m.PopulationDetailComponent),
    resolve: {
      population: PopulationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/population-update.component').then(m => m.PopulationUpdateComponent),
    resolve: {
      population: PopulationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/population-update.component').then(m => m.PopulationUpdateComponent),
    resolve: {
      population: PopulationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default populationRoute;
