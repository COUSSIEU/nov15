import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IChampEffectif, NewChampEffectif } from '../champ-effectif.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChampEffectif for edit and NewChampEffectifFormGroupInput for create.
 */
type ChampEffectifFormGroupInput = IChampEffectif | PartialWithRequiredKeyOf<NewChampEffectif>;

type ChampEffectifFormDefaults = Pick<NewChampEffectif, 'id'>;

type ChampEffectifFormGroupContent = {
  id: FormControl<IChampEffectif['id'] | NewChampEffectif['id']>;
  nom: FormControl<IChampEffectif['nom']>;
  valeur: FormControl<IChampEffectif['valeur']>;
  effectif: FormControl<IChampEffectif['effectif']>;
};

export type ChampEffectifFormGroup = FormGroup<ChampEffectifFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChampEffectifFormService {
  createChampEffectifFormGroup(champEffectif: ChampEffectifFormGroupInput = { id: null }): ChampEffectifFormGroup {
    const champEffectifRawValue = {
      ...this.getFormDefaults(),
      ...champEffectif,
    };
    return new FormGroup<ChampEffectifFormGroupContent>({
      id: new FormControl(
        { value: champEffectifRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(champEffectifRawValue.nom),
      valeur: new FormControl(champEffectifRawValue.valeur),
      effectif: new FormControl(champEffectifRawValue.effectif),
    });
  }

  getChampEffectif(form: ChampEffectifFormGroup): IChampEffectif | NewChampEffectif {
    return form.getRawValue() as IChampEffectif | NewChampEffectif;
  }

  resetForm(form: ChampEffectifFormGroup, champEffectif: ChampEffectifFormGroupInput): void {
    const champEffectifRawValue = { ...this.getFormDefaults(), ...champEffectif };
    form.reset(
      {
        ...champEffectifRawValue,
        id: { value: champEffectifRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChampEffectifFormDefaults {
    return {
      id: null,
    };
  }
}
