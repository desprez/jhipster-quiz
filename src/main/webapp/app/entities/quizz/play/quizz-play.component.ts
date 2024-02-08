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
import { IOption } from 'app/entities/option/option.model';
import { IAttemptAnswer } from 'app/entities/attempt-answer/attempt-answer.model';
import { CircleProgressComponent } from 'app/circle-progress/circle-progress.component';
import { PlayRadioOptionsComponent } from 'app/entities/option/play/radio-options.component';
import { ReactiveFormsModule } from '@angular/forms';

import { PlayService } from './play.service';
import { AttemptFormGroup, PlayFormService } from './play-form.service';

@Component({
  standalone: true,
  templateUrl: './quizz-play.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, CircleProgressComponent, PlayRadioOptionsComponent],
})
export class QuizzPlayComponent {
  @Input({ required: true }) quizz!: IQuizz;

  attempt: IAttempt | null = null;

  playForm: AttemptFormGroup = this.playFormService.createAttemptFormGroup();

  public answerSelected: FormControl;

  progress: number = 0;
  PlayModeEnum = PlayMode;
  mode = PlayMode.SHOW_RULES;

  timerEnabled: boolean = true;

  questionList: IQuestion[] = [];

  currentQuestionNo: number = 0;

  remainingTime: number = 0;

  timer = interval(1000); // every second (1000ms)
  subscription: Subscription[] = [];
  answerCount: number = 0;

  constructor(
    protected quizzService: QuizzService,
    protected playService: PlayService,
    protected playFormService: PlayFormService,
    protected activeModal: NgbActiveModal,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
  ) {
    this.answerSelected = new FormControl('');
  }

  //answerSelected = new FormControl('');

  ngOnInit(): void {
    this.questionList = this.quizz.questions ?? [];
    if (this.quizz.maxAnswerTime != null && this.quizz.maxAnswerTime > 0) {
      this.remainingTime = this.quizz.maxAnswerTime;
      this.timerEnabled = true;
    } else {
      this.timerEnabled = false;
    }
  }

  showWarningPopup(): void {
    this.mode = PlayMode.SHOW_RULES;
  }

  nextQuestion(): void {
    if (this.currentQuestionNo < this.questionList.length - 1) {
      this.currentQuestionNo++;
      // this.answerSelected.patchValue(this.answerList[this.currentQuestionNo].option?.id);
    } else {
      this.stopCounter();
    }
    console.log('answerSelected:' + this.answerSelected.value);
  }

  previousQuestion(): void {
    if (this.currentQuestionNo > 0) {
      // this.answerSelected.patchValue(this.answerList[this.currentQuestionNo].option?.id);
      this.currentQuestionNo--;
    }
    console.log('answerSelected:' + this.answerSelected.value);
  }

  // selectAnswer() {
  //   console.log('answerSelected:' + this.answerSelected.value);
  //   const option = this.questionList[this.currentQuestionNo].options?.find((o: IOption) => o.id === this.answerSelected.value) ?? null;
  //   const answer: IAttemptAnswer = this.getAnswer(this.questionList[this.currentQuestionNo]);
  //   answer.option = option;
  //   answer.ended = dayjs();

  //   // const answers = this.attempt?.answers ?? []; // Ensure that answers is an array

  //   this.computeAnsweredCount(this.attempt?.answers ?? []);
  //   this.getProgressPercent();
  // }

  // getAnswer(question: IQuestion): IAttemptAnswer {
  //   return (
  //     this.attempt?.answers?.find((a: IAttemptAnswer) => a.question?.id === question.id) ?? {
  //       id: '',
  //       option: null,
  //       question: question,
  //     }
  //   );
  // }

  computeAnsweredCount(answers: IAttemptAnswer[]) {
    this.answerCount = answers.filter((a: IAttemptAnswer) => a.option != null).length;
  }

  finish(): void {
    if (this.attempt) {
      this.playService.evaluate(this.attempt).subscribe();
    }
    this.mode = PlayMode.FINISHED;
  }

  startQuizz(): void {
    this.submitNewAttempt();
    this.mode = PlayMode.PLAYING;
    this.startCounter();
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

  private submitNewAttempt() {
    this.playService.play(this.quizz.id).subscribe({
      next: (res: HttpResponse<IAttempt>) => {
        if (res.body) {
          this.updateForm(res.body);
        }
      },
      error: response => console.log(response.message),
    });
  }

  protected updateForm(attempt: IAttempt): void {
    this.attempt = attempt;
    this.playFormService.resetForm(this.playForm, attempt);
    this.setAnswsers(attempt);
  }

  setAnswsers(attempt: IAttempt): void {
    if (attempt.answers) {
      attempt.answers.forEach(answer => {
        this.answers.push(this.playFormService.initAnswer(answer));
      });
    }
  }

  getAnswer(index: number): FormGroup {
    return this.answers.at(index) as FormGroup;
  }

  getRadioBoundControl(): FormControl {
    // return this.answers.controls[this.currentQuestionNo].get('option') as FormControl;
    return this.getAnswer(this.currentQuestionNo).get('option') as FormControl;
  }

  get answers(): FormArray {
    console.log('get answers:' + this.playForm.get('answers')?.value);
    return this.playForm.get('answers') as FormArray;
  }

  createNewAttempt(): NewAttempt {
    return {
      id: null,
      started: dayjs(),
      quizz: this.quizz,
      score: 0,
    };
  }

  getProgressPercent() {
    this.progress = (this.answerCount / this.questionList.length) * 100;
  }

  retry(): void {
    this.mode = PlayMode.SHOW_RULES;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  close(id: string): void {
    this.activeModal.close();
  }
}
