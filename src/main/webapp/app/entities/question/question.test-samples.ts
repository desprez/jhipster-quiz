import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: '2c88d92f-f47f-4f75-a5d8-8f0e7b435321',
  statement: 'rudely outlet pish',
  index: 31972,
  correctOptionIndex: 18610,
};

export const sampleWithPartialData: IQuestion = {
  id: 'd5d6bcdf-5077-403e-bcf3-8ca0200830be',
  statement: 'ferociously officially',
  index: 15052,
  correctOptionIndex: 18746,
};

export const sampleWithFullData: IQuestion = {
  id: 'cd6e9dfe-836e-4b9a-b4da-1e18341f5152',
  statement: 'fairly absent unsteady',
  index: 23680,
  correctOptionIndex: 9545,
};

export const sampleWithNewData: NewQuestion = {
  statement: 'calmly',
  index: 15025,
  correctOptionIndex: 4303,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
