import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMateriel, NewMateriel } from '../materiel.model';

export type PartialUpdateMateriel = Partial<IMateriel> & Pick<IMateriel, 'id'>;

export type EntityResponseType = HttpResponse<IMateriel>;
export type EntityArrayResponseType = HttpResponse<IMateriel[]>;

@Injectable({ providedIn: 'root' })
export class MaterielService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/materiels');

  create(materiel: NewMateriel): Observable<EntityResponseType> {
    return this.http.post<IMateriel>(this.resourceUrl, materiel, { observe: 'response' });
  }

  update(materiel: IMateriel): Observable<EntityResponseType> {
    return this.http.put<IMateriel>(`${this.resourceUrl}/${this.getMaterielIdentifier(materiel)}`, materiel, { observe: 'response' });
  }

  partialUpdate(materiel: PartialUpdateMateriel): Observable<EntityResponseType> {
    return this.http.patch<IMateriel>(`${this.resourceUrl}/${this.getMaterielIdentifier(materiel)}`, materiel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMateriel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMateriel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterielIdentifier(materiel: Pick<IMateriel, 'id'>): number {
    return materiel.id;
  }

  compareMateriel(o1: Pick<IMateriel, 'id'> | null, o2: Pick<IMateriel, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterielIdentifier(o1) === this.getMaterielIdentifier(o2) : o1 === o2;
  }

  addMaterielToCollectionIfMissing<Type extends Pick<IMateriel, 'id'>>(
    materielCollection: Type[],
    ...materielsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materiels: Type[] = materielsToCheck.filter(isPresent);
    if (materiels.length > 0) {
      const materielCollectionIdentifiers = materielCollection.map(materielItem => this.getMaterielIdentifier(materielItem));
      const materielsToAdd = materiels.filter(materielItem => {
        const materielIdentifier = this.getMaterielIdentifier(materielItem);
        if (materielCollectionIdentifiers.includes(materielIdentifier)) {
          return false;
        }
        materielCollectionIdentifiers.push(materielIdentifier);
        return true;
      });
      return [...materielsToAdd, ...materielCollection];
    }
    return materielCollection;
  }
}
