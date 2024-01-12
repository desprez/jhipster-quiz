import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttempt, NewAttempt } from '../attempt.model';

export type PartialUpdateAttempt = Partial<IAttempt> & Pick<IAttempt, 'id'>;

type RestOf<T extends IAttempt | NewAttempt> = Omit<T, 'started' | 'ended'> & {
  started?: string | null;
  ended?: string | null;
};

export type RestAttempt = RestOf<IAttempt>;

export type NewRestAttempt = RestOf<NewAttempt>;

export type PartialUpdateRestAttempt = RestOf<PartialUpdateAttempt>;

export type EntityResponseType = HttpResponse<IAttempt>;
export type EntityArrayResponseType = HttpResponse<IAttempt[]>;

@Injectable({ providedIn: 'root' })
export class AttemptService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attempts');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(attempt: NewAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attempt);
    return this.http
      .post<RestAttempt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(attempt: IAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attempt);
    return this.http
      .put<RestAttempt>(`${this.resourceUrl}/${this.getAttemptIdentifier(attempt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(attempt: PartialUpdateAttempt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attempt);
    return this.http
      .patch<RestAttempt>(`${this.resourceUrl}/${this.getAttemptIdentifier(attempt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestAttempt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAttempt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttemptIdentifier(attempt: Pick<IAttempt, 'id'>): string {
    return attempt.id;
  }

  compareAttempt(o1: Pick<IAttempt, 'id'> | null, o2: Pick<IAttempt, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttemptIdentifier(o1) === this.getAttemptIdentifier(o2) : o1 === o2;
  }

  addAttemptToCollectionIfMissing<Type extends Pick<IAttempt, 'id'>>(
    attemptCollection: Type[],
    ...attemptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attempts: Type[] = attemptsToCheck.filter(isPresent);
    if (attempts.length > 0) {
      const attemptCollectionIdentifiers = attemptCollection.map(attemptItem => this.getAttemptIdentifier(attemptItem)!);
      const attemptsToAdd = attempts.filter(attemptItem => {
        const attemptIdentifier = this.getAttemptIdentifier(attemptItem);
        if (attemptCollectionIdentifiers.includes(attemptIdentifier)) {
          return false;
        }
        attemptCollectionIdentifiers.push(attemptIdentifier);
        return true;
      });
      return [...attemptsToAdd, ...attemptCollection];
    }
    return attemptCollection;
  }

  protected convertDateFromClient<T extends IAttempt | NewAttempt | PartialUpdateAttempt>(attempt: T): RestOf<T> {
    return {
      ...attempt,
      started: attempt.started?.toJSON() ?? null,
      ended: attempt.ended?.toJSON() ?? null,
    };
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

  protected convertResponseArrayFromServer(res: HttpResponse<RestAttempt[]>): HttpResponse<IAttempt[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
