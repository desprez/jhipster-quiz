import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttemptAnswer } from '../attempt-answer.model';
import { AttemptAnswerService } from '../service/attempt-answer.service';

export const attemptAnswerResolve = (route: ActivatedRouteSnapshot): Observable<null | IAttemptAnswer> => {
  const id = route.params['id'];
  if (id) {
    return inject(AttemptAnswerService)
      .find(id)
      .pipe(
        mergeMap((attemptAnswer: HttpResponse<IAttemptAnswer>) => {
          if (attemptAnswer.body) {
            return of(attemptAnswer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default attemptAnswerResolve;
