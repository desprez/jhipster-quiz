import dayjs from 'dayjs/esm';

import { IAttemptAnswer, NewAttemptAnswer } from './attempt-answer.model';

export const sampleWithRequiredData: IAttemptAnswer = {
  id: 'b3253c9d-2756-41b9-961a-6bcf11390ede',
  started: dayjs('2024-01-12T00:31'),
};

export const sampleWithPartialData: IAttemptAnswer = {
  id: '994ed486-0267-4ec8-bd0b-bf39f1c8c562',
  started: dayjs('2024-01-12T07:28'),
  ended: dayjs('2024-01-12T14:24'),
  correct: false,
};

export const sampleWithFullData: IAttemptAnswer = {
  id: 'f3a46f53-7323-4d06-b9a3-605c55f7bea4',
  started: dayjs('2024-01-12T12:12'),
  ended: dayjs('2024-01-12T11:16'),
  correct: false,
};

export const sampleWithNewData: NewAttemptAnswer = {
  started: dayjs('2024-01-12T16:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
