import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPotentialOpportunity } from '../potential-opportunity.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../potential-opportunity.test-samples';

import { PotentialOpportunityService } from './potential-opportunity.service';

const requireRestSample: IPotentialOpportunity = {
  ...sampleWithRequiredData,
};

describe('PotentialOpportunity Service', () => {
  let service: PotentialOpportunityService;
  let httpMock: HttpTestingController;
  let expectedResult: IPotentialOpportunity | IPotentialOpportunity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PotentialOpportunityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PotentialOpportunity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const potentialOpportunity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(potentialOpportunity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PotentialOpportunity', () => {
      const potentialOpportunity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(potentialOpportunity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PotentialOpportunity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PotentialOpportunity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PotentialOpportunity', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPotentialOpportunityToCollectionIfMissing', () => {
      it('should add a PotentialOpportunity to an empty array', () => {
        const potentialOpportunity: IPotentialOpportunity = sampleWithRequiredData;
        expectedResult = service.addPotentialOpportunityToCollectionIfMissing([], potentialOpportunity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(potentialOpportunity);
      });

      it('should not add a PotentialOpportunity to an array that contains it', () => {
        const potentialOpportunity: IPotentialOpportunity = sampleWithRequiredData;
        const potentialOpportunityCollection: IPotentialOpportunity[] = [
          {
            ...potentialOpportunity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPotentialOpportunityToCollectionIfMissing(potentialOpportunityCollection, potentialOpportunity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PotentialOpportunity to an array that doesn't contain it", () => {
        const potentialOpportunity: IPotentialOpportunity = sampleWithRequiredData;
        const potentialOpportunityCollection: IPotentialOpportunity[] = [sampleWithPartialData];
        expectedResult = service.addPotentialOpportunityToCollectionIfMissing(potentialOpportunityCollection, potentialOpportunity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(potentialOpportunity);
      });

      it('should add only unique PotentialOpportunity to an array', () => {
        const potentialOpportunityArray: IPotentialOpportunity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const potentialOpportunityCollection: IPotentialOpportunity[] = [sampleWithRequiredData];
        expectedResult = service.addPotentialOpportunityToCollectionIfMissing(potentialOpportunityCollection, ...potentialOpportunityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const potentialOpportunity: IPotentialOpportunity = sampleWithRequiredData;
        const potentialOpportunity2: IPotentialOpportunity = sampleWithPartialData;
        expectedResult = service.addPotentialOpportunityToCollectionIfMissing([], potentialOpportunity, potentialOpportunity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(potentialOpportunity);
        expect(expectedResult).toContain(potentialOpportunity2);
      });

      it('should accept null and undefined values', () => {
        const potentialOpportunity: IPotentialOpportunity = sampleWithRequiredData;
        expectedResult = service.addPotentialOpportunityToCollectionIfMissing([], null, potentialOpportunity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(potentialOpportunity);
      });

      it('should return initial array if no PotentialOpportunity is added', () => {
        const potentialOpportunityCollection: IPotentialOpportunity[] = [sampleWithRequiredData];
        expectedResult = service.addPotentialOpportunityToCollectionIfMissing(potentialOpportunityCollection, undefined, null);
        expect(expectedResult).toEqual(potentialOpportunityCollection);
      });
    });

    describe('comparePotentialOpportunity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePotentialOpportunity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePotentialOpportunity(entity1, entity2);
        const compareResult2 = service.comparePotentialOpportunity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePotentialOpportunity(entity1, entity2);
        const compareResult2 = service.comparePotentialOpportunity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePotentialOpportunity(entity1, entity2);
        const compareResult2 = service.comparePotentialOpportunity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
