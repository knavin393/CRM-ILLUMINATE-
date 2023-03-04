import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPotentialOpportunity, NewPotentialOpportunity } from '../potential-opportunity.model';

export type PartialUpdatePotentialOpportunity = Partial<IPotentialOpportunity> & Pick<IPotentialOpportunity, 'id'>;

export type EntityResponseType = HttpResponse<IPotentialOpportunity>;
export type EntityArrayResponseType = HttpResponse<IPotentialOpportunity[]>;

@Injectable({ providedIn: 'root' })
export class PotentialOpportunityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/potential-opportunities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(potentialOpportunity: NewPotentialOpportunity): Observable<EntityResponseType> {
    return this.http.post<IPotentialOpportunity>(this.resourceUrl, potentialOpportunity, { observe: 'response' });
  }

  update(potentialOpportunity: IPotentialOpportunity): Observable<EntityResponseType> {
    return this.http.put<IPotentialOpportunity>(
      `${this.resourceUrl}/${this.getPotentialOpportunityIdentifier(potentialOpportunity)}`,
      potentialOpportunity,
      { observe: 'response' }
    );
  }

  partialUpdate(potentialOpportunity: PartialUpdatePotentialOpportunity): Observable<EntityResponseType> {
    return this.http.patch<IPotentialOpportunity>(
      `${this.resourceUrl}/${this.getPotentialOpportunityIdentifier(potentialOpportunity)}`,
      potentialOpportunity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPotentialOpportunity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPotentialOpportunity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPotentialOpportunityIdentifier(potentialOpportunity: Pick<IPotentialOpportunity, 'id'>): number {
    return potentialOpportunity.id;
  }

  comparePotentialOpportunity(o1: Pick<IPotentialOpportunity, 'id'> | null, o2: Pick<IPotentialOpportunity, 'id'> | null): boolean {
    return o1 && o2 ? this.getPotentialOpportunityIdentifier(o1) === this.getPotentialOpportunityIdentifier(o2) : o1 === o2;
  }

  addPotentialOpportunityToCollectionIfMissing<Type extends Pick<IPotentialOpportunity, 'id'>>(
    potentialOpportunityCollection: Type[],
    ...potentialOpportunitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const potentialOpportunities: Type[] = potentialOpportunitiesToCheck.filter(isPresent);
    if (potentialOpportunities.length > 0) {
      const potentialOpportunityCollectionIdentifiers = potentialOpportunityCollection.map(
        potentialOpportunityItem => this.getPotentialOpportunityIdentifier(potentialOpportunityItem)!
      );
      const potentialOpportunitiesToAdd = potentialOpportunities.filter(potentialOpportunityItem => {
        const potentialOpportunityIdentifier = this.getPotentialOpportunityIdentifier(potentialOpportunityItem);
        if (potentialOpportunityCollectionIdentifiers.includes(potentialOpportunityIdentifier)) {
          return false;
        }
        potentialOpportunityCollectionIdentifiers.push(potentialOpportunityIdentifier);
        return true;
      });
      return [...potentialOpportunitiesToAdd, ...potentialOpportunityCollection];
    }
    return potentialOpportunityCollection;
  }
}
