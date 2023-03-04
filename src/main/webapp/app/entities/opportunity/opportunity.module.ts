import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OpportunityComponent } from './list/opportunity.component';
import { OpportunityDetailComponent } from './detail/opportunity-detail.component';
import { OpportunityUpdateComponent } from './update/opportunity-update.component';
import { OpportunityDeleteDialogComponent } from './delete/opportunity-delete-dialog.component';
import { OpportunityRoutingModule } from './route/opportunity-routing.module';

@NgModule({
  imports: [SharedModule, OpportunityRoutingModule],
  declarations: [OpportunityComponent, OpportunityDetailComponent, OpportunityUpdateComponent, OpportunityDeleteDialogComponent],
})
export class OpportunityModule {}
