import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../win-loss.test-samples';

import { WinLossFormService } from './win-loss-form.service';

describe('WinLoss Form Service', () => {
  let service: WinLossFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WinLossFormService);
  });

  describe('Service methods', () => {
    describe('createWinLossFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWinLossFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            notes: expect.any(Object),
            opportunity: expect.any(Object),
            customer: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });

      it('passing IWinLoss should create a new form with FormGroup', () => {
        const formGroup = service.createWinLossFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            notes: expect.any(Object),
            opportunity: expect.any(Object),
            customer: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });
    });

    describe('getWinLoss', () => {
      it('should return NewWinLoss for default WinLoss initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWinLossFormGroup(sampleWithNewData);

        const winLoss = service.getWinLoss(formGroup) as any;

        expect(winLoss).toMatchObject(sampleWithNewData);
      });

      it('should return NewWinLoss for empty WinLoss initial value', () => {
        const formGroup = service.createWinLossFormGroup();

        const winLoss = service.getWinLoss(formGroup) as any;

        expect(winLoss).toMatchObject({});
      });

      it('should return IWinLoss', () => {
        const formGroup = service.createWinLossFormGroup(sampleWithRequiredData);

        const winLoss = service.getWinLoss(formGroup) as any;

        expect(winLoss).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWinLoss should not enable id FormControl', () => {
        const formGroup = service.createWinLossFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWinLoss should disable id FormControl', () => {
        const formGroup = service.createWinLossFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
