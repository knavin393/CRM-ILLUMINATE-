import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPotentialOpportunity } from '../potential-opportunity.model';
import { PotentialOpportunityService } from '../service/potential-opportunity.service';

import { PotentialOpportunityRoutingResolveService } from './potential-opportunity-routing-resolve.service';

describe('PotentialOpportunity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PotentialOpportunityRoutingResolveService;
  let service: PotentialOpportunityService;
  let resultPotentialOpportunity: IPotentialOpportunity | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PotentialOpportunityRoutingResolveService);
    service = TestBed.inject(PotentialOpportunityService);
    resultPotentialOpportunity = undefined;
  });

  describe('resolve', () => {
    it('should return IPotentialOpportunity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPotentialOpportunity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPotentialOpportunity).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPotentialOpportunity = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPotentialOpportunity).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPotentialOpportunity>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPotentialOpportunity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPotentialOpportunity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
