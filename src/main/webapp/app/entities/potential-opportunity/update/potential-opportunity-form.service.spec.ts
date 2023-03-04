import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../potential-opportunity.test-samples';

import { PotentialOpportunityFormService } from './potential-opportunity-form.service';

describe('PotentialOpportunity Form Service', () => {
  let service: PotentialOpportunityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PotentialOpportunityFormService);
  });

  describe('Service methods', () => {
    describe('createPotentialOpportunityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPotentialOpportunityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            followUp: expect.any(Object),
            status: expect.any(Object),
            customer: expect.any(Object),
            employee: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });

      it('passing IPotentialOpportunity should create a new form with FormGroup', () => {
        const formGroup = service.createPotentialOpportunityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            followUp: expect.any(Object),
            status: expect.any(Object),
            customer: expect.any(Object),
            employee: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });
    });

    describe('getPotentialOpportunity', () => {
      it('should return NewPotentialOpportunity for default PotentialOpportunity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPotentialOpportunityFormGroup(sampleWithNewData);

        const potentialOpportunity = service.getPotentialOpportunity(formGroup) as any;

        expect(potentialOpportunity).toMatchObject(sampleWithNewData);
      });

      it('should return NewPotentialOpportunity for empty PotentialOpportunity initial value', () => {
        const formGroup = service.createPotentialOpportunityFormGroup();

        const potentialOpportunity = service.getPotentialOpportunity(formGroup) as any;

        expect(potentialOpportunity).toMatchObject({});
      });

      it('should return IPotentialOpportunity', () => {
        const formGroup = service.createPotentialOpportunityFormGroup(sampleWithRequiredData);

        const potentialOpportunity = service.getPotentialOpportunity(formGroup) as any;

        expect(potentialOpportunity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPotentialOpportunity should not enable id FormControl', () => {
        const formGroup = service.createPotentialOpportunityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPotentialOpportunity should disable id FormControl', () => {
        const formGroup = service.createPotentialOpportunityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
