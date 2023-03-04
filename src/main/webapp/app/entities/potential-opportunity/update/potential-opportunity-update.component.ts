import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PotentialOpportunityFormService, PotentialOpportunityFormGroup } from './potential-opportunity-form.service';
import { IPotentialOpportunity } from '../potential-opportunity.model';
import { PotentialOpportunityService } from '../service/potential-opportunity.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { POStatus } from 'app/entities/enumerations/po-status.model';

@Component({
  selector: 'jhi-potential-opportunity-update',
  templateUrl: './potential-opportunity-update.component.html',
})
export class PotentialOpportunityUpdateComponent implements OnInit {
  isSaving = false;
  potentialOpportunity: IPotentialOpportunity | null = null;
  pOStatusValues = Object.keys(POStatus);

  customersSharedCollection: ICustomer[] = [];
  employeesSharedCollection: IEmployee[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: PotentialOpportunityFormGroup = this.potentialOpportunityFormService.createPotentialOpportunityFormGroup();

  constructor(
    protected potentialOpportunityService: PotentialOpportunityService,
    protected potentialOpportunityFormService: PotentialOpportunityFormService,
    protected customerService: CustomerService,
    protected employeeService: EmployeeService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ potentialOpportunity }) => {
      this.potentialOpportunity = potentialOpportunity;
      if (potentialOpportunity) {
        this.updateForm(potentialOpportunity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const potentialOpportunity = this.potentialOpportunityFormService.getPotentialOpportunity(this.editForm);
    if (potentialOpportunity.id !== null) {
      this.subscribeToSaveResponse(this.potentialOpportunityService.update(potentialOpportunity));
    } else {
      this.subscribeToSaveResponse(this.potentialOpportunityService.create(potentialOpportunity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPotentialOpportunity>>): void {
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

  protected updateForm(potentialOpportunity: IPotentialOpportunity): void {
    this.potentialOpportunity = potentialOpportunity;
    this.potentialOpportunityFormService.resetForm(this.editForm, potentialOpportunity);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      potentialOpportunity.customer
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      potentialOpportunity.employee
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      potentialOpportunity.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.potentialOpportunity?.customer)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.potentialOpportunity?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.potentialOpportunity?.product)
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
