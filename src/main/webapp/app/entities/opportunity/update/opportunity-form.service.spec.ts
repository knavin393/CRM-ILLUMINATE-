import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../opportunity.test-samples';

import { OpportunityFormService } from './opportunity-form.service';

describe('Opportunity Form Service', () => {
  let service: OpportunityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OpportunityFormService);
  });

  describe('Service methods', () => {
    describe('createOpportunityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOpportunityFormGroup();

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

      it('passing IOpportunity should create a new form with FormGroup', () => {
        const formGroup = service.createOpportunityFormGroup(sampleWithRequiredData);

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

    describe('getOpportunity', () => {
      it('should return NewOpportunity for default Opportunity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOpportunityFormGroup(sampleWithNewData);

        const opportunity = service.getOpportunity(formGroup) as any;

        expect(opportunity).toMatchObject(sampleWithNewData);
      });

      it('should return NewOpportunity for empty Opportunity initial value', () => {
        const formGroup = service.createOpportunityFormGroup();

        const opportunity = service.getOpportunity(formGroup) as any;

        expect(opportunity).toMatchObject({});
      });

      it('should return IOpportunity', () => {
        const formGroup = service.createOpportunityFormGroup(sampleWithRequiredData);

        const opportunity = service.getOpportunity(formGroup) as any;

        expect(opportunity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOpportunity should not enable id FormControl', () => {
        const formGroup = service.createOpportunityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOpportunity should disable id FormControl', () => {
        const formGroup = service.createOpportunityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
