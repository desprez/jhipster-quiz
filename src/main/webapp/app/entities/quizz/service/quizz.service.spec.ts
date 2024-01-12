import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuizz } from '../quizz.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../quizz.test-samples';

import { QuizzService } from './quizz.service';

const requireRestSample: IQuizz = {
  ...sampleWithRequiredData,
};

describe('Quizz Service', () => {
  let service: QuizzService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuizz | IQuizz[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuizzService);
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

    it('should create a Quizz', () => {
      const quizz = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quizz).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Quizz', () => {
      const quizz = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quizz).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Quizz', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Quizz', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Quizz', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuizzToCollectionIfMissing', () => {
      it('should add a Quizz to an empty array', () => {
        const quizz: IQuizz = sampleWithRequiredData;
        expectedResult = service.addQuizzToCollectionIfMissing([], quizz);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizz);
      });

      it('should not add a Quizz to an array that contains it', () => {
        const quizz: IQuizz = sampleWithRequiredData;
        const quizzCollection: IQuizz[] = [
          {
            ...quizz,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuizzToCollectionIfMissing(quizzCollection, quizz);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Quizz to an array that doesn't contain it", () => {
        const quizz: IQuizz = sampleWithRequiredData;
        const quizzCollection: IQuizz[] = [sampleWithPartialData];
        expectedResult = service.addQuizzToCollectionIfMissing(quizzCollection, quizz);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizz);
      });

      it('should add only unique Quizz to an array', () => {
        const quizzArray: IQuizz[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quizzCollection: IQuizz[] = [sampleWithRequiredData];
        expectedResult = service.addQuizzToCollectionIfMissing(quizzCollection, ...quizzArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quizz: IQuizz = sampleWithRequiredData;
        const quizz2: IQuizz = sampleWithPartialData;
        expectedResult = service.addQuizzToCollectionIfMissing([], quizz, quizz2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quizz);
        expect(expectedResult).toContain(quizz2);
      });

      it('should accept null and undefined values', () => {
        const quizz: IQuizz = sampleWithRequiredData;
        expectedResult = service.addQuizzToCollectionIfMissing([], null, quizz, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quizz);
      });

      it('should return initial array if no Quizz is added', () => {
        const quizzCollection: IQuizz[] = [sampleWithRequiredData];
        expectedResult = service.addQuizzToCollectionIfMissing(quizzCollection, undefined, null);
        expect(expectedResult).toEqual(quizzCollection);
      });
    });

    describe('compareQuizz', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuizz(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareQuizz(entity1, entity2);
        const compareResult2 = service.compareQuizz(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareQuizz(entity1, entity2);
        const compareResult2 = service.compareQuizz(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareQuizz(entity1, entity2);
        const compareResult2 = service.compareQuizz(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
