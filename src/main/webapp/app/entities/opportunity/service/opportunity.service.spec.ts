import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOpportunity } from '../opportunity.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../opportunity.test-samples';

import { OpportunityService } from './opportunity.service';

const requireRestSample: IOpportunity = {
  ...sampleWithRequiredData,
};

describe('Opportunity Service', () => {
  let service: OpportunityService;
  let httpMock: HttpTestingController;
  let expectedResult: IOpportunity | IOpportunity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OpportunityService);
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

    it('should create a Opportunity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const opportunity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(opportunity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Opportunity', () => {
      const opportunity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(opportunity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Opportunity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Opportunity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Opportunity', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOpportunityToCollectionIfMissing', () => {
      it('should add a Opportunity to an empty array', () => {
        const opportunity: IOpportunity = sampleWithRequiredData;
        expectedResult = service.addOpportunityToCollectionIfMissing([], opportunity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opportunity);
      });

      it('should not add a Opportunity to an array that contains it', () => {
        const opportunity: IOpportunity = sampleWithRequiredData;
        const opportunityCollection: IOpportunity[] = [
          {
            ...opportunity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOpportunityToCollectionIfMissing(opportunityCollection, opportunity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Opportunity to an array that doesn't contain it", () => {
        const opportunity: IOpportunity = sampleWithRequiredData;
        const opportunityCollection: IOpportunity[] = [sampleWithPartialData];
        expectedResult = service.addOpportunityToCollectionIfMissing(opportunityCollection, opportunity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opportunity);
      });

      it('should add only unique Opportunity to an array', () => {
        const opportunityArray: IOpportunity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const opportunityCollection: IOpportunity[] = [sampleWithRequiredData];
        expectedResult = service.addOpportunityToCollectionIfMissing(opportunityCollection, ...opportunityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const opportunity: IOpportunity = sampleWithRequiredData;
        const opportunity2: IOpportunity = sampleWithPartialData;
        expectedResult = service.addOpportunityToCollectionIfMissing([], opportunity, opportunity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opportunity);
        expect(expectedResult).toContain(opportunity2);
      });

      it('should accept null and undefined values', () => {
        const opportunity: IOpportunity = sampleWithRequiredData;
        expectedResult = service.addOpportunityToCollectionIfMissing([], null, opportunity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opportunity);
      });

      it('should return initial array if no Opportunity is added', () => {
        const opportunityCollection: IOpportunity[] = [sampleWithRequiredData];
        expectedResult = service.addOpportunityToCollectionIfMissing(opportunityCollection, undefined, null);
        expect(expectedResult).toEqual(opportunityCollection);
      });
    });

    describe('compareOpportunity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOpportunity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOpportunity(entity1, entity2);
        const compareResult2 = service.compareOpportunity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOpportunity(entity1, entity2);
        const compareResult2 = service.compareOpportunity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOpportunity(entity1, entity2);
        const compareResult2 = service.compareOpportunity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
