import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuizz, NewQuizz } from '../quizz.model';

export type PartialUpdateQuizz = Partial<IQuizz> & Pick<IQuizz, 'id'>;

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
    return this.http.post<IQuizz>(this.resourceUrl, quizz, { observe: 'response' });
  }

  update(quizz: IQuizz): Observable<EntityResponseType> {
    return this.http.put<IQuizz>(`${this.resourceUrl}/${this.getQuizzIdentifier(quizz)}`, quizz, { observe: 'response' });
  }

  partialUpdate(quizz: PartialUpdateQuizz): Observable<EntityResponseType> {
    return this.http.patch<IQuizz>(`${this.resourceUrl}/${this.getQuizzIdentifier(quizz)}`, quizz, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IQuizz>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuizz[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
