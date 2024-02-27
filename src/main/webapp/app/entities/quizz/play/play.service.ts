import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

import { IQuizz } from '../quizz.model';
import { IAttempt, NewAttempt } from 'app/entities/attempt/attempt.model';

export type PartialUpdateQuizz = Partial<IQuizz> & Pick<IQuizz, 'id'>;

export type EntityResponseType = HttpResponse<IQuizz>;
export type EntityArrayResponseType = HttpResponse<IQuizz[]>;

type RestOf<T extends IAttempt | NewAttempt> = Omit<T, 'started' | 'ended'> & {
  started?: string | null;
  ended?: string | null;
};

export type RestAttempt = RestOf<IAttempt>;

@Injectable({ providedIn: 'root' })
export class PlayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/play');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  play(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestAttempt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  evaluate(attempt: IAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attempt);
    return this.http
      .put<RestAttempt>(`${this.resourceUrl}/evaluate/${this.getAttemptIdentifier(attempt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  protected convertDateFromClient<T extends IAttempt>(attempt: T): RestOf<T> {
    return {
      ...attempt,
      started: attempt.started?.toJSON() ?? null,
      ended: attempt.ended?.toJSON() ?? null,
    };
  }

  getAttemptIdentifier(attempt: Pick<IAttempt, 'id'>): string {
    return attempt.id;
  }

  protected convertDateFromServer(restAttempt: RestAttempt): IAttempt {
    return {
      ...restAttempt,
      started: restAttempt.started ? dayjs(restAttempt.started) : undefined,
      ended: restAttempt.ended ? dayjs(restAttempt.ended) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAttempt>): HttpResponse<IAttempt> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }
}
