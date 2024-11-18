import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEffectif } from '../effectif.model';
import { EffectifService } from '../service/effectif.service';
import { EffectifFormGroup, EffectifFormService } from './effectif-form.service';

@Component({
  standalone: true,
  selector: 'jhi-effectif-update',
  templateUrl: './effectif-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EffectifUpdateComponent implements OnInit {
  isSaving = false;
  effectif: IEffectif | null = null;

  protected effectifService = inject(EffectifService);
  protected effectifFormService = inject(EffectifFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EffectifFormGroup = this.effectifFormService.createEffectifFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ effectif }) => {
      this.effectif = effectif;
      if (effectif) {
        this.updateForm(effectif);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const effectif = this.effectifFormService.getEffectif(this.editForm);
    if (effectif.id !== null) {
      this.subscribeToSaveResponse(this.effectifService.update(effectif));
    } else {
      this.subscribeToSaveResponse(this.effectifService.create(effectif));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEffectif>>): void {
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

  protected updateForm(effectif: IEffectif): void {
    this.effectif = effectif;
    this.effectifFormService.resetForm(this.editForm, effectif);
  }
}
