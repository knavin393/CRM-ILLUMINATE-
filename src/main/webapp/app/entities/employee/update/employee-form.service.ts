import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id'>;

type EmployeeFormGroupContent = {
  id: FormControl<IEmployee['id'] | NewEmployee['id']>;
  empName: FormControl<IEmployee['empName']>;
  empAge: FormControl<IEmployee['empAge']>;
  gender: FormControl<IEmployee['gender']>;
  empJobTitle: FormControl<IEmployee['empJobTitle']>;
  empPhone: FormControl<IEmployee['empPhone']>;
  empEmail: FormControl<IEmployee['empEmail']>;
  engage: FormControl<IEmployee['engage']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = {
      ...this.getFormDefaults(),
      ...employee,
    };
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      empName: new FormControl(employeeRawValue.empName, {
        validators: [Validators.required],
      }),
      empAge: new FormControl(employeeRawValue.empAge, {
        validators: [Validators.required, Validators.min(20), Validators.max(45)],
      }),
      gender: new FormControl(employeeRawValue.gender, {
        validators: [Validators.required],
      }),
      empJobTitle: new FormControl(employeeRawValue.empJobTitle, {
        validators: [Validators.required],
      }),
      empPhone: new FormControl(employeeRawValue.empPhone, {
        validators: [Validators.required],
      }),
      empEmail: new FormControl(employeeRawValue.empEmail, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+.com')],
      }),
      engage: new FormControl(employeeRawValue.engage),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return form.getRawValue() as IEmployee | NewEmployee;
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = { ...this.getFormDefaults(), ...employee };
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    return {
      id: null,
    };
  }
}
