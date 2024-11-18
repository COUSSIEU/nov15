import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IEffectif } from '../effectif.model';

@Component({
  standalone: true,
  selector: 'jhi-effectif-detail',
  templateUrl: './effectif-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EffectifDetailComponent {
  effectif = input<IEffectif | null>(null);

  previousState(): void {
    window.history.back();
  }
}
