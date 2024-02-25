import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizz, NewQuizz } from '../quizz.model';

export type PartialUpdateQuizz = Partial<IQuizz> & Pick<IQuizz, 'id'>;

type RestOf<T extends IQuizz | NewQuizz> = Omit<T, 'publishDate'> & {
  publishDate?: string | null;
};

export type RestQuizz = RestOf<IQuizz>;

export type NewRestQuizz = RestOf<NewQuizz>;

export type PartialUpdateRestQuizz = RestOf<PartialUpdateQuizz>;

export type EntityResponseType = HttpResponse<IQuizz>;
export type EntityArrayResponseType = HttpResponse<IQuizz[]>;

@Injectable({ providedIn: 'root' })
export class QuizzService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quizzes');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(quizz: NewQuizz): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizz);
    return this.http.post<RestQuizz>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quizz: IQuizz): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizz);
    return this.http
      .put<RestQuizz>(`${this.resourceUrl}/${this.getQuizzIdentifier(quizz)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quizz: PartialUpdateQuizz): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quizz);
    return this.http
      .patch<RestQuizz>(`${this.resourceUrl}/${this.getQuizzIdentifier(quizz)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestQuizz>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuizz[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuizzIdentifier(quizz: Pick<IQuizz, 'id'>): string {
    return quizz.id;
  }

  compareQuizz(o1: Pick<IQuizz, 'id'> | null, o2: Pick<IQuizz, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuizzIdentifier(o1) === this.getQuizzIdentifier(o2) : o1 === o2;
  }

  addQuizzToCollectionIfMissing<Type extends Pick<IQuizz, 'id'>>(
    quizzCollection: Type[],
    ...quizzesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quizzes: Type[] = quizzesToCheck.filter(isPresent);
    if (quizzes.length > 0) {
      const quizzCollectionIdentifiers = quizzCollection.map(quizzItem => this.getQuizzIdentifier(quizzItem)!);
      const quizzesToAdd = quizzes.filter(quizzItem => {
        const quizzIdentifier = this.getQuizzIdentifier(quizzItem);
        if (quizzCollectionIdentifiers.includes(quizzIdentifier)) {
          return false;
        }
        quizzCollectionIdentifiers.push(quizzIdentifier);
        return true;
      });
      return [...quizzesToAdd, ...quizzCollection];
    }
    return quizzCollection;
  }

  protected convertDateFromClient<T extends IQuizz | NewQuizz | PartialUpdateQuizz>(quizz: T): RestOf<T> {
    return {
      ...quizz,
      publishDate: quizz.publishDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restQuizz: RestQuizz): IQuizz {
    return {
      ...restQuizz,
      publishDate: restQuizz.publishDate ? dayjs(restQuizz.publishDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestQuizz>): HttpResponse<IQuizz> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestQuizz[]>): HttpResponse<IQuizz[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
