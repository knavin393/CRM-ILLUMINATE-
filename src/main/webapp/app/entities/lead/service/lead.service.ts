import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILead, NewLead } from '../lead.model';

export type PartialUpdateLead = Partial<ILead> & Pick<ILead, 'id'>;

export type EntityResponseType = HttpResponse<ILead>;
export type EntityArrayResponseType = HttpResponse<ILead[]>;

@Injectable({ providedIn: 'root' })
export class LeadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lead: NewLead): Observable<EntityResponseType> {
    return this.http.post<ILead>(this.resourceUrl, lead, { observe: 'response' });
  }

  update(lead: ILead): Observable<EntityResponseType> {
    return this.http.put<ILead>(`${this.resourceUrl}/${this.getLeadIdentifier(lead)}`, lead, { observe: 'response' });
  }

  partialUpdate(lead: PartialUpdateLead): Observable<EntityResponseType> {
    return this.http.patch<ILead>(`${this.resourceUrl}/${this.getLeadIdentifier(lead)}`, lead, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILead>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILead[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLeadIdentifier(lead: Pick<ILead, 'id'>): number {
    return lead.id;
  }

  compareLead(o1: Pick<ILead, 'id'> | null, o2: Pick<ILead, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeadIdentifier(o1) === this.getLeadIdentifier(o2) : o1 === o2;
  }

  addLeadToCollectionIfMissing<Type extends Pick<ILead, 'id'>>(
    leadCollection: Type[],
    ...leadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leads: Type[] = leadsToCheck.filter(isPresent);
    if (leads.length > 0) {
      const leadCollectionIdentifiers = leadCollection.map(leadItem => this.getLeadIdentifier(leadItem)!);
      const leadsToAdd = leads.filter(leadItem => {
        const leadIdentifier = this.getLeadIdentifier(leadItem);
        if (leadCollectionIdentifiers.includes(leadIdentifier)) {
          return false;
        }
        leadCollectionIdentifiers.push(leadIdentifier);
        return true;
      });
      return [...leadsToAdd, ...leadCollection];
    }
    return leadCollection;
  }
}
