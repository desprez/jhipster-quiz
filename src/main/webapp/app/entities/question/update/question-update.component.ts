import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuizz } from 'app/entities/quizz/quizz.model';
import { QuizzService } from 'app/entities/quizz/service/quizz.service';
import { IQuestion } from '../question.model';
import { QuestionService } from '../service/question.service';
import { QuestionFormService, QuestionFormGroup } from './question-form.service';

@Component({
  standalone: true,
  selector: 'jhi-question-update',
  templateUrl: './question-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuestionUpdateComponent implements OnInit {
  isSaving = false;
  question: IQuestion | null = null;

  quizzesSharedCollection: IQuizz[] = [];

  editForm: QuestionFormGroup = this.questionFormService.createQuestionFormGroup();

  constructor(
    protected questionService: QuestionService,
    protected questionFormService: QuestionFormService,
    protected quizzService: QuizzService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareQuizz = (o1: IQuizz | null, o2: IQuizz | null): boolean => this.quizzService.compareQuizz(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ question }) => {
      this.question = question;
      if (question) {
        this.updateForm(question);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const question = this.questionFormService.getQuestion(this.editForm);
    if (question.id !== null) {
      this.subscribeToSaveResponse(this.questionService.update(question));
    } else {
      this.subscribeToSaveResponse(this.questionService.create(question));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(question: IQuestion): void {
    this.question = question;
    this.questionFormService.resetForm(this.editForm, question);

    this.quizzesSharedCollection = this.quizzService.addQuizzToCollectionIfMissing<IQuizz>(this.quizzesSharedCollection, question.quizz);
  }

  protected loadRelationshipsOptions(): void {
    this.quizzService
      .query()
      .pipe(map((res: HttpResponse<IQuizz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuizz[]) => this.quizzService.addQuizzToCollectionIfMissing<IQuizz>(quizzes, this.question?.quizz)))
      .subscribe((quizzes: IQuizz[]) => (this.quizzesSharedCollection = quizzes));
  }
}
