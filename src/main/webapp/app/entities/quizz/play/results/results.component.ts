import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

import { IAttempt } from 'app/entities/attempt/attempt.model';
import SharedModule from 'app/shared/shared.module';
import { IQuizz } from '../../quizz.model';
import { PercentPipe } from '@angular/common';
import { RoundProgressComponent } from 'angular-svg-round-progressbar';

@Component({
  selector: 'jhi-results',
  standalone: true,
  imports: [SharedModule, RoundProgressComponent, PercentPipe],
  templateUrl: './results.component.html',
  styleUrl: './results.component.scss',
})
export class ResultsComponent implements OnChanges {
  current: number = 0;
  max: number = 0;
  answered: number = 0;
  percent: number = 0;

  @Input({ required: true }) attempt!: IAttempt;

  @Input({ required: true }) quizz!: IQuizz;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['attempt']) {
      this.current = this.attempt?.correctAnswerCount || 0;
      this.max = (this.attempt?.correctAnswerCount ?? 0) + (this.attempt?.wrongAnswerCount ?? 0) + (this.attempt?.unansweredCount ?? 0);
      this.answered = (this.attempt?.correctAnswerCount ?? 0) + (this.attempt?.wrongAnswerCount ?? 0);
      this.percent = (this.current / this.max) * 100;
    }
  }
}
