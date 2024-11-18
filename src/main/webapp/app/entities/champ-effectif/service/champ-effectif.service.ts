import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChampEffectif, NewChampEffectif } from '../champ-effectif.model';

export type PartialUpdateChampEffectif = Partial<IChampEffectif> & Pick<IChampEffectif, 'id'>;

export type EntityResponseType = HttpResponse<IChampEffectif>;
export type EntityArrayResponseType = HttpResponse<IChampEffectif[]>;

@Injectable({ providedIn: 'root' })
export class ChampEffectifService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/champ-effectifs');

  create(champEffectif: NewChampEffectif): Observable<EntityResponseType> {
    return this.http.post<IChampEffectif>(this.resourceUrl, champEffectif, { observe: 'response' });
  }

  update(champEffectif: IChampEffectif): Observable<EntityResponseType> {
    return this.http.put<IChampEffectif>(`${this.resourceUrl}/${this.getChampEffectifIdentifier(champEffectif)}`, champEffectif, {
      observe: 'response',
    });
  }

  partialUpdate(champEffectif: PartialUpdateChampEffectif): Observable<EntityResponseType> {
    return this.http.patch<IChampEffectif>(`${this.resourceUrl}/${this.getChampEffectifIdentifier(champEffectif)}`, champEffectif, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChampEffectif>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChampEffectif[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChampEffectifIdentifier(champEffectif: Pick<IChampEffectif, 'id'>): number {
    return champEffectif.id;
  }

  compareChampEffectif(o1: Pick<IChampEffectif, 'id'> | null, o2: Pick<IChampEffectif, 'id'> | null): boolean {
    return o1 && o2 ? this.getChampEffectifIdentifier(o1) === this.getChampEffectifIdentifier(o2) : o1 === o2;
  }

  addChampEffectifToCollectionIfMissing<Type extends Pick<IChampEffectif, 'id'>>(
    champEffectifCollection: Type[],
    ...champEffectifsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const champEffectifs: Type[] = champEffectifsToCheck.filter(isPresent);
    if (champEffectifs.length > 0) {
      const champEffectifCollectionIdentifiers = champEffectifCollection.map(champEffectifItem =>
        this.getChampEffectifIdentifier(champEffectifItem),
      );
      const champEffectifsToAdd = champEffectifs.filter(champEffectifItem => {
        const champEffectifIdentifier = this.getChampEffectifIdentifier(champEffectifItem);
        if (champEffectifCollectionIdentifiers.includes(champEffectifIdentifier)) {
          return false;
        }
        champEffectifCollectionIdentifiers.push(champEffectifIdentifier);
        return true;
      });
      return [...champEffectifsToAdd, ...champEffectifCollection];
    }
    return champEffectifCollection;
  }
}
