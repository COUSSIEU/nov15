import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMateriel } from '../materiel.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../materiel.test-samples';

import { MaterielService } from './materiel.service';

const requireRestSample: IMateriel = {
  ...sampleWithRequiredData,
};

describe('Materiel Service', () => {
  let service: MaterielService;
  let httpMock: HttpTestingController;
  let expectedResult: IMateriel | IMateriel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaterielService);
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

    it('should create a Materiel', () => {
      const materiel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materiel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Materiel', () => {
      const materiel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materiel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Materiel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Materiel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Materiel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterielToCollectionIfMissing', () => {
      it('should add a Materiel to an empty array', () => {
        const materiel: IMateriel = sampleWithRequiredData;
        expectedResult = service.addMaterielToCollectionIfMissing([], materiel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materiel);
      });

      it('should not add a Materiel to an array that contains it', () => {
        const materiel: IMateriel = sampleWithRequiredData;
        const materielCollection: IMateriel[] = [
          {
            ...materiel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterielToCollectionIfMissing(materielCollection, materiel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Materiel to an array that doesn't contain it", () => {
        const materiel: IMateriel = sampleWithRequiredData;
        const materielCollection: IMateriel[] = [sampleWithPartialData];
        expectedResult = service.addMaterielToCollectionIfMissing(materielCollection, materiel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materiel);
      });

      it('should add only unique Materiel to an array', () => {
        const materielArray: IMateriel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materielCollection: IMateriel[] = [sampleWithRequiredData];
        expectedResult = service.addMaterielToCollectionIfMissing(materielCollection, ...materielArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materiel: IMateriel = sampleWithRequiredData;
        const materiel2: IMateriel = sampleWithPartialData;
        expectedResult = service.addMaterielToCollectionIfMissing([], materiel, materiel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materiel);
        expect(expectedResult).toContain(materiel2);
      });

      it('should accept null and undefined values', () => {
        const materiel: IMateriel = sampleWithRequiredData;
        expectedResult = service.addMaterielToCollectionIfMissing([], null, materiel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materiel);
      });

      it('should return initial array if no Materiel is added', () => {
        const materielCollection: IMateriel[] = [sampleWithRequiredData];
        expectedResult = service.addMaterielToCollectionIfMissing(materielCollection, undefined, null);
        expect(expectedResult).toEqual(materielCollection);
      });
    });

    describe('compareMateriel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMateriel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMateriel(entity1, entity2);
        const compareResult2 = service.compareMateriel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMateriel(entity1, entity2);
        const compareResult2 = service.compareMateriel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMateriel(entity1, entity2);
        const compareResult2 = service.compareMateriel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
