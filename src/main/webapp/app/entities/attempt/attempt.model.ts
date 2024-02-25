import dayjs from 'dayjs/esm';
import { IAttemptAnswer } from 'app/entities/attempt-answer/attempt-answer.model';
import { IQuizz } from 'app/entities/quizz/quizz.model';
import { IUser } from 'app/entities/user/user.model';

export interface IAttempt {
  id: string;
  correctAnswerCount?: number | null;
  wrongAnswerCount?: number | null;
  unansweredCount?: number | null;
  started?: dayjs.Dayjs | null;
  ended?: dayjs.Dayjs | null;
  answers?: Pick<IAttemptAnswer, 'id' | 'correct' | 'question' | 'option'>[] | null;
  quizz?: Pick<IQuizz, 'id' | 'title'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewAttempt = Omit<IAttempt, 'id'> & { id: null };
