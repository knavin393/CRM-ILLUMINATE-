import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PotentialOpportunityFormService } from './potential-opportunity-form.service';
import { PotentialOpportunityService } from '../service/potential-opportunity.service';
import { IPotentialOpportunity } from '../potential-opportunity.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { PotentialOpportunityUpdateComponent } from './potential-opportunity-update.component';

describe('PotentialOpportunity Management Update Component', () => {
  let comp: PotentialOpportunityUpdateComponent;
  let fixture: ComponentFixture<PotentialOpportunityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let potentialOpportunityFormService: PotentialOpportunityFormService;
  let potentialOpportunityService: PotentialOpportunityService;
  let customerService: CustomerService;
  let employeeService: EmployeeService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PotentialOpportunityUpdateComponent],
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
      .overrideTemplate(PotentialOpportunityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PotentialOpportunityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    potentialOpportunityFormService = TestBed.inject(PotentialOpportunityFormService);
    potentialOpportunityService = TestBed.inject(PotentialOpportunityService);
    customerService = TestBed.inject(CustomerService);
    employeeService = TestBed.inject(EmployeeService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const potentialOpportunity: IPotentialOpportunity = { id: 456 };
      const customer: ICustomer = { id: 84500 };
      potentialOpportunity.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 39471 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ potentialOpportunity });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const potentialOpportunity: IPotentialOpportunity = { id: 456 };
      const employee: IEmployee = { id: 51032 };
      potentialOpportunity.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 80206 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ potentialOpportunity });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const potentialOpportunity: IPotentialOpportunity = { id: 456 };
      const product: IProduct = { id: 41934 };
      potentialOpportunity.product = product;

      const productCollection: IProduct[] = [{ id: 10075 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ potentialOpportunity });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const potentialOpportunity: IPotentialOpportunity = { id: 456 };
      const customer: ICustomer = { id: 47084 };
      potentialOpportunity.customer = customer;
      const employee: IEmployee = { id: 66390 };
      potentialOpportunity.employee = employee;
      const product: IProduct = { id: 87246 };
      potentialOpportunity.product = product;

      activatedRoute.data = of({ potentialOpportunity });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.potentialOpportunity).toEqual(potentialOpportunity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPotentialOpportunity>>();
      const potentialOpportunity = { id: 123 };
      jest.spyOn(potentialOpportunityFormService, 'getPotentialOpportunity').mockReturnValue(potentialOpportunity);
      jest.spyOn(potentialOpportunityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ potentialOpportunity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: potentialOpportunity }));
      saveSubject.complete();

      // THEN
      expect(potentialOpportunityFormService.getPotentialOpportunity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(potentialOpportunityService.update).toHaveBeenCalledWith(expect.objectContaining(potentialOpportunity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPotentialOpportunity>>();
      const potentialOpportunity = { id: 123 };
      jest.spyOn(potentialOpportunityFormService, 'getPotentialOpportunity').mockReturnValue({ id: null });
      jest.spyOn(potentialOpportunityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ potentialOpportunity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: potentialOpportunity }));
      saveSubject.complete();

      // THEN
      expect(potentialOpportunityFormService.getPotentialOpportunity).toHaveBeenCalled();
      expect(potentialOpportunityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPotentialOpportunity>>();
      const potentialOpportunity = { id: 123 };
      jest.spyOn(potentialOpportunityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ potentialOpportunity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(potentialOpportunityService.update).toHaveBeenCalled();
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
