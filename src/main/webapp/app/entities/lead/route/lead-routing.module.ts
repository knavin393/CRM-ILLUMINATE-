import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeadComponent } from '../list/lead.component';
import { LeadDetailComponent } from '../detail/lead-detail.component';
import { LeadUpdateComponent } from '../update/lead-update.component';
import { LeadRoutingResolveService } from './lead-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const leadRoute: Routes = [
  {
    path: '',
    component: LeadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeadDetailComponent,
    resolve: {
      lead: LeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeadUpdateComponent,
    resolve: {
      lead: LeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeadUpdateComponent,
    resolve: {
      lead: LeadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leadRoute)],
  exports: [RouterModule],
})
export class LeadRoutingModule {}
