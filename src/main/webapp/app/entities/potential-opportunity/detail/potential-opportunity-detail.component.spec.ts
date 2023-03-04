import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PotentialOpportunityDetailComponent } from './potential-opportunity-detail.component';

describe('PotentialOpportunity Management Detail Component', () => {
  let comp: PotentialOpportunityDetailComponent;
  let fixture: ComponentFixture<PotentialOpportunityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PotentialOpportunityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ potentialOpportunity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PotentialOpportunityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PotentialOpportunityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load potentialOpportunity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.potentialOpportunity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
