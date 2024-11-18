import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEffectif, NewEffectif } from '../effectif.model';

export type PartialUpdateEffectif = Partial<IEffectif> & Pick<IEffectif, 'id'>;

export type EntityResponseType = HttpResponse<IEffectif>;
export type EntityArrayResponseType = HttpResponse<IEffectif[]>;

@Injectable({ providedIn: 'root' })
export class EffectifService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/effectifs');

  create(effectif: NewEffectif): Observable<EntityResponseType> {
    return this.http.post<IEffectif>(this.resourceUrl, effectif, { observe: 'response' });
  }

  update(effectif: IEffectif): Observable<EntityResponseType> {
    return this.http.put<IEffectif>(`${this.resourceUrl}/${this.getEffectifIdentifier(effectif)}`, effectif, { observe: 'response' });
  }

  partialUpdate(effectif: PartialUpdateEffectif): Observable<EntityResponseType> {
    return this.http.patch<IEffectif>(`${this.resourceUrl}/${this.getEffectifIdentifier(effectif)}`, effectif, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEffectif>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEffectif[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEffectifIdentifier(effectif: Pick<IEffectif, 'id'>): number {
    return effectif.id;
  }

  compareEffectif(o1: Pick<IEffectif, 'id'> | null, o2: Pick<IEffectif, 'id'> | null): boolean {
    return o1 && o2 ? this.getEffectifIdentifier(o1) === this.getEffectifIdentifier(o2) : o1 === o2;
  }

  addEffectifToCollectionIfMissing<Type extends Pick<IEffectif, 'id'>>(
    effectifCollection: Type[],
    ...effectifsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const effectifs: Type[] = effectifsToCheck.filter(isPresent);
    if (effectifs.length > 0) {
      const effectifCollectionIdentifiers = effectifCollection.map(effectifItem => this.getEffectifIdentifier(effectifItem));
      const effectifsToAdd = effectifs.filter(effectifItem => {
        const effectifIdentifier = this.getEffectifIdentifier(effectifItem);
        if (effectifCollectionIdentifiers.includes(effectifIdentifier)) {
          return false;
        }
        effectifCollectionIdentifiers.push(effectifIdentifier);
        return true;
      });
      return [...effectifsToAdd, ...effectifCollection];
    }
    return effectifCollection;
  }
}
