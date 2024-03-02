import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAttempt } from '../attempt.model';

import { CircleProgressComponent } from 'app/circle-progress/circle-progress.component';

@Component({
  standalone: true,
  selector: 'jhi-attempt-detail',
  templateUrl: './attempt-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, CircleProgressComponent],
})
export class AttemptDetailComponent implements OnInit {
  current = 27;
  max = 50;

  @Input() attempt: IAttempt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.current = this.attempt?.correctAnswerCount || 0;
    this.max = (this.attempt?.correctAnswerCount ?? 0) + (this.attempt?.wrongAnswerCount ?? 0) + (this.attempt?.unansweredCount ?? 0);
  }

  previousState(): void {
    window.history.back();
  }
}
