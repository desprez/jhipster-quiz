import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';
import { Category } from 'app/entities/enumerations/category.model';
import { DisplayOrder } from 'app/entities/enumerations/display-order.model';
import { QuizzService } from '../service/quizz.service';
import { IQuizz } from '../quizz.model';
import { QuizzFormService, QuizzFormGroup } from './quizz-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quizz-update',
  templateUrl: './quizz-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuizzUpdateComponent implements OnInit {
  isSaving = false;
  quizz: IQuizz | null = null;
  difficultyValues = Object.keys(Difficulty);
  categoryValues = Object.keys(Category);
  displayOrderValues = Object.keys(DisplayOrder);

  usersSharedCollection: IUser[] = [];

  editForm: QuizzFormGroup = this.quizzFormService.createQuizzFormGroup();

  constructor(
    protected quizzService: QuizzService,
    protected quizzFormService: QuizzFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizz }) => {
      this.quizz = quizz;
      if (quizz) {
        this.updateForm(quizz);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizz = this.quizzFormService.getQuizz(this.editForm);
    if (quizz.id !== null) {
      this.subscribeToSaveResponse(this.quizzService.update(quizz));
    } else {
      this.subscribeToSaveResponse(this.quizzService.create(quizz));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizz>>): void {
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

  protected updateForm(quizz: IQuizz): void {
    this.quizz = quizz;
    this.quizzFormService.resetForm(this.editForm, quizz);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, quizz.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.quizz?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}