import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  custName: FormControl<ICustomer['custName']>;
  gender: FormControl<ICustomer['gender']>;
  custIC: FormControl<ICustomer['custIC']>;
  custEmail: FormControl<ICustomer['custEmail']>;
  custPhone: FormControl<ICustomer['custPhone']>;
  companyName: FormControl<ICustomer['companyName']>;
  custJobTitle: FormControl<ICustomer['custJobTitle']>;
  engage: FormControl<ICustomer['engage']>;
  company: FormControl<ICustomer['company']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      custName: new FormControl(customerRawValue.custName, {
        validators: [Validators.required],
      }),
      gender: new FormControl(customerRawValue.gender, {
        validators: [Validators.required],
      }),
      custIC: new FormControl(customerRawValue.custIC, {
        validators: [Validators.required],
      }),
      custEmail: new FormControl(customerRawValue.custEmail, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+.com')],
      }),
      custPhone: new FormControl(customerRawValue.custPhone, {
        validators: [Validators.required],
      }),
      companyName: new FormControl(customerRawValue.companyName, {
        validators: [Validators.required],
      }),
      custJobTitle: new FormControl(customerRawValue.custJobTitle, {
        validators: [Validators.required],
      }),
      engage: new FormControl(customerRawValue.engage),
      company: new FormControl(customerRawValue.company),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
    };
  }
}
