import { IOption, NewOption } from './option.model';

export const sampleWithRequiredData: IOption = {
  id: '53cbf59e-a4c7-458e-a1a3-82c6a64fe109',
  statement: 'oof minus',
  index: 12440,
};

export const sampleWithPartialData: IOption = {
  id: 'a4e13993-cd4a-4e48-831e-d12ae7a0701a',
  statement: 'repeal avaricious of',
  index: 32265,
};

export const sampleWithFullData: IOption = {
  id: 'c4be54a7-eebf-4e04-9fc7-877888d09eb1',
  statement: 'atop ick motivate',
  index: 20664,
};

export const sampleWithNewData: NewOption = {
  statement: 'acidly cobweb',
  index: 27697,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
