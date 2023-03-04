import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOpportunity } from '../opportunity.model';

@Component({
  selector: 'jhi-opportunity-detail',
  templateUrl: './opportunity-detail.component.html',
})
export class OpportunityDetailComponent implements OnInit {
  opportunity: IOpportunity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opportunity }) => {
      this.opportunity = opportunity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
