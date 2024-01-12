import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuizz } from 'app/entities/quizz/quizz.model';
import { QuizzService } from 'app/entities/quizz/service/quizz.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { AttemptService } from '../service/attempt.service';
import { IAttempt } from '../attempt.model';
import { AttemptFormService, AttemptFormGroup } from './attempt-form.service';

@Component({
  standalone: true,
  selector: 'jhi-attempt-update',
  templateUrl: './attempt-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AttemptUpdateComponent implements OnInit {
  isSaving = false;
  attempt: IAttempt | null = null;

  quizzesSharedCollection: IQuizz[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: AttemptFormGroup = this.attemptFormService.createAttemptFormGroup();

  constructor(
    protected attemptService: AttemptService,
    protected attemptFormService: AttemptFormService,
    protected quizzService: QuizzService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareQuizz = (o1: IQuizz | null, o2: IQuizz | null): boolean => this.quizzService.compareQuizz(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attempt }) => {
      this.attempt = attempt;
      if (attempt) {
        this.updateForm(attempt);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attempt = this.attemptFormService.getAttempt(this.editForm);
    if (attempt.id !== null) {
      this.subscribeToSaveResponse(this.attemptService.update(attempt));
    } else {
      this.subscribeToSaveResponse(this.attemptService.create(attempt));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttempt>>): void {
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

  protected updateForm(attempt: IAttempt): void {
    this.attempt = attempt;
    this.attemptFormService.resetForm(this.editForm, attempt);

    this.quizzesSharedCollection = this.quizzService.addQuizzToCollectionIfMissing<IQuizz>(this.quizzesSharedCollection, attempt.quizz);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, attempt.user);
  }

  protected loadRelationshipsOptions(): void {
    this.quizzService
      .query()
      .pipe(map((res: HttpResponse<IQuizz[]>) => res.body ?? []))
      .pipe(map((quizzes: IQuizz[]) => this.quizzService.addQuizzToCollectionIfMissing<IQuizz>(quizzes, this.attempt?.quizz)))
      .subscribe((quizzes: IQuizz[]) => (this.quizzesSharedCollection = quizzes));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.attempt?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
