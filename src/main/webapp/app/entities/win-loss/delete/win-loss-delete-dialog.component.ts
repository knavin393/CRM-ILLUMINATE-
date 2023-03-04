import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWinLoss } from '../win-loss.model';
import { WinLossService } from '../service/win-loss.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './win-loss-delete-dialog.component.html',
})
export class WinLossDeleteDialogComponent {
  winLoss?: IWinLoss;

  constructor(protected winLossService: WinLossService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.winLossService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
