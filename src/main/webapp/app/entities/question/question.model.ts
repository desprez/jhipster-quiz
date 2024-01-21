import { IOption } from 'app/entities/option/option.model';
import { IQuizz } from 'app/entities/quizz/quizz.model';

export interface IQuestion {
  id: string;
  statement?: string | null;
  index?: number | null;
  correctOptionIndex?: number | null;
  options?: IOption[] | null;
  quizz?: IQuizz | null;
}

export type NewQuestion = Omit<IQuestion, 'id'> & { id: null };
