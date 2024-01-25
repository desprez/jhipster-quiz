import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators, FormArray, FormBuilder } from '@angular/forms';

import { IQuizz, NewQuizz } from '../quizz.model';
import { IQuestion, NewQuestion } from 'app/entities/question/question.model';
import { IOption, NewOption } from 'app/entities/option/option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizz for edit and NewQuizzFormGroupInput for create.
 */
type QuizzFormGroupInput = IQuizz | PartialWithRequiredKeyOf<NewQuizz>;

type QuizzFormDefaults = Pick<NewQuizz, 'id' | 'allowBack' | 'allowReview' | 'secretGoodAnwers' | 'published'>;

type OptionFormGroupContent = {
  id: FormControl<IOption['id'] | NewOption['id']>;
  statement: FormControl<IOption['statement']>;
  index: FormControl<IOption['index']>;
};

type QuestionFormGroupContent = {
  id: FormControl<IQuestion['id'] | NewQuestion['id']>;
  statement: FormControl<IQuestion['statement']>;
  index: FormControl<IQuestion['index']>;
  correctOptionIndex: FormControl<IQuestion['correctOptionIndex']>;
  options: FormArray<FormGroup<OptionFormGroupContent>>;
};

type QuizzFormGroupContent = {
  id: FormControl<IQuizz['id'] | NewQuizz['id']>;
  title: FormControl<IQuizz['title']>;
  description: FormControl<IQuizz['description']>;
  difficulty: FormControl<IQuizz['difficulty']>;
  category: FormControl<IQuizz['category']>;
  questionOrder: FormControl<IQuizz['questionOrder']>;
  maxAnswerTime: FormControl<IQuizz['maxAnswerTime']>;
  allowBack: FormControl<IQuizz['allowBack']>;
  allowReview: FormControl<IQuizz['allowReview']>;
  secretGoodAnwers: FormControl<IQuizz['secretGoodAnwers']>;
  image: FormControl<IQuizz['image']>;
  imageContentType: FormControl<IQuizz['imageContentType']>;
  published: FormControl<IQuizz['published']>;
  user: FormControl<IQuizz['user']>;
  questions: FormArray<FormGroup<QuestionFormGroupContent>>;
};

export type QuizzFormGroup = FormGroup<QuizzFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizzFormService {
  constructor(private fb: FormBuilder) {}

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
      questionOrder: new FormControl(quizzRawValue.questionOrder, {
        validators: [Validators.required],
      }),
      maxAnswerTime: new FormControl(quizzRawValue.maxAnswerTime),
      allowBack: new FormControl(quizzRawValue.allowBack, {
        validators: [Validators.required],
      }),
      allowReview: new FormControl(quizzRawValue.allowReview, {
        validators: [Validators.required],
      }),
      secretGoodAnwers: new FormControl(quizzRawValue.secretGoodAnwers, {
        validators: [Validators.required],
      }),
      image: new FormControl(quizzRawValue.image),
      imageContentType: new FormControl(quizzRawValue.imageContentType),
      published: new FormControl(quizzRawValue.published, {
        validators: [Validators.required],
      }),
      user: new FormControl(quizzRawValue.user, {
        validators: [Validators.required],
      }),
      questions: new FormArray<FormGroup<QuestionFormGroupContent>>(
        (quizzRawValue.questions ?? []).map(question => this.initQuestion(question)),
      ),
    });
  }

  initQuestion(questionRawValue: IQuestion): FormGroup<QuestionFormGroupContent> {
    return new FormGroup<QuestionFormGroupContent>({
      id: new FormControl({ value: questionRawValue.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      statement: new FormControl(questionRawValue.statement, { validators: [Validators.required] }),
      index: new FormControl(questionRawValue.index, { validators: [Validators.required] }),
      correctOptionIndex: new FormControl(questionRawValue.correctOptionIndex, { validators: [Validators.required] }),
      options: new FormArray<FormGroup<OptionFormGroupContent>>((questionRawValue.options ?? []).map(option => this.initOption(option))),
    });
  }

  initOption(optionRawValue: IOption): FormGroup<OptionFormGroupContent> {
    return new FormGroup<OptionFormGroupContent>({
      id: new FormControl({ value: optionRawValue.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      statement: new FormControl(optionRawValue.statement, { validators: [Validators.required] }),
      index: new FormControl(optionRawValue.index, { validators: [Validators.required] }),
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
      allowBack: false,
      allowReview: false,
      secretGoodAnwers: false,
      published: false,
    };
  }
}
