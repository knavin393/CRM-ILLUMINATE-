import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OpportunityFormService } from './opportunity-form.service';
import { OpportunityService } from '../service/opportunity.service';
import { IOpportunity } from '../opportunity.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { OpportunityUpdateComponent } from './opportunity-update.component';

describe('Opportunity Management Update Component', () => {
  let comp: OpportunityUpdateComponent;
  let fixture: ComponentFixture<OpportunityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let opportunityFormService: OpportunityFormService;
  let opportunityService: OpportunityService;
  let customerService: CustomerService;
  let employeeService: EmployeeService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OpportunityUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OpportunityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OpportunityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    opportunityFormService = TestBed.inject(OpportunityFormService);
    opportunityService = TestBed.inject(OpportunityService);
    customerService = TestBed.inject(CustomerService);
    employeeService = TestBed.inject(EmployeeService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const opportunity: IOpportunity = { id: 456 };
      const customer: ICustomer = { id: 30833 };
      opportunity.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 83232 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ opportunity });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const opportunity: IOpportunity = { id: 456 };
      const employee: IEmployee = { id: 9997 };
      opportunity.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 89395 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ opportunity });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const opportunity: IOpportunity = { id: 456 };
      const product: IProduct = { id: 58613 };
      opportunity.product = product;

      const productCollection: IProduct[] = [{ id: 61690 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ opportunity });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const opportunity: IOpportunity = { id: 456 };
      const customer: ICustomer = { id: 14946 };
      opportunity.customer = customer;
      const employee: IEmployee = { id: 14539 };
      opportunity.employee = employee;
      const product: IProduct = { id: 22084 };
      opportunity.product = product;

      activatedRoute.data = of({ opportunity });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.opportunity).toEqual(opportunity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpportunity>>();
      const opportunity = { id: 123 };
      jest.spyOn(opportunityFormService, 'getOpportunity').mockReturnValue(opportunity);
      jest.spyOn(opportunityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opportunity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opportunity }));
      saveSubject.complete();

      // THEN
      expect(opportunityFormService.getOpportunity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(opportunityService.update).toHaveBeenCalledWith(expect.objectContaining(opportunity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpportunity>>();
      const opportunity = { id: 123 };
      jest.spyOn(opportunityFormService, 'getOpportunity').mockReturnValue({ id: null });
      jest.spyOn(opportunityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opportunity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opportunity }));
      saveSubject.complete();

      // THEN
      expect(opportunityFormService.getOpportunity).toHaveBeenCalled();
      expect(opportunityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpportunity>>();
      const opportunity = { id: 123 };
      jest.spyOn(opportunityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opportunity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(opportunityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
