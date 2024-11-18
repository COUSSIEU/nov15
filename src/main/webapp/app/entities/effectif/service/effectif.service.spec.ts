import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEffectif } from '../effectif.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../effectif.test-samples';

import { EffectifService } from './effectif.service';

const requireRestSample: IEffectif = {
  ...sampleWithRequiredData,
};

describe('Effectif Service', () => {
  let service: EffectifService;
  let httpMock: HttpTestingController;
  let expectedResult: IEffectif | IEffectif[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EffectifService);
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

    it('should create a Effectif', () => {
      const effectif = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(effectif).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Effectif', () => {
      const effectif = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(effectif).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Effectif', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Effectif', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Effectif', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEffectifToCollectionIfMissing', () => {
      it('should add a Effectif to an empty array', () => {
        const effectif: IEffectif = sampleWithRequiredData;
        expectedResult = service.addEffectifToCollectionIfMissing([], effectif);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(effectif);
      });

      it('should not add a Effectif to an array that contains it', () => {
        const effectif: IEffectif = sampleWithRequiredData;
        const effectifCollection: IEffectif[] = [
          {
            ...effectif,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEffectifToCollectionIfMissing(effectifCollection, effectif);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Effectif to an array that doesn't contain it", () => {
        const effectif: IEffectif = sampleWithRequiredData;
        const effectifCollection: IEffectif[] = [sampleWithPartialData];
        expectedResult = service.addEffectifToCollectionIfMissing(effectifCollection, effectif);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(effectif);
      });

      it('should add only unique Effectif to an array', () => {
        const effectifArray: IEffectif[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const effectifCollection: IEffectif[] = [sampleWithRequiredData];
        expectedResult = service.addEffectifToCollectionIfMissing(effectifCollection, ...effectifArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const effectif: IEffectif = sampleWithRequiredData;
        const effectif2: IEffectif = sampleWithPartialData;
        expectedResult = service.addEffectifToCollectionIfMissing([], effectif, effectif2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(effectif);
        expect(expectedResult).toContain(effectif2);
      });

      it('should accept null and undefined values', () => {
        const effectif: IEffectif = sampleWithRequiredData;
        expectedResult = service.addEffectifToCollectionIfMissing([], null, effectif, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(effectif);
      });

      it('should return initial array if no Effectif is added', () => {
        const effectifCollection: IEffectif[] = [sampleWithRequiredData];
        expectedResult = service.addEffectifToCollectionIfMissing(effectifCollection, undefined, null);
        expect(expectedResult).toEqual(effectifCollection);
      });
    });

    describe('compareEffectif', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEffectif(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEffectif(entity1, entity2);
        const compareResult2 = service.compareEffectif(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEffectif(entity1, entity2);
        const compareResult2 = service.compareEffectif(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEffectif(entity1, entity2);
        const compareResult2 = service.compareEffectif(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
