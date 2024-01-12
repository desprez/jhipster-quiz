import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';

export const quizzResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuizz> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuizzService)
      .find(id)
      .pipe(
        mergeMap((quizz: HttpResponse<IQuizz>) => {
          if (quizz.body) {
            return of(quizz.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default quizzResolve;
