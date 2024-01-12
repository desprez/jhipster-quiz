import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttemptAnswer, NewAttemptAnswer } from '../attempt-answer.model';

export type PartialUpdateAttemptAnswer = Partial<IAttemptAnswer> & Pick<IAttemptAnswer, 'id'>;

type RestOf<T extends IAttemptAnswer | NewAttemptAnswer> = Omit<T, 'started' | 'ended'> & {
  started?: string | null;
  ended?: string | null;
};

export type RestAttemptAnswer = RestOf<IAttemptAnswer>;

export type NewRestAttemptAnswer = RestOf<NewAttemptAnswer>;

export type PartialUpdateRestAttemptAnswer = RestOf<PartialUpdateAttemptAnswer>;

export type EntityResponseType = HttpResponse<IAttemptAnswer>;
export type EntityArrayResponseType = HttpResponse<IAttemptAnswer[]>;

@Injectable({ providedIn: 'root' })
export class AttemptAnswerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attempt-answers');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestAttemptAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAttemptAnswer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getAttemptAnswerIdentifier(attemptAnswer: Pick<IAttemptAnswer, 'id'>): string {
    return attemptAnswer.id;
  }

  compareAttemptAnswer(o1: Pick<IAttemptAnswer, 'id'> | null, o2: Pick<IAttemptAnswer, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttemptAnswerIdentifier(o1) === this.getAttemptAnswerIdentifier(o2) : o1 === o2;
  }

  addAttemptAnswerToCollectionIfMissing<Type extends Pick<IAttemptAnswer, 'id'>>(
    attemptAnswerCollection: Type[],
    ...attemptAnswersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attemptAnswers: Type[] = attemptAnswersToCheck.filter(isPresent);
    if (attemptAnswers.length > 0) {
      const attemptAnswerCollectionIdentifiers = attemptAnswerCollection.map(
        attemptAnswerItem => this.getAttemptAnswerIdentifier(attemptAnswerItem)!,
      );
      const attemptAnswersToAdd = attemptAnswers.filter(attemptAnswerItem => {
        const attemptAnswerIdentifier = this.getAttemptAnswerIdentifier(attemptAnswerItem);
        if (attemptAnswerCollectionIdentifiers.includes(attemptAnswerIdentifier)) {
          return false;
        }
        attemptAnswerCollectionIdentifiers.push(attemptAnswerIdentifier);
        return true;
      });
      return [...attemptAnswersToAdd, ...attemptAnswerCollection];
    }
    return attemptAnswerCollection;
  }

  protected convertDateFromClient<T extends IAttemptAnswer | NewAttemptAnswer | PartialUpdateAttemptAnswer>(attemptAnswer: T): RestOf<T> {
    return {
      ...attemptAnswer,
      started: attemptAnswer.started?.toJSON() ?? null,
      ended: attemptAnswer.ended?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAttemptAnswer: RestAttemptAnswer): IAttemptAnswer {
    return {
      ...restAttemptAnswer,
      started: restAttemptAnswer.started ? dayjs(restAttemptAnswer.started) : undefined,
      ended: restAttemptAnswer.ended ? dayjs(restAttemptAnswer.ended) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAttemptAnswer>): HttpResponse<IAttemptAnswer> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAttemptAnswer[]>): HttpResponse<IAttemptAnswer[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
