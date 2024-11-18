import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMateriel } from '../materiel.model';

@Component({
  standalone: true,
  selector: 'jhi-materiel-detail',
  templateUrl: './materiel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaterielDetailComponent {
  materiel = input<IMateriel | null>(null);

  previousState(): void {
    window.history.back();
  }
}
