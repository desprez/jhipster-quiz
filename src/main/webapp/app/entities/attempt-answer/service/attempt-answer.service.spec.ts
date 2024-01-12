import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAttemptAnswer } from '../attempt-answer.model';
import { sampleWithRequiredData, sampleWithPartialData, sampleWithFullData } from '../attempt-answer.test-samples';

import { AttemptAnswerService, RestAttemptAnswer } from './attempt-answer.service';

const requireRestSample: RestAttemptAnswer = {
  ...sampleWithRequiredData,
  started: sampleWithRequiredData.started?.toJSON(),
  ended: sampleWithRequiredData.ended?.toJSON(),
};

describe('AttemptAnswer Service', () => {
  let service: AttemptAnswerService;
  let httpMock: HttpTestingController;
  let expectedResult: IAttemptAnswer | IAttemptAnswer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AttemptAnswerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AttemptAnswer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    describe('addAttemptAnswerToCollectionIfMissing', () => {
      it('should add a AttemptAnswer to an empty array', () => {
        const attemptAnswer: IAttemptAnswer = sampleWithRequiredData;
        expectedResult = service.addAttemptAnswerToCollectionIfMissing([], attemptAnswer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attemptAnswer);
      });

      it('should not add a AttemptAnswer to an array that contains it', () => {
        const attemptAnswer: IAttemptAnswer = sampleWithRequiredData;
        const attemptAnswerCollection: IAttemptAnswer[] = [
          {
            ...attemptAnswer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAttemptAnswerToCollectionIfMissing(attemptAnswerCollection, attemptAnswer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AttemptAnswer to an array that doesn't contain it", () => {
        const attemptAnswer: IAttemptAnswer = sampleWithRequiredData;
        const attemptAnswerCollection: IAttemptAnswer[] = [sampleWithPartialData];
        expectedResult = service.addAttemptAnswerToCollectionIfMissing(attemptAnswerCollection, attemptAnswer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attemptAnswer);
      });

      it('should add only unique AttemptAnswer to an array', () => {
        const attemptAnswerArray: IAttemptAnswer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const attemptAnswerCollection: IAttemptAnswer[] = [sampleWithRequiredData];
        expectedResult = service.addAttemptAnswerToCollectionIfMissing(attemptAnswerCollection, ...attemptAnswerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const attemptAnswer: IAttemptAnswer = sampleWithRequiredData;
        const attemptAnswer2: IAttemptAnswer = sampleWithPartialData;
        expectedResult = service.addAttemptAnswerToCollectionIfMissing([], attemptAnswer, attemptAnswer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attemptAnswer);
        expect(expectedResult).toContain(attemptAnswer2);
      });

      it('should accept null and undefined values', () => {
        const attemptAnswer: IAttemptAnswer = sampleWithRequiredData;
        expectedResult = service.addAttemptAnswerToCollectionIfMissing([], null, attemptAnswer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attemptAnswer);
      });

      it('should return initial array if no AttemptAnswer is added', () => {
        const attemptAnswerCollection: IAttemptAnswer[] = [sampleWithRequiredData];
        expectedResult = service.addAttemptAnswerToCollectionIfMissing(attemptAnswerCollection, undefined, null);
        expect(expectedResult).toEqual(attemptAnswerCollection);
      });
    });

    describe('compareAttemptAnswer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAttemptAnswer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareAttemptAnswer(entity1, entity2);
        const compareResult2 = service.compareAttemptAnswer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareAttemptAnswer(entity1, entity2);
        const compareResult2 = service.compareAttemptAnswer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareAttemptAnswer(entity1, entity2);
        const compareResult2 = service.compareAttemptAnswer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
