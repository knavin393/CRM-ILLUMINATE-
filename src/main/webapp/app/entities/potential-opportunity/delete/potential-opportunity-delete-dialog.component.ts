import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPotentialOpportunity } from '../potential-opportunity.model';
import { PotentialOpportunityService } from '../service/potential-opportunity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './potential-opportunity-delete-dialog.component.html',
})
export class PotentialOpportunityDeleteDialogComponent {
  potentialOpportunity?: IPotentialOpportunity;

  constructor(protected potentialOpportunityService: PotentialOpportunityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.potentialOpportunityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
