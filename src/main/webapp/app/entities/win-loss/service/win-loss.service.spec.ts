import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWinLoss } from '../win-loss.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../win-loss.test-samples';

import { WinLossService } from './win-loss.service';

const requireRestSample: IWinLoss = {
  ...sampleWithRequiredData,
};

describe('WinLoss Service', () => {
  let service: WinLossService;
  let httpMock: HttpTestingController;
  let expectedResult: IWinLoss | IWinLoss[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WinLossService);
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

    it('should create a WinLoss', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const winLoss = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(winLoss).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WinLoss', () => {
      const winLoss = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(winLoss).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WinLoss', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WinLoss', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WinLoss', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWinLossToCollectionIfMissing', () => {
      it('should add a WinLoss to an empty array', () => {
        const winLoss: IWinLoss = sampleWithRequiredData;
        expectedResult = service.addWinLossToCollectionIfMissing([], winLoss);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(winLoss);
      });

      it('should not add a WinLoss to an array that contains it', () => {
        const winLoss: IWinLoss = sampleWithRequiredData;
        const winLossCollection: IWinLoss[] = [
          {
            ...winLoss,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWinLossToCollectionIfMissing(winLossCollection, winLoss);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WinLoss to an array that doesn't contain it", () => {
        const winLoss: IWinLoss = sampleWithRequiredData;
        const winLossCollection: IWinLoss[] = [sampleWithPartialData];
        expectedResult = service.addWinLossToCollectionIfMissing(winLossCollection, winLoss);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(winLoss);
      });

      it('should add only unique WinLoss to an array', () => {
        const winLossArray: IWinLoss[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const winLossCollection: IWinLoss[] = [sampleWithRequiredData];
        expectedResult = service.addWinLossToCollectionIfMissing(winLossCollection, ...winLossArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const winLoss: IWinLoss = sampleWithRequiredData;
        const winLoss2: IWinLoss = sampleWithPartialData;
        expectedResult = service.addWinLossToCollectionIfMissing([], winLoss, winLoss2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(winLoss);
        expect(expectedResult).toContain(winLoss2);
      });

      it('should accept null and undefined values', () => {
        const winLoss: IWinLoss = sampleWithRequiredData;
        expectedResult = service.addWinLossToCollectionIfMissing([], null, winLoss, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(winLoss);
      });

      it('should return initial array if no WinLoss is added', () => {
        const winLossCollection: IWinLoss[] = [sampleWithRequiredData];
        expectedResult = service.addWinLossToCollectionIfMissing(winLossCollection, undefined, null);
        expect(expectedResult).toEqual(winLossCollection);
      });
    });

    describe('compareWinLoss', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWinLoss(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWinLoss(entity1, entity2);
        const compareResult2 = service.compareWinLoss(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWinLoss(entity1, entity2);
        const compareResult2 = service.compareWinLoss(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWinLoss(entity1, entity2);
        const compareResult2 = service.compareWinLoss(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
