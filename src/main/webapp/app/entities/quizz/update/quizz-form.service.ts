import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuizz, NewQuizz } from '../quizz.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizz for edit and NewQuizzFormGroupInput for create.
 */
type QuizzFormGroupInput = IQuizz | PartialWithRequiredKeyOf<NewQuizz>;

type QuizzFormDefaults = Pick<NewQuizz, 'id' | 'published' | 'rollbackAllowed'>;

type QuizzFormGroupContent = {
  id: FormControl<IQuizz['id'] | NewQuizz['id']>;
  title: FormControl<IQuizz['title']>;
  description: FormControl<IQuizz['description']>;
  difficulty: FormControl<IQuizz['difficulty']>;
  category: FormControl<IQuizz['category']>;
  published: FormControl<IQuizz['published']>;
  questionOrder: FormControl<IQuizz['questionOrder']>;
  maxAnswerTime: FormControl<IQuizz['maxAnswerTime']>;
  rollbackAllowed: FormControl<IQuizz['rollbackAllowed']>;
  user: FormControl<IQuizz['user']>;
};

export type QuizzFormGroup = FormGroup<QuizzFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizzFormService {
  createQuizzFormGroup(quizz: QuizzFormGroupInput = { id: null }): QuizzFormGroup {
    const quizzRawValue = {
      ...this.getFormDefaults(),
      ...quizz,
    };
    return new FormGroup<QuizzFormGroupContent>({
      id: new FormControl(
        { value: quizzRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(quizzRawValue.title, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(100)],
      }),
      description: new FormControl(quizzRawValue.description, {
        validators: [Validators.maxLength(500)],
      }),
      difficulty: new FormControl(quizzRawValue.difficulty, {
        validators: [Validators.required],
      }),
      category: new FormControl(quizzRawValue.category, {
        validators: [Validators.required],
      }),
      published: new FormControl(quizzRawValue.published, {
        validators: [Validators.required],
      }),
      questionOrder: new FormControl(quizzRawValue.questionOrder, {
        validators: [Validators.required],
      }),
      maxAnswerTime: new FormControl(quizzRawValue.maxAnswerTime, {
        validators: [Validators.required],
      }),
      rollbackAllowed: new FormControl(quizzRawValue.rollbackAllowed, {
        validators: [Validators.required],
      }),
      user: new FormControl(quizzRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getQuizz(form: QuizzFormGroup): IQuizz | NewQuizz {
    return form.getRawValue() as IQuizz | NewQuizz;
  }

  resetForm(form: QuizzFormGroup, quizz: QuizzFormGroupInput): void {
    const quizzRawValue = { ...this.getFormDefaults(), ...quizz };
    form.reset(
      {
        ...quizzRawValue,
        id: { value: quizzRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizzFormDefaults {
    return {
      id: null,
      published: false,
      rollbackAllowed: false,
    };
  }
}
