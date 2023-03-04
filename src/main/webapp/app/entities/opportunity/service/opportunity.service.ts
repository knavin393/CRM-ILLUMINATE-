import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOpportunity, NewOpportunity } from '../opportunity.model';

export type PartialUpdateOpportunity = Partial<IOpportunity> & Pick<IOpportunity, 'id'>;

export type EntityResponseType = HttpResponse<IOpportunity>;
export type EntityArrayResponseType = HttpResponse<IOpportunity[]>;

@Injectable({ providedIn: 'root' })
export class OpportunityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/opportunities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(opportunity: NewOpportunity): Observable<EntityResponseType> {
    return this.http.post<IOpportunity>(this.resourceUrl, opportunity, { observe: 'response' });
  }

  update(opportunity: IOpportunity): Observable<EntityResponseType> {
    return this.http.put<IOpportunity>(`${this.resourceUrl}/${this.getOpportunityIdentifier(opportunity)}`, opportunity, {
      observe: 'response',
    });
  }

  partialUpdate(opportunity: PartialUpdateOpportunity): Observable<EntityResponseType> {
    return this.http.patch<IOpportunity>(`${this.resourceUrl}/${this.getOpportunityIdentifier(opportunity)}`, opportunity, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOpportunity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOpportunity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOpportunityIdentifier(opportunity: Pick<IOpportunity, 'id'>): number {
    return opportunity.id;
  }

  compareOpportunity(o1: Pick<IOpportunity, 'id'> | null, o2: Pick<IOpportunity, 'id'> | null): boolean {
    return o1 && o2 ? this.getOpportunityIdentifier(o1) === this.getOpportunityIdentifier(o2) : o1 === o2;
  }

  addOpportunityToCollectionIfMissing<Type extends Pick<IOpportunity, 'id'>>(
    opportunityCollection: Type[],
    ...opportunitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const opportunities: Type[] = opportunitiesToCheck.filter(isPresent);
    if (opportunities.length > 0) {
      const opportunityCollectionIdentifiers = opportunityCollection.map(
        opportunityItem => this.getOpportunityIdentifier(opportunityItem)!
      );
      const opportunitiesToAdd = opportunities.filter(opportunityItem => {
        const opportunityIdentifier = this.getOpportunityIdentifier(opportunityItem);
        if (opportunityCollectionIdentifiers.includes(opportunityIdentifier)) {
          return false;
        }
        opportunityCollectionIdentifiers.push(opportunityIdentifier);
        return true;
      });
      return [...opportunitiesToAdd, ...opportunityCollection];
    }
    return opportunityCollection;
  }
}
