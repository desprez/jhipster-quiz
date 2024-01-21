import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../attempt.test-samples';

import { AttemptFormService } from './attempt-form.service';

describe('Attempt Form Service', () => {
  let service: AttemptFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttemptFormService);
  });

  describe('Service methods', () => {
    describe('createAttemptFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttemptFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            score: expect.any(Object),
            started: expect.any(Object),
            ended: expect.any(Object),
            quizz: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IAttempt should create a new form with FormGroup', () => {
        const formGroup = service.createAttemptFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            score: expect.any(Object),
            started: expect.any(Object),
            ended: expect.any(Object),
            quizz: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getAttempt', () => {
      it('should return NewAttempt for default Attempt initial value', () => {
        const formGroup = service.createAttemptFormGroup(sampleWithNewData);

        const attempt = service.getAttempt(formGroup) as any;

        expect(attempt).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttempt for empty Attempt initial value', () => {
        const formGroup = service.createAttemptFormGroup();

        const attempt = service.getAttempt(formGroup) as any;

        expect(attempt).toMatchObject({});
      });

      it('should return IAttempt', () => {
        const formGroup = service.createAttemptFormGroup(sampleWithRequiredData);

        const attempt = service.getAttempt(formGroup) as any;

        expect(attempt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttempt should not enable id FormControl', () => {
        const formGroup = service.createAttemptFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttempt should disable id FormControl', () => {
        const formGroup = service.createAttemptFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
