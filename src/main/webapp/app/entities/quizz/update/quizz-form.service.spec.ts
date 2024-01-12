import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../quizz.test-samples';

import { QuizzFormService } from './quizz-form.service';

describe('Quizz Form Service', () => {
  let service: QuizzFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuizzFormService);
  });

  describe('Service methods', () => {
    describe('createQuizzFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuizzFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            difficulty: expect.any(Object),
            category: expect.any(Object),
            published: expect.any(Object),
            questionOrder: expect.any(Object),
            maxAnswerTime: expect.any(Object),
            rollbackAllowed: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IQuizz should create a new form with FormGroup', () => {
        const formGroup = service.createQuizzFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            difficulty: expect.any(Object),
            category: expect.any(Object),
            published: expect.any(Object),
            questionOrder: expect.any(Object),
            maxAnswerTime: expect.any(Object),
            rollbackAllowed: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuizz', () => {
      it('should return NewQuizz for default Quizz initial value', () => {
        const formGroup = service.createQuizzFormGroup(sampleWithNewData);

        const quizz = service.getQuizz(formGroup) as any;

        expect(quizz).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuizz for empty Quizz initial value', () => {
        const formGroup = service.createQuizzFormGroup();

        const quizz = service.getQuizz(formGroup) as any;

        expect(quizz).toMatchObject({});
      });

      it('should return IQuizz', () => {
        const formGroup = service.createQuizzFormGroup(sampleWithRequiredData);

        const quizz = service.getQuizz(formGroup) as any;

        expect(quizz).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuizz should not enable id FormControl', () => {
        const formGroup = service.createQuizzFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuizz should disable id FormControl', () => {
        const formGroup = service.createQuizzFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
