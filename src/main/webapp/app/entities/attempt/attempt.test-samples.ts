import dayjs from 'dayjs/esm';

import { IAttempt, NewAttempt } from './attempt.model';

export const sampleWithRequiredData: IAttempt = {
  id: 'c528df98-857c-403b-b0e8-45e7a57f61a9',
  score: 25701,
  started: dayjs('2024-01-11T19:33'),
};

export const sampleWithPartialData: IAttempt = {
  id: '57a232a9-086e-4b07-8c22-521b0e486dbe',
  score: 20244,
  started: dayjs('2024-01-11T23:14'),
  ended: dayjs('2024-01-12T14:33'),
};

export const sampleWithFullData: IAttempt = {
  id: '6f685949-4844-437b-88bb-7c92f0c67182',
  score: 14355,
  started: dayjs('2024-01-12T18:45'),
  ended: dayjs('2024-01-11T23:34'),
};

export const sampleWithNewData: NewAttempt = {
  score: 18642,
  started: dayjs('2024-01-11T20:49'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
