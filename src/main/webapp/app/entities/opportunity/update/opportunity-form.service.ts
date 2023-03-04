import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOpportunity, NewOpportunity } from '../opportunity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOpportunity for edit and NewOpportunityFormGroupInput for create.
 */
type OpportunityFormGroupInput = IOpportunity | PartialWithRequiredKeyOf<NewOpportunity>;

type OpportunityFormDefaults = Pick<NewOpportunity, 'id'>;

type OpportunityFormGroupContent = {
  id: FormControl<IOpportunity['id'] | NewOpportunity['id']>;
  followUp: FormControl<IOpportunity['followUp']>;
  status: FormControl<IOpportunity['status']>;
  customer: FormControl<IOpportunity['customer']>;
  employee: FormControl<IOpportunity['employee']>;
  product: FormControl<IOpportunity['product']>;
};

export type OpportunityFormGroup = FormGroup<OpportunityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OpportunityFormService {
  createOpportunityFormGroup(opportunity: OpportunityFormGroupInput = { id: null }): OpportunityFormGroup {
    const opportunityRawValue = {
      ...this.getFormDefaults(),
      ...opportunity,
    };
    return new FormGroup<OpportunityFormGroupContent>({
      id: new FormControl(
        { value: opportunityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      followUp: new FormControl(opportunityRawValue.followUp),
      status: new FormControl(opportunityRawValue.status, {
        validators: [Validators.required],
      }),
      customer: new FormControl(opportunityRawValue.customer, {
        validators: [Validators.required],
      }),
      employee: new FormControl(opportunityRawValue.employee, {
        validators: [Validators.required],
      }),
      product: new FormControl(opportunityRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getOpportunity(form: OpportunityFormGroup): IOpportunity | NewOpportunity {
    return form.getRawValue() as IOpportunity | NewOpportunity;
  }

  resetForm(form: OpportunityFormGroup, opportunity: OpportunityFormGroupInput): void {
    const opportunityRawValue = { ...this.getFormDefaults(), ...opportunity };
    form.reset(
      {
        ...opportunityRawValue,
        id: { value: opportunityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OpportunityFormDefaults {
    return {
      id: null,
    };
  }
}
