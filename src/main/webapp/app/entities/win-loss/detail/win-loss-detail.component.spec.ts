import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WinLossDetailComponent } from './win-loss-detail.component';

describe('WinLoss Management Detail Component', () => {
  let comp: WinLossDetailComponent;
  let fixture: ComponentFixture<WinLossDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WinLossDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ winLoss: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WinLossDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WinLossDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load winLoss on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.winLoss).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
