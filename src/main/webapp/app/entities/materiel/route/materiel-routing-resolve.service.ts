import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMateriel } from '../materiel.model';
import { MaterielService } from '../service/materiel.service';

const materielResolve = (route: ActivatedRouteSnapshot): Observable<null | IMateriel> => {
  const id = route.params.id;
  if (id) {
    return inject(MaterielService)
      .find(id)
      .pipe(
        mergeMap((materiel: HttpResponse<IMateriel>) => {
          if (materiel.body) {
            return of(materiel.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default materielResolve;
