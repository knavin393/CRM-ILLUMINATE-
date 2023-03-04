import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WinLossComponent } from './list/win-loss.component';
import { WinLossDetailComponent } from './detail/win-loss-detail.component';
import { WinLossUpdateComponent } from './update/win-loss-update.component';
import { WinLossDeleteDialogComponent } from './delete/win-loss-delete-dialog.component';
import { WinLossRoutingModule } from './route/win-loss-routing.module';

@NgModule({
  imports: [SharedModule, WinLossRoutingModule],
  declarations: [WinLossComponent, WinLossDetailComponent, WinLossUpdateComponent, WinLossDeleteDialogComponent],
})
export class WinLossModule {}
