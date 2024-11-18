import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEffectif, NewEffectif } from '../effectif.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEffectif for edit and NewEffectifFormGroupInput for create.
 */
type EffectifFormGroupInput = IEffectif | PartialWithRequiredKeyOf<NewEffectif>;

type EffectifFormDefaults = Pick<NewEffectif, 'id'>;

type EffectifFormGroupContent = {
  id: FormControl<IEffectif['id'] | NewEffectif['id']>;
  name: FormControl<IEffectif['name']>;
  cumul: FormControl<IEffectif['cumul']>;
};

export type EffectifFormGroup = FormGroup<EffectifFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EffectifFormService {
  createEffectifFormGroup(effectif: EffectifFormGroupInput = { id: null }): EffectifFormGroup {
    const effectifRawValue = {
      ...this.getFormDefaults(),
      ...effectif,
    };
    return new FormGroup<EffectifFormGroupContent>({
      id: new FormControl(
        { value: effectifRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(effectifRawValue.name),
      cumul: new FormControl(effectifRawValue.cumul),
    });
  }

  getEffectif(form: EffectifFormGroup): IEffectif | NewEffectif {
    return form.getRawValue() as IEffectif | NewEffectif;
  }

  resetForm(form: EffectifFormGroup, effectif: EffectifFormGroupInput): void {
    const effectifRawValue = { ...this.getFormDefaults(), ...effectif };
    form.reset(
      {
        ...effectifRawValue,
        id: { value: effectifRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EffectifFormDefaults {
    return {
      id: null,
    };
  }
}
