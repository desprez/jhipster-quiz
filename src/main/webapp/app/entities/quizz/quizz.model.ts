import { IQuestion } from 'app/entities/question/question.model';
import { IUser } from 'app/entities/user/user.model';
import { Difficulty } from 'app/entities/enumerations/difficulty.model';
import { Category } from 'app/entities/enumerations/category.model';
import { DisplayOrder } from 'app/entities/enumerations/display-order.model';

export interface IQuizz {
  id: string;
  title?: string | null;
  description?: string | null;
  difficulty?: keyof typeof Difficulty | null;
  category?: keyof typeof Category | null;
  questionOrder?: keyof typeof DisplayOrder | null;
  maxAnswerTime?: number | null;
  allowBack?: boolean | null;
  allowReview?: boolean | null;
  secretGoodAnwers?: boolean | null;
  image?: string | null;
  imageContentType?: string | null;
  published?: boolean | null;
  questions?: Pick<IQuestion, 'id' | 'statement'>[] | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  questionCount?: number | null;
}

export type NewQuizz = Omit<IQuizz, 'id'> & { id: null };
