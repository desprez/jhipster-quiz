import { Component, Input } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';
import { Observable, Subscription, interval } from 'rxjs';
import { IQuestion } from 'app/entities/question/question.model';
import { IAttempt, NewAttempt } from 'app/entities/attempt/attempt.model';

import dayjs from 'dayjs/esm';
import { PlayMode } from './play-mode';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { ReactiveFormsModule } from '@angular/forms';

import { PlayService } from './play.service';

import { WelcomeComponent } from './welcome/welcome.component';
import { RulesComponent } from './rules/rules.component';
import { ResultsComponent } from './results/results.component';
import { SummaryComponent } from './summary/summary.component';
import { QuestionsComponent } from './questions/questions.component';

@Component({
  standalone: true,
  templateUrl: './quizz-play.component.html',
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    WelcomeComponent,
    RulesComponent,
    QuestionsComponent,
    ResultsComponent,
    SummaryComponent,
  ],
})
export class QuizzPlayComponent {
  @Input({ required: true }) quizz!: IQuizz;

  newAttempt!: IAttempt;

  PlayModeEnum = PlayMode;
  mode = PlayMode.WELCOME;

  questionList: IQuestion[] = [];

  constructor(
    protected quizzService: QuizzService,
    protected playService: PlayService,
    protected activeModal: NgbActiveModal,
    protected activatedRoute: ActivatedRoute,
  ) {}

  displayRules(): void {
    this.mode = PlayMode.RULES;
  }

  startQuizz(): void {
    this.submitNewAttempt();
    this.mode = PlayMode.PLAYING;
  }

  displaySummary(): void {
    if (this.quizz.allowReview) {
      this.mode = PlayMode.SUMMARY;
    } else {
      this.displayResult();
    }
  }

  goto(index: number): void {
    this.mode = PlayMode.PLAYING;
  }

  displayResult(): void {
    if (this.newAttempt) {
      this.playService.evaluate(this.newAttempt).subscribe({
        next: (res: HttpResponse<IAttempt>) => {
          if (res.body) {
            this.newAttempt = res.body as IAttempt;
          }
        },
        error: response => console.log(response.message),
      });
    }
    this.mode = PlayMode.RESULTS;
  }

  private submitNewAttempt(): void {
    this.playService.play(this.quizz.id).subscribe({
      next: (res: HttpResponse<IAttempt>) => {
        if (res.body) {
          this.newAttempt = res.body as IAttempt;
        }
      },
      error: response => console.log(response.message),
    });
  }

  retry(): void {
    this.mode = PlayMode.RULES;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  close(): void {
    this.activeModal.close();
  }
}
