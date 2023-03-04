import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OpportunityComponent } from '../list/opportunity.component';
import { OpportunityDetailComponent } from '../detail/opportunity-detail.component';
import { OpportunityUpdateComponent } from '../update/opportunity-update.component';
import { OpportunityRoutingResolveService } from './opportunity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const opportunityRoute: Routes = [
  {
    path: '',
    component: OpportunityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OpportunityDetailComponent,
    resolve: {
      opportunity: OpportunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OpportunityUpdateComponent,
    resolve: {
      opportunity: OpportunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OpportunityUpdateComponent,
    resolve: {
      opportunity: OpportunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(opportunityRoute)],
  exports: [RouterModule],
})
export class OpportunityRoutingModule {}
