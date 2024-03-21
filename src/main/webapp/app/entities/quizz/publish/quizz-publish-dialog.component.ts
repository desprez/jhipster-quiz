import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_PUBLISHED_EVENT } from 'app/config/navigation.constants';
import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';

@Component({
  standalone: true,
  templateUrl: './quizz-publish-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizzPublishDialogComponent {
  quizz?: IQuizz;

  constructor(
    protected quizzService: QuizzService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmPublish(id: string): void {
    this.quizzService.publish(id).subscribe(() => {
      this.activeModal.close(ITEM_PUBLISHED_EVENT);
    });
  }
}
