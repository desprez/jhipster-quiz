import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';
import { AttemptService } from 'app/entities/attempt/service/attempt.service';
import { Observable, Subscription, interval } from 'rxjs';
import { IQuestion } from 'app/entities/question/question.model';
import { IAttempt, NewAttempt } from 'app/entities/attempt/attempt.model';

import dayjs from 'dayjs/esm';
import { PlayMode } from './play-mode';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  standalone: true,
  templateUrl: './quizz-play.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizzPlayComponent {
  @Input() quizz: IQuizz | null = null;

  attempt: IAttempt | null = null;

  progress: string = '0';
  PlayModeEnum = PlayMode;
  mode = PlayMode.SHOW_RULES;
  showWarning: boolean = false;

  questionList: IQuestion[] = [];
  currentQuestionNo: number = 0;

  remainingTime: number = 10;

  timer = interval(1000);
  subscription: Subscription[] = [];
  correctAnswerCount: number = 0;
  constructor(
    protected quizzService: QuizzService,
    protected attemptService: AttemptService,
    protected activeModal: NgbActiveModal,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    // this.activatedRoute.data.subscribe(({ quizz }) => {
    //   this.quizz = quizz;
    // });
    this.questionList = this.quizz?.questions ?? [];
  }

  nextQuestion(): void {
    if (this.currentQuestionNo < this.questionList.length - 1) {
      this.currentQuestionNo++;
    } else {
      this.subscription.forEach(element => {
        element.unsubscribe();
      });
    }
  }

  finish(): void {
    this.mode = PlayMode.FINISHED;
  }

  start(): void {
    this.mode = PlayMode.PLAYING;
  }

  createNewAttempt(): NewAttempt {
    return {
      id: null,
      started: dayjs(),
      quizz: this.quizz,
      score: 0,
    };
  }

  showWarningPopup(): void {
    this.mode = PlayMode.SHOW_RULES;
  }

  selectOption(option: any): void {
    if (option.isCorrect) {
      this.correctAnswerCount++;
    }
    option.isSelected = true;
  }

  isOptionSelected(options: any): boolean {
    const selectionCount = options.filter((m: any) => m.isSelected === true).length;
    if (selectionCount === 0) {
      return false;
    } else {
      return true;
    }
  }

  startQuizz(): void {
    this.attemptService.create(this.createNewAttempt()).subscribe(
      (res: HttpResponse<IAttempt>) => {
        this.attempt = res.body;
      },
      (res: HttpErrorResponse) => console.log(res.message),
    );
    this.mode = PlayMode.PLAYING;

    this.subscription.push(
      this.timer.subscribe(res => {
        if (this.remainingTime !== 0) {
          this.remainingTime--;
        }
        if (this.remainingTime === 0) {
          this.nextQuestion();
          this.remainingTime = 10;
        }
      }),
    );
  }

  getProgressPercent() {
    this.progress = ((this.currentQuestionNo / this.questionList.length) * 100).toString();
    return this.progress;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  close(id: string): void {
    this.activeModal.close();
  }
}
