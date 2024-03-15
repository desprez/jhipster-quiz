import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IQuizz } from '../quizz.model';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { ConfirmationDialogService } from 'app/shared/confirmation-dialog/confirmation-dialog.service';
import { NgbAccordionModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DigitToLetterPipe } from 'app/shared/pipe/digit-to-letter.pipe';

@Component({
  standalone: true,
  selector: 'jhi-quizz-detail',
  templateUrl: './quizz-detail.component.html',
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    HasAnyAuthorityDirective,
    NgbAccordionModule,
    DigitToLetterPipe,
  ],
})
export class QuizzDetailComponent {
  @Input() quizz: IQuizz | null = null;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    private confirmationDialogService: ConfirmationDialogService,
  ) {}

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  publish(): void {
    this.confirmationDialogService
      .confirm('Please confirm..', 'Do you really want to ... ?')
      .then(confirmed => console.log('User confirmed:', confirmed))
      .catch(() => console.log('User dismissed the dialog (e.g., by using ESC, clicking the cross icon, or clicking outside the dialog)'));
  }
}
