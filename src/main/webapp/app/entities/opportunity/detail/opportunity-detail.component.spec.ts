import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpportunityDetailComponent } from './opportunity-detail.component';

describe('Opportunity Management Detail Component', () => {
  let comp: OpportunityDetailComponent;
  let fixture: ComponentFixture<OpportunityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OpportunityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ opportunity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OpportunityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OpportunityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load opportunity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.opportunity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
