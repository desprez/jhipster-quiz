import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAttempt } from '../attempt.model';
import { AttemptService } from '../service/attempt.service';

@Component({
  standalone: true,
  templateUrl: './attempt-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AttemptDeleteDialogComponent {
  attempt?: IAttempt;

  constructor(
    protected attemptService: AttemptService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attemptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
