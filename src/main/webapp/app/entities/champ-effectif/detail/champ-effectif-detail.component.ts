import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IChampEffectif } from '../champ-effectif.model';

@Component({
  standalone: true,
  selector: 'jhi-champ-effectif-detail',
  templateUrl: './champ-effectif-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ChampEffectifDetailComponent {
  champEffectif = input<IChampEffectif | null>(null);

  previousState(): void {
    window.history.back();
  }
}
