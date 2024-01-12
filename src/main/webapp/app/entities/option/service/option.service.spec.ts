import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOption } from '../option.model';
import { sampleWithRequiredData, sampleWithPartialData, sampleWithFullData } from '../option.test-samples';

import { OptionService } from './option.service';

const requireRestSample: IOption = {
  ...sampleWithRequiredData,
};

describe('Option Service', () => {
  let service: OptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IOption | IOption[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OptionService);
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

    it('should return a list of Option', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    describe('addOptionToCollectionIfMissing', () => {
      it('should add a Option to an empty array', () => {
        const option: IOption = sampleWithRequiredData;
        expectedResult = service.addOptionToCollectionIfMissing([], option);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(option);
      });

      it('should not add a Option to an array that contains it', () => {
        const option: IOption = sampleWithRequiredData;
        const optionCollection: IOption[] = [
          {
            ...option,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOptionToCollectionIfMissing(optionCollection, option);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Option to an array that doesn't contain it", () => {
        const option: IOption = sampleWithRequiredData;
        const optionCollection: IOption[] = [sampleWithPartialData];
        expectedResult = service.addOptionToCollectionIfMissing(optionCollection, option);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(option);
      });

      it('should add only unique Option to an array', () => {
        const optionArray: IOption[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const optionCollection: IOption[] = [sampleWithRequiredData];
        expectedResult = service.addOptionToCollectionIfMissing(optionCollection, ...optionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const option: IOption = sampleWithRequiredData;
        const option2: IOption = sampleWithPartialData;
        expectedResult = service.addOptionToCollectionIfMissing([], option, option2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(option);
        expect(expectedResult).toContain(option2);
      });

      it('should accept null and undefined values', () => {
        const option: IOption = sampleWithRequiredData;
        expectedResult = service.addOptionToCollectionIfMissing([], null, option, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(option);
      });

      it('should return initial array if no Option is added', () => {
        const optionCollection: IOption[] = [sampleWithRequiredData];
        expectedResult = service.addOptionToCollectionIfMissing(optionCollection, undefined, null);
        expect(expectedResult).toEqual(optionCollection);
      });
    });

    describe('compareOption', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOption(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareOption(entity1, entity2);
        const compareResult2 = service.compareOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareOption(entity1, entity2);
        const compareResult2 = service.compareOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareOption(entity1, entity2);
        const compareResult2 = service.compareOption(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
