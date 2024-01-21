import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';

@Component({
  standalone: true,
  templateUrl: './quizz-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizzDeleteDialogComponent {
  quizz?: IQuizz;

  constructor(
    protected quizzService: QuizzService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.quizzService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
