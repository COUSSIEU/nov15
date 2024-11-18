import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../materiel.test-samples';

import { MaterielFormService } from './materiel-form.service';

describe('Materiel Form Service', () => {
  let service: MaterielFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterielFormService);
  });

  describe('Service methods', () => {
    describe('createMaterielFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterielFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etat: expect.any(Object),
            release: expect.any(Object),
            modele: expect.any(Object),
            sorte: expect.any(Object),
            site: expect.any(Object),
            region: expect.any(Object),
            mission: expect.any(Object),
            entite: expect.any(Object),
          }),
        );
      });

      it('passing IMateriel should create a new form with FormGroup', () => {
        const formGroup = service.createMaterielFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etat: expect.any(Object),
            release: expect.any(Object),
            modele: expect.any(Object),
            sorte: expect.any(Object),
            site: expect.any(Object),
            region: expect.any(Object),
            mission: expect.any(Object),
            entite: expect.any(Object),
          }),
        );
      });
    });

    describe('getMateriel', () => {
      it('should return NewMateriel for default Materiel initial value', () => {
        const formGroup = service.createMaterielFormGroup(sampleWithNewData);

        const materiel = service.getMateriel(formGroup) as any;

        expect(materiel).toMatchObject(sampleWithNewData);
      });

      it('should return NewMateriel for empty Materiel initial value', () => {
        const formGroup = service.createMaterielFormGroup();

        const materiel = service.getMateriel(formGroup) as any;

        expect(materiel).toMatchObject({});
      });

      it('should return IMateriel', () => {
        const formGroup = service.createMaterielFormGroup(sampleWithRequiredData);

        const materiel = service.getMateriel(formGroup) as any;

        expect(materiel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMateriel should not enable id FormControl', () => {
        const formGroup = service.createMaterielFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMateriel should disable id FormControl', () => {
        const formGroup = service.createMaterielFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
