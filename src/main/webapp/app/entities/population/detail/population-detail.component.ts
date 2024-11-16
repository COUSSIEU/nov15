import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPopulation } from '../population.model';

@Component({
  standalone: true,
  selector: 'jhi-population-detail',
  templateUrl: './population-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PopulationDetailComponent {
  population = input<IPopulation | null>(null);

  previousState(): void {
    window.history.back();
  }

  champs(): string[] {
    return 'nom;age;springboot;angular;html;css;transport;sport'.split(';');
  }
}
