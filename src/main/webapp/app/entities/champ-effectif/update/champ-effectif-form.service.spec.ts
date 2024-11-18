import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../champ-effectif.test-samples';

import { ChampEffectifFormService } from './champ-effectif-form.service';

describe('ChampEffectif Form Service', () => {
  let service: ChampEffectifFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChampEffectifFormService);
  });

  describe('Service methods', () => {
    describe('createChampEffectifFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChampEffectifFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            valeur: expect.any(Object),
            effectif: expect.any(Object),
          }),
        );
      });

      it('passing IChampEffectif should create a new form with FormGroup', () => {
        const formGroup = service.createChampEffectifFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            valeur: expect.any(Object),
            effectif: expect.any(Object),
          }),
        );
      });
    });

    describe('getChampEffectif', () => {
      it('should return NewChampEffectif for default ChampEffectif initial value', () => {
        const formGroup = service.createChampEffectifFormGroup(sampleWithNewData);

        const champEffectif = service.getChampEffectif(formGroup) as any;

        expect(champEffectif).toMatchObject(sampleWithNewData);
      });

      it('should return NewChampEffectif for empty ChampEffectif initial value', () => {
        const formGroup = service.createChampEffectifFormGroup();

        const champEffectif = service.getChampEffectif(formGroup) as any;

        expect(champEffectif).toMatchObject({});
      });

      it('should return IChampEffectif', () => {
        const formGroup = service.createChampEffectifFormGroup(sampleWithRequiredData);

        const champEffectif = service.getChampEffectif(formGroup) as any;

        expect(champEffectif).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChampEffectif should not enable id FormControl', () => {
        const formGroup = service.createChampEffectifFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChampEffectif should disable id FormControl', () => {
        const formGroup = service.createChampEffectifFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
