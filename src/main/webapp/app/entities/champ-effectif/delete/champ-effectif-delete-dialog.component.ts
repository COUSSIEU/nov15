import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IChampEffectif } from '../champ-effectif.model';
import { ChampEffectifService } from '../service/champ-effectif.service';

@Component({
  standalone: true,
  templateUrl: './champ-effectif-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ChampEffectifDeleteDialogComponent {
  champEffectif?: IChampEffectif;

  protected champEffectifService = inject(ChampEffectifService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.champEffectifService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
