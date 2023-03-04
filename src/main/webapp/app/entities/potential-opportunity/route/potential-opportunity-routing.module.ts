import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PotentialOpportunityComponent } from '../list/potential-opportunity.component';
import { PotentialOpportunityDetailComponent } from '../detail/potential-opportunity-detail.component';
import { PotentialOpportunityUpdateComponent } from '../update/potential-opportunity-update.component';
import { PotentialOpportunityRoutingResolveService } from './potential-opportunity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const potentialOpportunityRoute: Routes = [
  {
    path: '',
    component: PotentialOpportunityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PotentialOpportunityDetailComponent,
    resolve: {
      potentialOpportunity: PotentialOpportunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PotentialOpportunityUpdateComponent,
    resolve: {
      potentialOpportunity: PotentialOpportunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PotentialOpportunityUpdateComponent,
    resolve: {
      potentialOpportunity: PotentialOpportunityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(potentialOpportunityRoute)],
  exports: [RouterModule],
})
export class PotentialOpportunityRoutingModule {}
