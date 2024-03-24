import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAttempt } from '../attempt.model';

import { PercentPipe } from '@angular/common';
import { RoundProgressComponent } from 'angular-svg-round-progressbar';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';

@Component({
  standalone: true,
  selector: 'jhi-attempt-detail',
  templateUrl: './attempt-detail.component.html',
  styleUrl: './attempt-detail.component.scss',
  // styles: [`
  // .progress-percentage-wrapper {
  //   position: relative;
  //   margin: 20px auto;
  //   font-size: 50px;
  //   width: 200px;
  //   height: 200px;
  // }
  // .progress-percentage {
  //   position: absolute;
  //   color: #bbb;
  //   width: 100%;
  //   text-align: center !important;
  //   top: 30%;
  // }
  // `],
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    RoundProgressComponent,
    PercentPipe,
    HasAnyAuthorityDirective,
  ],
})
export class AttemptDetailComponent implements OnInit {
  current = 0;
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
