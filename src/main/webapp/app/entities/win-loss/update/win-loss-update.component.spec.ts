import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WinLossFormService } from './win-loss-form.service';
import { WinLossService } from '../service/win-loss.service';
import { IWinLoss } from '../win-loss.model';
import { IOpportunity } from 'app/entities/opportunity/opportunity.model';
import { OpportunityService } from 'app/entities/opportunity/service/opportunity.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { WinLossUpdateComponent } from './win-loss-update.component';

describe('WinLoss Management Update Component', () => {
  let comp: WinLossUpdateComponent;
  let fixture: ComponentFixture<WinLossUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let winLossFormService: WinLossFormService;
  let winLossService: WinLossService;
  let opportunityService: OpportunityService;
  let customerService: CustomerService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WinLossUpdateComponent],
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
      .overrideTemplate(WinLossUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WinLossUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    winLossFormService = TestBed.inject(WinLossFormService);
    winLossService = TestBed.inject(WinLossService);
    opportunityService = TestBed.inject(OpportunityService);
    customerService = TestBed.inject(CustomerService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Opportunity query and add missing value', () => {
      const winLoss: IWinLoss = { id: 456 };
      const opportunity: IOpportunity = { id: 84064 };
      winLoss.opportunity = opportunity;

      const opportunityCollection: IOpportunity[] = [{ id: 92191 }];
      jest.spyOn(opportunityService, 'query').mockReturnValue(of(new HttpResponse({ body: opportunityCollection })));
      const additionalOpportunities = [opportunity];
      const expectedCollection: IOpportunity[] = [...additionalOpportunities, ...opportunityCollection];
      jest.spyOn(opportunityService, 'addOpportunityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ winLoss });
      comp.ngOnInit();

      expect(opportunityService.query).toHaveBeenCalled();
      expect(opportunityService.addOpportunityToCollectionIfMissing).toHaveBeenCalledWith(
        opportunityCollection,
        ...additionalOpportunities.map(expect.objectContaining)
      );
      expect(comp.opportunitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const winLoss: IWinLoss = { id: 456 };
      const customer: ICustomer = { id: 34547 };
      winLoss.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 38886 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ winLoss });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const winLoss: IWinLoss = { id: 456 };
      const product: IProduct = { id: 42521 };
      winLoss.product = product;

      const productCollection: IProduct[] = [{ id: 99656 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ winLoss });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const winLoss: IWinLoss = { id: 456 };
      const opportunity: IOpportunity = { id: 23829 };
      winLoss.opportunity = opportunity;
      const customer: ICustomer = { id: 34248 };
      winLoss.customer = customer;
      const product: IProduct = { id: 55436 };
      winLoss.product = product;

      activatedRoute.data = of({ winLoss });
      comp.ngOnInit();

      expect(comp.opportunitiesSharedCollection).toContain(opportunity);
      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.winLoss).toEqual(winLoss);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWinLoss>>();
      const winLoss = { id: 123 };
      jest.spyOn(winLossFormService, 'getWinLoss').mockReturnValue(winLoss);
      jest.spyOn(winLossService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ winLoss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: winLoss }));
      saveSubject.complete();

      // THEN
      expect(winLossFormService.getWinLoss).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(winLossService.update).toHaveBeenCalledWith(expect.objectContaining(winLoss));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWinLoss>>();
      const winLoss = { id: 123 };
      jest.spyOn(winLossFormService, 'getWinLoss').mockReturnValue({ id: null });
      jest.spyOn(winLossService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ winLoss: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: winLoss }));
      saveSubject.complete();

      // THEN
      expect(winLossFormService.getWinLoss).toHaveBeenCalled();
      expect(winLossService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWinLoss>>();
      const winLoss = { id: 123 };
      jest.spyOn(winLossService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ winLoss });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(winLossService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOpportunity', () => {
      it('Should forward to opportunityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(opportunityService, 'compareOpportunity');
        comp.compareOpportunity(entity, entity2);
        expect(opportunityService.compareOpportunity).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
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
