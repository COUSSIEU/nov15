import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMateriel, NewMateriel } from '../materiel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMateriel for edit and NewMaterielFormGroupInput for create.
 */
type MaterielFormGroupInput = IMateriel | PartialWithRequiredKeyOf<NewMateriel>;

type MaterielFormDefaults = Pick<NewMateriel, 'id'>;

type MaterielFormGroupContent = {
  id: FormControl<IMateriel['id'] | NewMateriel['id']>;
  etat: FormControl<IMateriel['etat']>;
  release: FormControl<IMateriel['release']>;
  modele: FormControl<IMateriel['modele']>;
  sorte: FormControl<IMateriel['sorte']>;
  site: FormControl<IMateriel['site']>;
  region: FormControl<IMateriel['region']>;
  mission: FormControl<IMateriel['mission']>;
  entite: FormControl<IMateriel['entite']>;
};

export type MaterielFormGroup = FormGroup<MaterielFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterielFormService {
  createMaterielFormGroup(materiel: MaterielFormGroupInput = { id: null }): MaterielFormGroup {
    const materielRawValue = {
      ...this.getFormDefaults(),
      ...materiel,
    };
    return new FormGroup<MaterielFormGroupContent>({
      id: new FormControl(
        { value: materielRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      etat: new FormControl(materielRawValue.etat),
      release: new FormControl(materielRawValue.release),
      modele: new FormControl(materielRawValue.modele),
      sorte: new FormControl(materielRawValue.sorte),
      site: new FormControl(materielRawValue.site),
      region: new FormControl(materielRawValue.region),
      mission: new FormControl(materielRawValue.mission),
      entite: new FormControl(materielRawValue.entite),
    });
  }

  getMateriel(form: MaterielFormGroup): IMateriel | NewMateriel {
    return form.getRawValue() as IMateriel | NewMateriel;
  }

  resetForm(form: MaterielFormGroup, materiel: MaterielFormGroupInput): void {
    const materielRawValue = { ...this.getFormDefaults(), ...materiel };
    form.reset(
      {
        ...materielRawValue,
        id: { value: materielRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterielFormDefaults {
    return {
      id: null,
    };
  }
}
