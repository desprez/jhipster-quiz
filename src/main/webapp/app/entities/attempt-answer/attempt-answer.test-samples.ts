import dayjs from 'dayjs/esm';

import { IAttemptAnswer, NewAttemptAnswer } from './attempt-answer.model';

export const sampleWithRequiredData: IAttemptAnswer = {
  id: 'b3253c9d-2756-41b9-961a-6bcf11390ede',
  started: dayjs('2024-01-12T00:31'),
  ended: dayjs('2024-01-12T06:56'),
};

export const sampleWithPartialData: IAttemptAnswer = {
  id: '994ed486-0267-4ec8-bd0b-bf39f1c8c562',
  started: dayjs('2024-01-12T07:28'),
  ended: dayjs('2024-01-11T20:06'),
};

export const sampleWithFullData: IAttemptAnswer = {
  id: '3a46f537-323d-4069-9a36-05c55f7bea4f',
  started: dayjs('2024-01-12T11:16'),
  ended: dayjs('2024-01-12T16:21'),
};

export const sampleWithNewData: NewAttemptAnswer = {
  started: dayjs('2024-01-11T21:15'),
  ended: dayjs('2024-01-12T14:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
