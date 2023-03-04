import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { WinLossFormService, WinLossFormGroup } from './win-loss-form.service';
import { IWinLoss } from '../win-loss.model';
import { WinLossService } from '../service/win-loss.service';
import { IOpportunity } from 'app/entities/opportunity/opportunity.model';
import { OpportunityService } from 'app/entities/opportunity/service/opportunity.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-win-loss-update',
  templateUrl: './win-loss-update.component.html',
})
export class WinLossUpdateComponent implements OnInit {
  isSaving = false;
  winLoss: IWinLoss | null = null;

  opportunitiesSharedCollection: IOpportunity[] = [];
  customersSharedCollection: ICustomer[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: WinLossFormGroup = this.winLossFormService.createWinLossFormGroup();

  constructor(
    protected winLossService: WinLossService,
    protected winLossFormService: WinLossFormService,
    protected opportunityService: OpportunityService,
    protected customerService: CustomerService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOpportunity = (o1: IOpportunity | null, o2: IOpportunity | null): boolean => this.opportunityService.compareOpportunity(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ winLoss }) => {
      this.winLoss = winLoss;
      if (winLoss) {
        this.updateForm(winLoss);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const winLoss = this.winLossFormService.getWinLoss(this.editForm);
    if (winLoss.id !== null) {
      this.subscribeToSaveResponse(this.winLossService.update(winLoss));
    } else {
      this.subscribeToSaveResponse(this.winLossService.create(winLoss));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWinLoss>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(winLoss: IWinLoss): void {
    this.winLoss = winLoss;
    this.winLossFormService.resetForm(this.editForm, winLoss);

    this.opportunitiesSharedCollection = this.opportunityService.addOpportunityToCollectionIfMissing<IOpportunity>(
      this.opportunitiesSharedCollection,
      winLoss.opportunity
    );
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      winLoss.customer
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      winLoss.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.opportunityService
      .query()
      .pipe(map((res: HttpResponse<IOpportunity[]>) => res.body ?? []))
      .pipe(
        map((opportunities: IOpportunity[]) =>
          this.opportunityService.addOpportunityToCollectionIfMissing<IOpportunity>(opportunities, this.winLoss?.opportunity)
        )
      )
      .subscribe((opportunities: IOpportunity[]) => (this.opportunitiesSharedCollection = opportunities));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.winLoss?.customer))
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.winLoss?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
