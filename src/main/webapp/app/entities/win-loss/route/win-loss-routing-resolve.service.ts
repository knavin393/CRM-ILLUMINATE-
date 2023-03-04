import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWinLoss } from '../win-loss.model';
import { WinLossService } from '../service/win-loss.service';

@Injectable({ providedIn: 'root' })
export class WinLossRoutingResolveService implements Resolve<IWinLoss | null> {
  constructor(protected service: WinLossService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWinLoss | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((winLoss: HttpResponse<IWinLoss>) => {
          if (winLoss.body) {
            return of(winLoss.body);
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
