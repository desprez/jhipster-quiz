import { IQuestion } from 'app/entities/question/question.model';

export interface IOption {
  id: string;
  statement?: string | null;
  index?: number | null;
  question?: IQuestion | null;
}

export type NewOption = Omit<IOption, 'id'> & { id: null };
