import { IQuizz, NewQuizz } from './quizz.model';

export const sampleWithRequiredData: IQuizz = {
  id: 'b015f4a3-59d0-4d7c-9168-b16da068d391',
  title: 'adjourn',
  difficulty: 'MEDIUM',
  category: 'CARTOON',
  questionOrder: 'RANDOM',
  allowBack: true,
  allowReview: true,
  keepAnswersSecret: true,
  published: true,
};

export const sampleWithPartialData: IQuizz = {
  id: 'aad16523-075a-49e4-a55e-cedf20765a7b',
  title: 'and',
  description: 'straight',
  difficulty: 'EASY',
  category: 'ANIMALS',
  questionOrder: 'RANDOM',
  maxAnswerTime: 13265,
  allowBack: false,
  allowReview: false,
  keepAnswersSecret: true,
  published: true,
  passingScore: 79,
};

export const sampleWithFullData: IQuizz = {
  id: 'd57a4759-7abb-43ba-a88c-69c88d593866',
  title: 'before',
  description: 'intestine waveform yahoo',
  difficulty: 'MEDIUM',
  category: 'VIDEO_GAMES',
  questionOrder: 'FIXED',
  maxAnswerTime: 19982,
  allowBack: false,
  allowReview: true,
  keepAnswersSecret: true,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  published: true,
  publishDate: dayjs('2024-01-12T04:40'),
  attempsLimit: 30884,
  attempsLimitPeriod: 'DAY',
  questionCount: 20708,
  passingScore: 30,
};

export const sampleWithNewData: NewQuizz = {
  title: 'champion phooey',
  difficulty: 'MEDIUM',
  category: 'CARTOON',
  questionOrder: 'FIXED',
  allowBack: true,
  allowReview: true,
  keepAnswersSecret: false,
  published: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
