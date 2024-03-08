import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IAttempt } from 'app/entities/attempt/attempt.model';
import { IAttemptAnswer } from 'app/entities/attempt-answer/attempt-answer.model';

@Component({
  selector: 'jhi-summary',
  standalone: true,
  imports: [],
  templateUrl: './summary.component.html',
})
export class SummaryComponent {
  @Input({ required: true }) attempt!: IAttempt;

  @Output() onNextStep = new EventEmitter<number>();

  goTo(index: number) {
    this.onNextStep.emit(index);
  }

  isAnswered(answer: IAttemptAnswer) {
    return answer.option?.id === undefined ? 'Not Answered' : 'Answered';
  }
}
