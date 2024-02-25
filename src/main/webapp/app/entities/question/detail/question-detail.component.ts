import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IQuestion } from '../question.model';
import { DigitToLetterPipe } from 'app/shared/pipe/digit-to-letter.pipe';

@Component({
  standalone: true,
  selector: 'jhi-question-detail',
  templateUrl: './question-detail.component.html',
  styleUrl: './question-detail.component.scss',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, DigitToLetterPipe],
})
export class QuestionDetailComponent {
  @Input() question: IQuestion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
