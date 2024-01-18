import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';
import { AttemptService } from 'app/entities/attempt/service/attempt.service';
import { Subscription, interval } from 'rxjs';
import { IQuestion } from 'app/entities/question/question.model';
import { NewAttempt } from 'app/entities/attempt/attempt.model';

import dayjs from 'dayjs/esm';
import { PlayMode } from './play-mode';

@Component({
  standalone: true,
  templateUrl: './quizz-play.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuizzPlayComponent {
  quizz?: IQuizz;
  attempt: NewAttempt | null = null;

  progress: string = '0';
  PlayModeEnum = PlayMode;
  mode = PlayMode.ENTER;
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
  ) {}

  ngOnInit(): void {
    this.questionList = this.quizz?.questions ?? [];

    const sampleWithNewData: NewAttempt = this.createNewAttempt();
    this.attempt = sampleWithNewData;
    // this.attemptService.create(this.attempt).subscribe((res: NewAttempt) => {
    //   this.attempt = res;
    // }
  }

  private createNewAttempt(): NewAttempt {
    return {
      id: null,
      started: dayjs(),
      quizz: this.quizz,
    };
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
    this.mode = PlayMode.PLAYING;
    this.subscription.push(
      this.timer.subscribe(res => {
        console.log(res);
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
