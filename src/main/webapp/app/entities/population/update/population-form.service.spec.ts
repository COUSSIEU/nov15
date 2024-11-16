import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../population.test-samples';

import { PopulationFormService } from './population-form.service';

describe('Population Form Service', () => {
  let service: PopulationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PopulationFormService);
  });

  describe('Service methods', () => {
    describe('createPopulationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPopulationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tableName: expect.any(Object),
            attributNames: expect.any(Object),
            typeNames: expect.any(Object),
          }),
        );
      });

      it('passing IPopulation should create a new form with FormGroup', () => {
        const formGroup = service.createPopulationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tableName: expect.any(Object),
            attributNames: expect.any(Object),
            typeNames: expect.any(Object),
          }),
        );
      });
    });

    describe('getPopulation', () => {
      it('should return NewPopulation for default Population initial value', () => {
        const formGroup = service.createPopulationFormGroup(sampleWithNewData);

        const population = service.getPopulation(formGroup) as any;

        expect(population).toMatchObject(sampleWithNewData);
      });

      it('should return NewPopulation for empty Population initial value', () => {
        const formGroup = service.createPopulationFormGroup();

        const population = service.getPopulation(formGroup) as any;

        expect(population).toMatchObject({});
      });

      it('should return IPopulation', () => {
        const formGroup = service.createPopulationFormGroup(sampleWithRequiredData);

        const population = service.getPopulation(formGroup) as any;

        expect(population).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPopulation should not enable id FormControl', () => {
        const formGroup = service.createPopulationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPopulation should disable id FormControl', () => {
        const formGroup = service.createPopulationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
