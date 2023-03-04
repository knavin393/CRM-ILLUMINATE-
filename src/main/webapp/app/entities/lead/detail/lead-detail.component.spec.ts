import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeadDetailComponent } from './lead-detail.component';

describe('Lead Management Detail Component', () => {
  let comp: LeadDetailComponent;
  let fixture: ComponentFixture<LeadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lead: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lead on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lead).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
