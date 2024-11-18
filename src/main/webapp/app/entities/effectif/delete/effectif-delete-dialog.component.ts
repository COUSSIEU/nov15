import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEffectif } from '../effectif.model';
import { EffectifService } from '../service/effectif.service';

@Component({
  standalone: true,
  templateUrl: './effectif-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EffectifDeleteDialogComponent {
  effectif?: IEffectif;

  protected effectifService = inject(EffectifService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.effectifService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
