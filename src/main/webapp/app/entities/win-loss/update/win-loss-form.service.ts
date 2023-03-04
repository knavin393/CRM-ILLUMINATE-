import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWinLoss, NewWinLoss } from '../win-loss.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWinLoss for edit and NewWinLossFormGroupInput for create.
 */
type WinLossFormGroupInput = IWinLoss | PartialWithRequiredKeyOf<NewWinLoss>;

type WinLossFormDefaults = Pick<NewWinLoss, 'id'>;

type WinLossFormGroupContent = {
  id: FormControl<IWinLoss['id'] | NewWinLoss['id']>;
  notes: FormControl<IWinLoss['notes']>;
  opportunity: FormControl<IWinLoss['opportunity']>;
  customer: FormControl<IWinLoss['customer']>;
  product: FormControl<IWinLoss['product']>;
};

export type WinLossFormGroup = FormGroup<WinLossFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WinLossFormService {
  createWinLossFormGroup(winLoss: WinLossFormGroupInput = { id: null }): WinLossFormGroup {
    const winLossRawValue = {
      ...this.getFormDefaults(),
      ...winLoss,
    };
    return new FormGroup<WinLossFormGroupContent>({
      id: new FormControl(
        { value: winLossRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      notes: new FormControl(winLossRawValue.notes),
      opportunity: new FormControl(winLossRawValue.opportunity, {
        validators: [Validators.required],
      }),
      customer: new FormControl(winLossRawValue.customer, {
        validators: [Validators.required],
      }),
      product: new FormControl(winLossRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getWinLoss(form: WinLossFormGroup): IWinLoss | NewWinLoss {
    return form.getRawValue() as IWinLoss | NewWinLoss;
  }

  resetForm(form: WinLossFormGroup, winLoss: WinLossFormGroupInput): void {
    const winLossRawValue = { ...this.getFormDefaults(), ...winLoss };
    form.reset(
      {
        ...winLossRawValue,
        id: { value: winLossRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WinLossFormDefaults {
    return {
      id: null,
    };
  }
}
