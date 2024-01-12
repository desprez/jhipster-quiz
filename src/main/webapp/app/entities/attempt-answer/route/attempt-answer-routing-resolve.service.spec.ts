import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAttemptAnswer } from '../attempt-answer.model';
import { AttemptAnswerService } from '../service/attempt-answer.service';

import attemptAnswerResolve from './attempt-answer-routing-resolve.service';

describe('AttemptAnswer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: AttemptAnswerService;
  let resultAttemptAnswer: IAttemptAnswer | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(AttemptAnswerService);
    resultAttemptAnswer = undefined;
  });

  describe('resolve', () => {
    it('should return IAttemptAnswer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        attemptAnswerResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAttemptAnswer = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
      expect(resultAttemptAnswer).toEqual({ id: '9fec3727-3421-4967-b213-ba36557ca194' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        attemptAnswerResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAttemptAnswer = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAttemptAnswer).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IAttemptAnswer>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        attemptAnswerResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAttemptAnswer = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
      expect(resultAttemptAnswer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
