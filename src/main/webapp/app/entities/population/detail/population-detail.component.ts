import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPopulation } from '../population.model';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'jhi-population-detail',
  templateUrl: './population-detail.component.html',
  imports: [SharedModule, RouterModule, ReactiveFormsModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PopulationDetailComponent {
  population = input<IPopulation | null>(null);
  formGroup = new FormGroup({
    lignes: new FormControl(''),
    colonnes: new FormControl(''),
  });
  previousState(): void {
    window.history.back();
  }
  decoupe(attributeNames: string | null | undefined): string[] {
    return attributeNames ? attributeNames.split(';') : [];
  }
  champs(): string[] {
    return 'nom;age;springboot;angular;html;css;transport;sport'.split(';');
  }
}
