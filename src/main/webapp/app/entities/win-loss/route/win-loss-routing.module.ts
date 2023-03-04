import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WinLossComponent } from '../list/win-loss.component';
import { WinLossDetailComponent } from '../detail/win-loss-detail.component';
import { WinLossUpdateComponent } from '../update/win-loss-update.component';
import { WinLossRoutingResolveService } from './win-loss-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const winLossRoute: Routes = [
  {
    path: '',
    component: WinLossComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WinLossDetailComponent,
    resolve: {
      winLoss: WinLossRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WinLossUpdateComponent,
    resolve: {
      winLoss: WinLossRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WinLossUpdateComponent,
    resolve: {
      winLoss: WinLossRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(winLossRoute)],
  exports: [RouterModule],
})
export class WinLossRoutingModule {}
