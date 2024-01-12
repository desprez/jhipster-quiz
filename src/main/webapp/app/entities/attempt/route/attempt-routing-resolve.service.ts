import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttempt } from '../attempt.model';
import { AttemptService } from '../service/attempt.service';

export const attemptResolve = (route: ActivatedRouteSnapshot): Observable<null | IAttempt> => {
  const id = route.params['id'];
  if (id) {
    return inject(AttemptService)
      .find(id)
      .pipe(
        mergeMap((attempt: HttpResponse<IAttempt>) => {
          if (attempt.body) {
            return of(attempt.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default attemptResolve;
