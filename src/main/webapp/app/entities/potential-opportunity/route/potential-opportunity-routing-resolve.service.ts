import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPotentialOpportunity } from '../potential-opportunity.model';
import { PotentialOpportunityService } from '../service/potential-opportunity.service';

@Injectable({ providedIn: 'root' })
export class PotentialOpportunityRoutingResolveService implements Resolve<IPotentialOpportunity | null> {
  constructor(protected service: PotentialOpportunityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPotentialOpportunity | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((potentialOpportunity: HttpResponse<IPotentialOpportunity>) => {
          if (potentialOpportunity.body) {
            return of(potentialOpportunity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
