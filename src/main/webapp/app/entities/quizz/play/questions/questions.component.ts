import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IQuizz } from '../../quizz.model';
import { IAttempt } from 'app/entities/attempt/attempt.model';
import { Subscription, interval } from 'rxjs';
import { IAttemptAnswer } from 'app/entities/attempt-answer/attempt-answer.model';
import { IQuestion } from 'app/entities/question/question.model';
import SharedModule from 'app/shared/shared.module';

import { PlayMode } from '../play-mode';
import { PlayFormComponent } from '../play-form/play-form.component';
import dayjs from 'dayjs/esm';
import { CountDownComponent } from 'app/shared/count-down/count-down.component';

@Component({
  selector: 'jhi-questions',
  standalone: true,
  imports: [SharedModule, PlayFormComponent, CountDownComponent],
  templateUrl: './questions.component.html',
})
export class QuestionsComponent implements OnInit {
  @Input({ required: true }) quizz!: IQuizz;

  @Input({ required: true }) attempt!: IAttempt;
  // @Input()
  // set attempt(value: IAttempt) {
  //   this.attempt = value;
  //   this.startQuizz();
  // }

  @Output() onNextStep = new EventEmitter<string>();

  currentQuestionIndex: number = 0;
  // questionList: IQuestion[] = [];
  answerList: IAttemptAnswer[] = [];

  progress: number = 0;
  answerCount: number = 0;

  timerEnabled: boolean = true;
  remainingTime: number = 0;

  timer = interval(1000); // every second (1000ms)
  subscription: Subscription[] = [];

  ngOnInit(): void {
    this.startQuizz();
  }

  startQuizz() {
    this.answerList = this.attempt.answers || [];
    if (this.quizz.maxAnswerTime != null && this.quizz.maxAnswerTime > 0) {
      this.remainingTime = this.quizz.maxAnswerTime;
      this.timerEnabled = true;
    } else {
      this.timerEnabled = false;
    }
    this.startCounter();
    this.answerList[this.currentQuestionIndex].started = dayjs();
  }

  updateChoice(choice: string) {
    this.answerList[this.currentQuestionIndex].option = { id: choice };
    this.computeAnsweredCount(this.answerList);
    this.getProgressPercent();
  }

  nextQuestion(): void {
    this.answerList[this.currentQuestionIndex].ended = dayjs();
    if (this.currentQuestionIndex < this.answerList.length - 1) {
      this.currentQuestionIndex++;
      // this.answerSelected.patchValue(this.answerList[this.currentQuestionIndex].option?.id);
    } else {
      this.stopCounter();
    }
  }

  previousQuestion(): void {
    if (this.currentQuestionIndex > 0) {
      // this.answerSelected.patchValue(this.answerList[this.currentQuestionIndex].option?.id);
      this.currentQuestionIndex--;
    }
  }

  getCurrentQuestion(): IQuestion {
    return this.findQuestionById(this.answerList[this.currentQuestionIndex]?.question?.id ?? '');
  }

  findQuestionById(id: string): IQuestion {
    const question = this.quizz?.questions?.find(question => question.id === id);
    return question !== undefined ? question : { id: '', statement: 'Question not found', options: [] };
  }

  private startCounter() {
    if (!this.timerEnabled) {
      return;
    }
    this.subscription.push(
      this.timer.subscribe(res => {
        if (this.remainingTime !== 0) {
          this.remainingTime--;
        }
        // Auto move to next question after x seconds
        if (this.remainingTime === 0) {
          this.nextQuestion();
          this.remainingTime = this.quizz?.maxAnswerTime ?? 0;
        }
      }),
    );
  }

  private stopCounter() {
    if (!this.timerEnabled) {
      return;
    }
    this.subscription.forEach(element => {
      element.unsubscribe();
    });
  }

  getProgressPercent() {
    if (this.attempt == null || this.attempt.answers == null || this.attempt.answers.length === 0) {
      this.progress = 0;
    } else {
      this.progress = (this.answerCount / this.attempt!.answers.length) * 100;
    }
  }

  computeAnsweredCount(answers: IAttemptAnswer[]) {
    this.answerCount = answers.filter((a: IAttemptAnswer) => a.option?.id != null).length;
  }

  finishAttempt() {
    this.onNextStep.emit(PlayMode.RESULTS);
  }

  goTo(index: number) {
    this.currentQuestionIndex = index;
  }

  isAnswered(answer: IAttemptAnswer) {
    return answer.option?.id === undefined ? 'Not Answered' : 'Answered';
  }
}
