import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEffectif } from 'app/entities/effectif/effectif.model';
import { EffectifService } from 'app/entities/effectif/service/effectif.service';
import { IChampEffectif } from '../champ-effectif.model';
import { ChampEffectifService } from '../service/champ-effectif.service';
import { ChampEffectifFormGroup, ChampEffectifFormService } from './champ-effectif-form.service';

@Component({
  standalone: true,
  selector: 'jhi-champ-effectif-update',
  templateUrl: './champ-effectif-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChampEffectifUpdateComponent implements OnInit {
  isSaving = false;
  champEffectif: IChampEffectif | null = null;

  effectifsSharedCollection: IEffectif[] = [];

  protected champEffectifService = inject(ChampEffectifService);
  protected champEffectifFormService = inject(ChampEffectifFormService);
  protected effectifService = inject(EffectifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChampEffectifFormGroup = this.champEffectifFormService.createChampEffectifFormGroup();

  compareEffectif = (o1: IEffectif | null, o2: IEffectif | null): boolean => this.effectifService.compareEffectif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ champEffectif }) => {
      this.champEffectif = champEffectif;
      if (champEffectif) {
        this.updateForm(champEffectif);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const champEffectif = this.champEffectifFormService.getChampEffectif(this.editForm);
    if (champEffectif.id !== null) {
      this.subscribeToSaveResponse(this.champEffectifService.update(champEffectif));
    } else {
      this.subscribeToSaveResponse(this.champEffectifService.create(champEffectif));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChampEffectif>>): void {
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

  protected updateForm(champEffectif: IChampEffectif): void {
    this.champEffectif = champEffectif;
    this.champEffectifFormService.resetForm(this.editForm, champEffectif);

    this.effectifsSharedCollection = this.effectifService.addEffectifToCollectionIfMissing<IEffectif>(
      this.effectifsSharedCollection,
      champEffectif.effectif,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.effectifService
      .query()
      .pipe(map((res: HttpResponse<IEffectif[]>) => res.body ?? []))
      .pipe(
        map((effectifs: IEffectif[]) =>
          this.effectifService.addEffectifToCollectionIfMissing<IEffectif>(effectifs, this.champEffectif?.effectif),
        ),
      )
      .subscribe((effectifs: IEffectif[]) => (this.effectifsSharedCollection = effectifs));
  }
}
