import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILead, NewLead } from '../lead.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILead for edit and NewLeadFormGroupInput for create.
 */
type LeadFormGroupInput = ILead | PartialWithRequiredKeyOf<NewLead>;

type LeadFormDefaults = Pick<NewLead, 'id'>;

type LeadFormGroupContent = {
  id: FormControl<ILead['id'] | NewLead['id']>;
  followUp: FormControl<ILead['followUp']>;
  status: FormControl<ILead['status']>;
  customer: FormControl<ILead['customer']>;
  employee: FormControl<ILead['employee']>;
  product: FormControl<ILead['product']>;
};

export type LeadFormGroup = FormGroup<LeadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeadFormService {
  createLeadFormGroup(lead: LeadFormGroupInput = { id: null }): LeadFormGroup {
    const leadRawValue = {
      ...this.getFormDefaults(),
      ...lead,
    };
    return new FormGroup<LeadFormGroupContent>({
      id: new FormControl(
        { value: leadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      followUp: new FormControl(leadRawValue.followUp),
      status: new FormControl(leadRawValue.status, {
        validators: [Validators.required],
      }),
      customer: new FormControl(leadRawValue.customer, {
        validators: [Validators.required],
      }),
      employee: new FormControl(leadRawValue.employee, {
        validators: [Validators.required],
      }),
      product: new FormControl(leadRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getLead(form: LeadFormGroup): ILead | NewLead {
    return form.getRawValue() as ILead | NewLead;
  }

  resetForm(form: LeadFormGroup, lead: LeadFormGroupInput): void {
    const leadRawValue = { ...this.getFormDefaults(), ...lead };
    form.reset(
      {
        ...leadRawValue,
        id: { value: leadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LeadFormDefaults {
    return {
      id: null,
    };
  }
}
