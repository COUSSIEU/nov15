import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChampEffectif } from '../champ-effectif.model';
import { ChampEffectifService } from '../service/champ-effectif.service';

const champEffectifResolve = (route: ActivatedRouteSnapshot): Observable<null | IChampEffectif> => {
  const id = route.params.id;
  if (id) {
    return inject(ChampEffectifService)
      .find(id)
      .pipe(
        mergeMap((champEffectif: HttpResponse<IChampEffectif>) => {
          if (champEffectif.body) {
            return of(champEffectif.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default champEffectifResolve;
