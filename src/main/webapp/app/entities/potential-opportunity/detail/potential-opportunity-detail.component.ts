import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPotentialOpportunity } from '../potential-opportunity.model';

@Component({
  selector: 'jhi-potential-opportunity-detail',
  templateUrl: './potential-opportunity-detail.component.html',
})
export class PotentialOpportunityDetailComponent implements OnInit {
  potentialOpportunity: IPotentialOpportunity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ potentialOpportunity }) => {
      this.potentialOpportunity = potentialOpportunity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
