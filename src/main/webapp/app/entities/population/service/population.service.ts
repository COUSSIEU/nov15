import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPopulation, NewPopulation } from '../population.model';

export type PartialUpdatePopulation = Partial<IPopulation> & Pick<IPopulation, 'id'>;

export type EntityResponseType = HttpResponse<IPopulation>;
export type EntityArrayResponseType = HttpResponse<IPopulation[]>;

@Injectable({ providedIn: 'root' })
export class PopulationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/populations');

  create(population: NewPopulation): Observable<EntityResponseType> {
    return this.http.post<IPopulation>(this.resourceUrl, population, { observe: 'response' });
  }

  update(population: IPopulation): Observable<EntityResponseType> {
    return this.http.put<IPopulation>(`${this.resourceUrl}/${this.getPopulationIdentifier(population)}`, population, {
      observe: 'response',
    });
  }

  partialUpdate(population: PartialUpdatePopulation): Observable<EntityResponseType> {
    return this.http.patch<IPopulation>(`${this.resourceUrl}/${this.getPopulationIdentifier(population)}`, population, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPopulation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPopulation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPopulationIdentifier(population: Pick<IPopulation, 'id'>): number {
    return population.id;
  }

  comparePopulation(o1: Pick<IPopulation, 'id'> | null, o2: Pick<IPopulation, 'id'> | null): boolean {
    return o1 && o2 ? this.getPopulationIdentifier(o1) === this.getPopulationIdentifier(o2) : o1 === o2;
  }

  addPopulationToCollectionIfMissing<Type extends Pick<IPopulation, 'id'>>(
    populationCollection: Type[],
    ...populationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const populations: Type[] = populationsToCheck.filter(isPresent);
    if (populations.length > 0) {
      const populationCollectionIdentifiers = populationCollection.map(populationItem => this.getPopulationIdentifier(populationItem));
      const populationsToAdd = populations.filter(populationItem => {
        const populationIdentifier = this.getPopulationIdentifier(populationItem);
        if (populationCollectionIdentifiers.includes(populationIdentifier)) {
          return false;
        }
        populationCollectionIdentifiers.push(populationIdentifier);
        return true;
      });
      return [...populationsToAdd, ...populationCollection];
    }
    return populationCollection;
  }
}
