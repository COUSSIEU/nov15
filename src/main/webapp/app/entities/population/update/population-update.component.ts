import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPopulation } from '../population.model';
import { PopulationService } from '../service/population.service';
import { PopulationFormGroup, PopulationFormService } from './population-form.service';

@Component({
  standalone: true,
  selector: 'jhi-population-update',
  templateUrl: './population-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PopulationUpdateComponent implements OnInit {
  isSaving = false;
  population: IPopulation | null = null;

  protected populationService = inject(PopulationService);
  protected populationFormService = inject(PopulationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PopulationFormGroup = this.populationFormService.createPopulationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ population }) => {
      this.population = population;
      if (population) {
        this.updateForm(population);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const population = this.populationFormService.getPopulation(this.editForm);
    if (population.id !== null) {
      this.subscribeToSaveResponse(this.populationService.update(population));
    } else {
      this.subscribeToSaveResponse(this.populationService.create(population));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPopulation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(population: IPopulation): void {
    this.population = population;
    this.populationFormService.resetForm(this.editForm, population);
  }
}
