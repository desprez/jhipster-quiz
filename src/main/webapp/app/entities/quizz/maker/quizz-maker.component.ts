import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { FormArray, FormBuilder, FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';
import { Category } from 'app/entities/enumerations/category.model';
import { DisplayOrder } from 'app/entities/enumerations/display-order.model';
import { Period } from 'app/entities/enumerations/period.model';
import { QuizzService } from '../service/quizz.service';
import { IQuizz } from '../quizz.model';
import { QuizzFormService, QuizzFormGroup } from '../update/quizz-form.service';
import { QuestionMakerComponent } from './question-maker/question-maker.component';

@Component({
  standalone: true,
  selector: 'jhi-quizz-maker',
  templateUrl: './quizz-maker.component.html',
  styleUrl: './quizz-maker.component.scss',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, HasAnyAuthorityDirective, QuestionMakerComponent],
})
export class QuizzMakerComponent implements OnInit {
  isSaving = false;
  quizz: IQuizz | null = null;
  difficultyValues = Object.keys(Difficulty);
  categoryValues = Object.keys(Category);
  displayOrderValues = Object.keys(DisplayOrder);
  periodValues = Object.keys(Period);

  usersSharedCollection: IUser[] = [];

  editForm: QuizzFormGroup = this.quizzFormService.createQuizzFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected quizzService: QuizzService,
    protected quizzFormService: QuizzFormService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
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

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('quizzApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
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
    this.setQuestions(quizz);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, quizz.user);
  }

  get questions(): FormArray {
    return this.editForm.get('questions') as FormArray;
  }

  setQuestions(quizz: IQuizz): void {
    if (quizz.questions) {
      quizz.questions.forEach(question => {
        this.questions.push(this.quizzFormService.initQuestion(question));
      });
    }
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.quizz?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
