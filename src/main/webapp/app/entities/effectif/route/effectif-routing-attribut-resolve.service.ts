import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEffectif } from '../effectif.model';
import { EffectifService } from '../service/effectif.service';

const effectifAttributResolve = (route: ActivatedRouteSnapshot): Observable<null | IEffectif> => {
  const id = route.params.id;
  if (id) {
    return inject(EffectifService)
      .find(id)
      .pipe(
        mergeMap((effectif: HttpResponse<IEffectif>) => {
          if (effectif.body) {
            return of(effectif.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default effectifAttributResolve;
