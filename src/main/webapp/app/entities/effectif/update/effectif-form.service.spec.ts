import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../effectif.test-samples';

import { EffectifFormService } from './effectif-form.service';

describe('Effectif Form Service', () => {
  let service: EffectifFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EffectifFormService);
  });

  describe('Service methods', () => {
    describe('createEffectifFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEffectifFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cumul: expect.any(Object),
          }),
        );
      });

      it('passing IEffectif should create a new form with FormGroup', () => {
        const formGroup = service.createEffectifFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cumul: expect.any(Object),
          }),
        );
      });
    });

    describe('getEffectif', () => {
      it('should return NewEffectif for default Effectif initial value', () => {
        const formGroup = service.createEffectifFormGroup(sampleWithNewData);

        const effectif = service.getEffectif(formGroup) as any;

        expect(effectif).toMatchObject(sampleWithNewData);
      });

      it('should return NewEffectif for empty Effectif initial value', () => {
        const formGroup = service.createEffectifFormGroup();

        const effectif = service.getEffectif(formGroup) as any;

        expect(effectif).toMatchObject({});
      });

      it('should return IEffectif', () => {
        const formGroup = service.createEffectifFormGroup(sampleWithRequiredData);

        const effectif = service.getEffectif(formGroup) as any;

        expect(effectif).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEffectif should not enable id FormControl', () => {
        const formGroup = service.createEffectifFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEffectif should disable id FormControl', () => {
        const formGroup = service.createEffectifFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
