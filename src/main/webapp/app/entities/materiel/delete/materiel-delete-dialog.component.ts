import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMateriel } from '../materiel.model';
import { MaterielService } from '../service/materiel.service';

@Component({
  standalone: true,
  templateUrl: './materiel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaterielDeleteDialogComponent {
  materiel?: IMateriel;

  protected materielService = inject(MaterielService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materielService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
