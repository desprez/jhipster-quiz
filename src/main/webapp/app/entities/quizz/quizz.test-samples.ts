import { IQuizz, NewQuizz } from './quizz.model';

export const sampleWithRequiredData: IQuizz = {
  id: '1feb015f-4a35-49d0-8d7c-168b16da068d',
  title: 'surprisingly gladly',
  difficulty: 'MEDIUM',
  category: 'FILM',
  published: true,
  questionOrder: 'RANDOM',
  maxAnswerTime: 5645,
  rollbackAllowed: false,
};

export const sampleWithPartialData: IQuizz = {
  id: 'ad165230-75a9-4e45-85ec-edf20765a7b8',
  title: 'abaft straight excepting',
  difficulty: 'EASY',
  category: 'COMICS',
  published: true,
  questionOrder: 'RANDOM',
  maxAnswerTime: 22358,
  rollbackAllowed: true,
};

export const sampleWithFullData: IQuizz = {
  id: '7597abb3-ba88-4c69-9c88-d59386680343',
  title: 'overreact',
  description: 'maximize',
  difficulty: 'MEDIUM',
  category: 'CARTOON',
  published: false,
  questionOrder: 'RANDOM',
  maxAnswerTime: 13628,
  rollbackAllowed: true,
};

export const sampleWithNewData: NewQuizz = {
  title: 'shoddy driving',
  difficulty: 'MEDIUM',
  category: 'FILM',
  published: true,
  questionOrder: 'RANDOM',
  maxAnswerTime: 1232,
  rollbackAllowed: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
