import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWinLoss, NewWinLoss } from '../win-loss.model';

export type PartialUpdateWinLoss = Partial<IWinLoss> & Pick<IWinLoss, 'id'>;

export type EntityResponseType = HttpResponse<IWinLoss>;
export type EntityArrayResponseType = HttpResponse<IWinLoss[]>;

@Injectable({ providedIn: 'root' })
export class WinLossService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/win-losses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(winLoss: NewWinLoss): Observable<EntityResponseType> {
    return this.http.post<IWinLoss>(this.resourceUrl, winLoss, { observe: 'response' });
  }

  update(winLoss: IWinLoss): Observable<EntityResponseType> {
    return this.http.put<IWinLoss>(`${this.resourceUrl}/${this.getWinLossIdentifier(winLoss)}`, winLoss, { observe: 'response' });
  }

  partialUpdate(winLoss: PartialUpdateWinLoss): Observable<EntityResponseType> {
    return this.http.patch<IWinLoss>(`${this.resourceUrl}/${this.getWinLossIdentifier(winLoss)}`, winLoss, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWinLoss>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWinLoss[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWinLossIdentifier(winLoss: Pick<IWinLoss, 'id'>): number {
    return winLoss.id;
  }

  compareWinLoss(o1: Pick<IWinLoss, 'id'> | null, o2: Pick<IWinLoss, 'id'> | null): boolean {
    return o1 && o2 ? this.getWinLossIdentifier(o1) === this.getWinLossIdentifier(o2) : o1 === o2;
  }

  addWinLossToCollectionIfMissing<Type extends Pick<IWinLoss, 'id'>>(
    winLossCollection: Type[],
    ...winLossesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const winLosses: Type[] = winLossesToCheck.filter(isPresent);
    if (winLosses.length > 0) {
      const winLossCollectionIdentifiers = winLossCollection.map(winLossItem => this.getWinLossIdentifier(winLossItem)!);
      const winLossesToAdd = winLosses.filter(winLossItem => {
        const winLossIdentifier = this.getWinLossIdentifier(winLossItem);
        if (winLossCollectionIdentifiers.includes(winLossIdentifier)) {
          return false;
        }
        winLossCollectionIdentifiers.push(winLossIdentifier);
        return true;
      });
      return [...winLossesToAdd, ...winLossCollection];
    }
    return winLossCollection;
  }
}
