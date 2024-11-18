import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMateriel } from '../materiel.model';
import { MaterielService } from '../service/materiel.service';
import { MaterielFormGroup, MaterielFormService } from './materiel-form.service';

@Component({
  standalone: true,
  selector: 'jhi-materiel-update',
  templateUrl: './materiel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterielUpdateComponent implements OnInit {
  isSaving = false;
  materiel: IMateriel | null = null;

  protected materielService = inject(MaterielService);
  protected materielFormService = inject(MaterielFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterielFormGroup = this.materielFormService.createMaterielFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materiel }) => {
      this.materiel = materiel;
      if (materiel) {
        this.updateForm(materiel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materiel = this.materielFormService.getMateriel(this.editForm);
    if (materiel.id !== null) {
      this.subscribeToSaveResponse(this.materielService.update(materiel));
    } else {
      this.subscribeToSaveResponse(this.materielService.create(materiel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMateriel>>): void {
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

  protected updateForm(materiel: IMateriel): void {
    this.materiel = materiel;
    this.materielFormService.resetForm(this.editForm, materiel);
  }
}
