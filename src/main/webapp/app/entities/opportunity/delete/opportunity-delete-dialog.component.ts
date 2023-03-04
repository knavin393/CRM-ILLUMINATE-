import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOpportunity } from '../opportunity.model';
import { OpportunityService } from '../service/opportunity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './opportunity-delete-dialog.component.html',
})
export class OpportunityDeleteDialogComponent {
  opportunity?: IOpportunity;

  constructor(protected opportunityService: OpportunityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.opportunityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
