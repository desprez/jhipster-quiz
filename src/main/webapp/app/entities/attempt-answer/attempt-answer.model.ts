import dayjs from 'dayjs/esm';
import { IQuestion } from 'app/entities/question/question.model';
import { IOption } from 'app/entities/option/option.model';
import { IAttempt } from 'app/entities/attempt/attempt.model';

export interface IAttemptAnswer {
  id: string;
  started?: dayjs.Dayjs | null;
  ended?: dayjs.Dayjs | null;
  correct?: boolean | null;
  question?: IQuestion | null;
  option?: IOption | null;
  attempt?: IAttempt | null;
}

export type NewAttemptAnswer = Omit<IAttemptAnswer, 'id'> & { id: null };
