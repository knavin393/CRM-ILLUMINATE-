import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductOrder, NewProductOrder } from '../product-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductOrder for edit and NewProductOrderFormGroupInput for create.
 */
type ProductOrderFormGroupInput = IProductOrder | PartialWithRequiredKeyOf<NewProductOrder>;

type ProductOrderFormDefaults = Pick<NewProductOrder, 'id'>;

type ProductOrderFormGroupContent = {
  id: FormControl<IProductOrder['id'] | NewProductOrder['id']>;
  placedDate: FormControl<IProductOrder['placedDate']>;
  status: FormControl<IProductOrder['status']>;
  invoiceId: FormControl<IProductOrder['invoiceId']>;
  quantity: FormControl<IProductOrder['quantity']>;
  totalPrice: FormControl<IProductOrder['totalPrice']>;
  product: FormControl<IProductOrder['product']>;
  customer: FormControl<IProductOrder['customer']>;
};

export type ProductOrderFormGroup = FormGroup<ProductOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductOrderFormService {
  createProductOrderFormGroup(productOrder: ProductOrderFormGroupInput = { id: null }): ProductOrderFormGroup {
    const productOrderRawValue = {
      ...this.getFormDefaults(),
      ...productOrder,
    };
    return new FormGroup<ProductOrderFormGroupContent>({
      id: new FormControl(
        { value: productOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      placedDate: new FormControl(productOrderRawValue.placedDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(productOrderRawValue.status, {
        validators: [Validators.required],
      }),
      invoiceId: new FormControl(productOrderRawValue.invoiceId),
      quantity: new FormControl(productOrderRawValue.quantity, {
        validators: [Validators.required, Validators.min(0)],
      }),
      totalPrice: new FormControl(productOrderRawValue.totalPrice, {
        validators: [Validators.required, Validators.min(0)],
      }),
      product: new FormControl(productOrderRawValue.product, {
        validators: [Validators.required],
      }),
      customer: new FormControl(productOrderRawValue.customer, {
        validators: [Validators.required],
      }),
    });
  }

  getProductOrder(form: ProductOrderFormGroup): IProductOrder | NewProductOrder {
    return form.getRawValue() as IProductOrder | NewProductOrder;
  }

  resetForm(form: ProductOrderFormGroup, productOrder: ProductOrderFormGroupInput): void {
    const productOrderRawValue = { ...this.getFormDefaults(), ...productOrder };
    form.reset(
      {
        ...productOrderRawValue,
        id: { value: productOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductOrderFormDefaults {
    return {
      id: null,
    };
  }
}
