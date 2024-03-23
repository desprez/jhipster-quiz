import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators, FormArray, FormBuilder } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IQuizz | NewQuizz> = Omit<T, 'publishDate'> & {
  publishDate?: string | null;
};

type QuizzFormRawValue = FormValueOf<IQuizz>;

type NewQuizzFormRawValue = FormValueOf<NewQuizz>;

type QuizzFormDefaults = Pick<NewQuizz, 'id' | 'allowBack' | 'allowReview' | 'keepAnswersSecret' | 'published' | 'publishDate'>;

type OptionFormGroupContent = {
  id: FormControl<IOption['id'] | NewOption['id']>;
  statement: FormControl<IOption['statement']>;
  index: FormControl<IOption['index']>;
};

export type OptionFormGroup = FormGroup<OptionFormGroupContent>;

type QuestionFormGroupContent = {
  id: FormControl<IQuestion['id'] | NewQuestion['id']>;
  statement: FormControl<IQuestion['statement']>;
  index: FormControl<IQuestion['index']>;
  correctOptionIndex: FormControl<IQuestion['correctOptionIndex']>;
  options: FormArray<FormGroup<OptionFormGroupContent>>;
};

export type QuestionFormGroup = FormGroup<QuestionFormGroupContent>;

type QuizzFormGroupContent = {
  id: FormControl<QuizzFormRawValue['id'] | NewQuizz['id']>;
  title: FormControl<QuizzFormRawValue['title']>;
  description: FormControl<QuizzFormRawValue['description']>;
  difficulty: FormControl<QuizzFormRawValue['difficulty']>;
  category: FormControl<QuizzFormRawValue['category']>;
  questionOrder: FormControl<QuizzFormRawValue['questionOrder']>;
  maxAnswerTime: FormControl<QuizzFormRawValue['maxAnswerTime']>;
  allowBack: FormControl<QuizzFormRawValue['allowBack']>;
  allowReview: FormControl<QuizzFormRawValue['allowReview']>;
  keepAnswersSecret: FormControl<QuizzFormRawValue['keepAnswersSecret']>;
  image: FormControl<QuizzFormRawValue['image']>;
  imageContentType: FormControl<QuizzFormRawValue['imageContentType']>;
  published: FormControl<QuizzFormRawValue['published']>;
  publishDate: FormControl<QuizzFormRawValue['publishDate']>;
  attempsLimit: FormControl<QuizzFormRawValue['attempsLimit']>;
  attempsLimitPeriod: FormControl<QuizzFormRawValue['attempsLimitPeriod']>;
  questionCount: FormControl<QuizzFormRawValue['questionCount']>;
  passingScore: FormControl<QuizzFormRawValue['passingScore']>;
  user: FormControl<QuizzFormRawValue['user']>;
  questions: FormArray<FormGroup<QuestionFormGroupContent>>;
};

export type QuizzFormGroup = FormGroup<QuizzFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizzFormService {
  constructor(private fb: FormBuilder) {}

  createQuizzFormGroup(quizz: QuizzFormGroupInput = { id: null }): QuizzFormGroup {
    const quizzRawValue = this.convertQuizzToQuizzRawValue({
      ...this.getFormDefaults(),
      ...quizz,
    });
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
      keepAnswersSecret: new FormControl(quizzRawValue.keepAnswersSecret, {
        validators: [Validators.required],
      }),
      image: new FormControl(quizzRawValue.image),
      imageContentType: new FormControl(quizzRawValue.imageContentType),
      published: new FormControl(quizzRawValue.published, {
        validators: [Validators.required],
      }),
      publishDate: new FormControl(quizzRawValue.publishDate),
      attempsLimit: new FormControl(quizzRawValue.attempsLimit),
      attempsLimitPeriod: new FormControl(quizzRawValue.attempsLimitPeriod),
      questionCount: new FormControl(quizzRawValue.questionCount),
      passingScore: new FormControl(quizzRawValue.passingScore, {
        validators: [Validators.min(1), Validators.max(100)],
      }),
      user: new FormControl(quizzRawValue.user, {
        validators: [Validators.required],
      }),
      questions: new FormArray<QuestionFormGroup>((quizzRawValue.questions ?? []).map(question => this.initQuestion(question))),
    });
  }

  initQuestion(questionRawValue: IQuestion): QuestionFormGroup {
    return new FormGroup<QuestionFormGroupContent>({
      id: new FormControl({ value: questionRawValue.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      statement: new FormControl(questionRawValue.statement, { validators: [Validators.required] }),
      index: new FormControl(questionRawValue.index, { validators: [Validators.required] }),
      correctOptionIndex: new FormControl(questionRawValue.correctOptionIndex, { validators: [Validators.required] }),
      options: new FormArray<OptionFormGroup>((questionRawValue.options ?? []).map(option => this.initOption(option))),
    });
  }

  initOption(optionRawValue: IOption): OptionFormGroup {
    return new FormGroup<OptionFormGroupContent>({
      id: new FormControl({ value: optionRawValue.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      statement: new FormControl(optionRawValue.statement, { validators: [Validators.required] }),
      index: new FormControl(optionRawValue.index, { validators: [Validators.required] }),
    });
  }

  getQuizz(form: QuizzFormGroup): IQuizz | NewQuizz {
    return this.convertQuizzRawValueToQuizz(form.getRawValue() as QuizzFormRawValue | NewQuizzFormRawValue);
  }

  resetForm(form: QuizzFormGroup, quizz: QuizzFormGroupInput): void {
    const quizzRawValue = this.convertQuizzToQuizzRawValue({ ...this.getFormDefaults(), ...quizz });
    form.reset(
      {
        ...quizzRawValue,
        id: { value: quizzRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuizzFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      allowBack: false,
      allowReview: false,
      keepAnswersSecret: false,
      published: false,
      // publishDate: currentTime, filled only by according service
    };
  }

  private convertQuizzRawValueToQuizz(rawQuizz: QuizzFormRawValue | NewQuizzFormRawValue): IQuizz | NewQuizz {
    return {
      ...rawQuizz,
      publishDate: dayjs(rawQuizz.publishDate, DATE_TIME_FORMAT),
    };
  }

  private convertQuizzToQuizzRawValue(
    quizz: IQuizz | (Partial<NewQuizz> & QuizzFormDefaults),
  ): QuizzFormRawValue | PartialWithRequiredKeyOf<NewQuizzFormRawValue> {
    return {
      ...quizz,
      publishDate: quizz.publishDate ? quizz.publishDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
