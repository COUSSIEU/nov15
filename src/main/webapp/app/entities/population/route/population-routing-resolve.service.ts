import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPopulation } from '../population.model';
import { PopulationService } from '../service/population.service';

const populationResolve = (route: ActivatedRouteSnapshot): Observable<null | IPopulation> => {
  const id = route.params.id;
  if (id) {
    return inject(PopulationService)
      .find(id)
      .pipe(
        mergeMap((population: HttpResponse<IPopulation>) => {
          if (population.body) {
            return of(population.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default populationResolve;
