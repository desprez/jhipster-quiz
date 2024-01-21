import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAttempt, NewAttempt } from '../attempt.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttempt for edit and NewAttemptFormGroupInput for create.
 */
type AttemptFormGroupInput = IAttempt | PartialWithRequiredKeyOf<NewAttempt>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAttempt | NewAttempt> = Omit<T, 'started' | 'ended'> & {
  started?: string | null;
  ended?: string | null;
};

type AttemptFormRawValue = FormValueOf<IAttempt>;

type NewAttemptFormRawValue = FormValueOf<NewAttempt>;

type AttemptFormDefaults = Pick<NewAttempt, 'id' | 'started' | 'ended'>;

type AttemptFormGroupContent = {
  id: FormControl<AttemptFormRawValue['id'] | NewAttempt['id']>;
  score: FormControl<AttemptFormRawValue['score']>;
  started: FormControl<AttemptFormRawValue['started']>;
  ended: FormControl<AttemptFormRawValue['ended']>;
  quizz: FormControl<AttemptFormRawValue['quizz']>;
  user: FormControl<AttemptFormRawValue['user']>;
};

export type AttemptFormGroup = FormGroup<AttemptFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttemptFormService {
  createAttemptFormGroup(attempt: AttemptFormGroupInput = { id: null }): AttemptFormGroup {
    const attemptRawValue = this.convertAttemptToAttemptRawValue({
      ...this.getFormDefaults(),
      ...attempt,
    });
    return new FormGroup<AttemptFormGroupContent>({
      id: new FormControl(
        { value: attemptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      score: new FormControl(attemptRawValue.score, {
        validators: [Validators.required, Validators.min(0)],
      }),
      started: new FormControl(attemptRawValue.started, {
        validators: [Validators.required],
      }),
      ended: new FormControl(attemptRawValue.ended),
      quizz: new FormControl(attemptRawValue.quizz, {
        validators: [Validators.required],
      }),
      user: new FormControl(attemptRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getAttempt(form: AttemptFormGroup): IAttempt | NewAttempt {
    return this.convertAttemptRawValueToAttempt(form.getRawValue() as AttemptFormRawValue | NewAttemptFormRawValue);
  }

  resetForm(form: AttemptFormGroup, attempt: AttemptFormGroupInput): void {
    const attemptRawValue = this.convertAttemptToAttemptRawValue({ ...this.getFormDefaults(), ...attempt });
    form.reset(
      {
        ...attemptRawValue,
        id: { value: attemptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AttemptFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      started: currentTime,
      ended: currentTime,
    };
  }

  private convertAttemptRawValueToAttempt(rawAttempt: AttemptFormRawValue | NewAttemptFormRawValue): IAttempt | NewAttempt {
    return {
      ...rawAttempt,
      started: dayjs(rawAttempt.started, DATE_TIME_FORMAT),
      ended: dayjs(rawAttempt.ended, DATE_TIME_FORMAT),
    };
  }

  private convertAttemptToAttemptRawValue(
    attempt: IAttempt | (Partial<NewAttempt> & AttemptFormDefaults),
  ): AttemptFormRawValue | PartialWithRequiredKeyOf<NewAttemptFormRawValue> {
    return {
      ...attempt,
      started: attempt.started ? attempt.started.format(DATE_TIME_FORMAT) : undefined,
      ended: attempt.ended ? attempt.ended.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
