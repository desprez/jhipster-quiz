import { Component, Input } from '@angular/core';
import { CircleProgressComponent } from 'app/circle-progress/circle-progress.component';
import { IAttemptAnswer } from 'app/entities/attempt-answer/attempt-answer.model';

import { IAttempt } from 'app/entities/attempt/attempt.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-results',
  standalone: true,
  imports: [SharedModule, CircleProgressComponent],
  templateUrl: './results.component.html',
})
export class ResultsComponent {
  @Input({ required: true }) attempt!: IAttempt;

  progress: number = 10;

  answersCount: number = 0;
}
