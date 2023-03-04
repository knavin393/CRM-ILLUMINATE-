import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LeadFormService, LeadFormGroup } from './lead-form.service';
import { ILead } from '../lead.model';
import { LeadService } from '../service/lead.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { LeadStatus } from 'app/entities/enumerations/lead-status.model';

@Component({
  selector: 'jhi-lead-update',
  templateUrl: './lead-update.component.html',
})
export class LeadUpdateComponent implements OnInit {
  isSaving = false;
  lead: ILead | null = null;
  leadStatusValues = Object.keys(LeadStatus);

  customersSharedCollection: ICustomer[] = [];
  employeesSharedCollection: IEmployee[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: LeadFormGroup = this.leadFormService.createLeadFormGroup();

  constructor(
    protected leadService: LeadService,
    protected leadFormService: LeadFormService,
    protected customerService: CustomerService,
    protected employeeService: EmployeeService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lead }) => {
      this.lead = lead;
      if (lead) {
        this.updateForm(lead);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lead = this.leadFormService.getLead(this.editForm);
    if (lead.id !== null) {
      this.subscribeToSaveResponse(this.leadService.update(lead));
    } else {
      this.subscribeToSaveResponse(this.leadService.create(lead));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILead>>): void {
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

  protected updateForm(lead: ILead): void {
    this.lead = lead;
    this.leadFormService.resetForm(this.editForm, lead);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      lead.customer
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      lead.employee
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      lead.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.lead?.customer))
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.lead?.employee))
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.lead?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
