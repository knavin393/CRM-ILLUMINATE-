import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILead } from '../lead.model';

@Component({
  selector: 'jhi-lead-detail',
  templateUrl: './lead-detail.component.html',
})
export class LeadDetailComponent implements OnInit {
  lead: ILead | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lead }) => {
      this.lead = lead;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
