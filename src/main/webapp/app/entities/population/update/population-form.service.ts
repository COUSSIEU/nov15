import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPopulation, NewPopulation } from '../population.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPopulation for edit and NewPopulationFormGroupInput for create.
 */
type PopulationFormGroupInput = IPopulation | PartialWithRequiredKeyOf<NewPopulation>;

type PopulationFormDefaults = Pick<NewPopulation, 'id'>;

type PopulationFormGroupContent = {
  id: FormControl<IPopulation['id'] | NewPopulation['id']>;
  tableName: FormControl<IPopulation['tableName']>;
  attributNames: FormControl<IPopulation['attributNames']>;
  typeNames: FormControl<IPopulation['typeNames']>;
};

export type PopulationFormGroup = FormGroup<PopulationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PopulationFormService {
  createPopulationFormGroup(population: PopulationFormGroupInput = { id: null }): PopulationFormGroup {
    const populationRawValue = {
      ...this.getFormDefaults(),
      ...population,
    };
    return new FormGroup<PopulationFormGroupContent>({
      id: new FormControl(
        { value: populationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tableName: new FormControl(populationRawValue.tableName),
      attributNames: new FormControl(populationRawValue.attributNames),
      typeNames: new FormControl(populationRawValue.typeNames),
    });
  }

  getPopulation(form: PopulationFormGroup): IPopulation | NewPopulation {
    return form.getRawValue() as IPopulation | NewPopulation;
  }

  resetForm(form: PopulationFormGroup, population: PopulationFormGroupInput): void {
    const populationRawValue = { ...this.getFormDefaults(), ...population };
    form.reset(
      {
        ...populationRawValue,
        id: { value: populationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PopulationFormDefaults {
    return {
      id: null,
    };
  }
}
