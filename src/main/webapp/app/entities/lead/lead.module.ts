import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeadComponent } from './list/lead.component';
import { LeadDetailComponent } from './detail/lead-detail.component';
import { LeadUpdateComponent } from './update/lead-update.component';
import { LeadDeleteDialogComponent } from './delete/lead-delete-dialog.component';
import { LeadRoutingModule } from './route/lead-routing.module';

@NgModule({
  imports: [SharedModule, LeadRoutingModule],
  declarations: [LeadComponent, LeadDetailComponent, LeadUpdateComponent, LeadDeleteDialogComponent],
})
export class LeadModule {}
