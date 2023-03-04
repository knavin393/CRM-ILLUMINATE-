import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWinLoss } from '../win-loss.model';

@Component({
  selector: 'jhi-win-loss-detail',
  templateUrl: './win-loss-detail.component.html',
})
export class WinLossDetailComponent implements OnInit {
  winLoss: IWinLoss | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ winLoss }) => {
      this.winLoss = winLoss;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
