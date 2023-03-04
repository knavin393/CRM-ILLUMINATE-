import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PotentialOpportunityComponent } from './list/potential-opportunity.component';
import { PotentialOpportunityDetailComponent } from './detail/potential-opportunity-detail.component';
import { PotentialOpportunityUpdateComponent } from './update/potential-opportunity-update.component';
import { PotentialOpportunityDeleteDialogComponent } from './delete/potential-opportunity-delete-dialog.component';
import { PotentialOpportunityRoutingModule } from './route/potential-opportunity-routing.module';

@NgModule({
  imports: [SharedModule, PotentialOpportunityRoutingModule],
  declarations: [
    PotentialOpportunityComponent,
    PotentialOpportunityDetailComponent,
    PotentialOpportunityUpdateComponent,
    PotentialOpportunityDeleteDialogComponent,
  ],
})
export class PotentialOpportunityModule {}
