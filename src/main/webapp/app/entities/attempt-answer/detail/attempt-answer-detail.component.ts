import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAttemptAnswer } from '../attempt-answer.model';

@Component({
  standalone: true,
  selector: 'jhi-attempt-answer-detail',
  templateUrl: './attempt-answer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AttemptAnswerDetailComponent {
  @Input() attemptAnswer: IAttemptAnswer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
