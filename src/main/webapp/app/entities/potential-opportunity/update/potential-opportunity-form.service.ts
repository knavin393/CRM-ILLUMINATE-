import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPotentialOpportunity, NewPotentialOpportunity } from '../potential-opportunity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPotentialOpportunity for edit and NewPotentialOpportunityFormGroupInput for create.
 */
type PotentialOpportunityFormGroupInput = IPotentialOpportunity | PartialWithRequiredKeyOf<NewPotentialOpportunity>;

type PotentialOpportunityFormDefaults = Pick<NewPotentialOpportunity, 'id'>;

type PotentialOpportunityFormGroupContent = {
  id: FormControl<IPotentialOpportunity['id'] | NewPotentialOpportunity['id']>;
  followUp: FormControl<IPotentialOpportunity['followUp']>;
  status: FormControl<IPotentialOpportunity['status']>;
  customer: FormControl<IPotentialOpportunity['customer']>;
  employee: FormControl<IPotentialOpportunity['employee']>;
  product: FormControl<IPotentialOpportunity['product']>;
};

export type PotentialOpportunityFormGroup = FormGroup<PotentialOpportunityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PotentialOpportunityFormService {
  createPotentialOpportunityFormGroup(
    potentialOpportunity: PotentialOpportunityFormGroupInput = { id: null }
  ): PotentialOpportunityFormGroup {
    const potentialOpportunityRawValue = {
      ...this.getFormDefaults(),
      ...potentialOpportunity,
    };
    return new FormGroup<PotentialOpportunityFormGroupContent>({
      id: new FormControl(
        { value: potentialOpportunityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      followUp: new FormControl(potentialOpportunityRawValue.followUp),
      status: new FormControl(potentialOpportunityRawValue.status, {
        validators: [Validators.required],
      }),
      customer: new FormControl(potentialOpportunityRawValue.customer, {
        validators: [Validators.required],
      }),
      employee: new FormControl(potentialOpportunityRawValue.employee, {
        validators: [Validators.required],
      }),
      product: new FormControl(potentialOpportunityRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getPotentialOpportunity(form: PotentialOpportunityFormGroup): IPotentialOpportunity | NewPotentialOpportunity {
    return form.getRawValue() as IPotentialOpportunity | NewPotentialOpportunity;
  }

  resetForm(form: PotentialOpportunityFormGroup, potentialOpportunity: PotentialOpportunityFormGroupInput): void {
    const potentialOpportunityRawValue = { ...this.getFormDefaults(), ...potentialOpportunity };
    form.reset(
      {
        ...potentialOpportunityRawValue,
        id: { value: potentialOpportunityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PotentialOpportunityFormDefaults {
    return {
      id: null,
    };
  }
}
