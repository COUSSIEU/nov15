import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IChampEffectif } from '../champ-effectif.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../champ-effectif.test-samples';

import { ChampEffectifService } from './champ-effectif.service';

const requireRestSample: IChampEffectif = {
  ...sampleWithRequiredData,
};

describe('ChampEffectif Service', () => {
  let service: ChampEffectifService;
  let httpMock: HttpTestingController;
  let expectedResult: IChampEffectif | IChampEffectif[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ChampEffectifService);
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

    it('should create a ChampEffectif', () => {
      const champEffectif = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(champEffectif).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChampEffectif', () => {
      const champEffectif = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(champEffectif).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChampEffectif', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChampEffectif', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ChampEffectif', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addChampEffectifToCollectionIfMissing', () => {
      it('should add a ChampEffectif to an empty array', () => {
        const champEffectif: IChampEffectif = sampleWithRequiredData;
        expectedResult = service.addChampEffectifToCollectionIfMissing([], champEffectif);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(champEffectif);
      });

      it('should not add a ChampEffectif to an array that contains it', () => {
        const champEffectif: IChampEffectif = sampleWithRequiredData;
        const champEffectifCollection: IChampEffectif[] = [
          {
            ...champEffectif,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChampEffectifToCollectionIfMissing(champEffectifCollection, champEffectif);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChampEffectif to an array that doesn't contain it", () => {
        const champEffectif: IChampEffectif = sampleWithRequiredData;
        const champEffectifCollection: IChampEffectif[] = [sampleWithPartialData];
        expectedResult = service.addChampEffectifToCollectionIfMissing(champEffectifCollection, champEffectif);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(champEffectif);
      });

      it('should add only unique ChampEffectif to an array', () => {
        const champEffectifArray: IChampEffectif[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const champEffectifCollection: IChampEffectif[] = [sampleWithRequiredData];
        expectedResult = service.addChampEffectifToCollectionIfMissing(champEffectifCollection, ...champEffectifArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const champEffectif: IChampEffectif = sampleWithRequiredData;
        const champEffectif2: IChampEffectif = sampleWithPartialData;
        expectedResult = service.addChampEffectifToCollectionIfMissing([], champEffectif, champEffectif2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(champEffectif);
        expect(expectedResult).toContain(champEffectif2);
      });

      it('should accept null and undefined values', () => {
        const champEffectif: IChampEffectif = sampleWithRequiredData;
        expectedResult = service.addChampEffectifToCollectionIfMissing([], null, champEffectif, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(champEffectif);
      });

      it('should return initial array if no ChampEffectif is added', () => {
        const champEffectifCollection: IChampEffectif[] = [sampleWithRequiredData];
        expectedResult = service.addChampEffectifToCollectionIfMissing(champEffectifCollection, undefined, null);
        expect(expectedResult).toEqual(champEffectifCollection);
      });
    });

    describe('compareChampEffectif', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareChampEffectif(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareChampEffectif(entity1, entity2);
        const compareResult2 = service.compareChampEffectif(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareChampEffectif(entity1, entity2);
        const compareResult2 = service.compareChampEffectif(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareChampEffectif(entity1, entity2);
        const compareResult2 = service.compareChampEffectif(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
