import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuizz } from 'app/entities/quizz/quizz.model';
import { QuizzService } from 'app/entities/quizz/service/quizz.service';
import { QuestionService } from '../service/question.service';
import { IQuestion } from '../question.model';
import { QuestionFormService } from './question-form.service';

import { QuestionUpdateComponent } from './question-update.component';

describe('Question Management Update Component', () => {
  let comp: QuestionUpdateComponent;
  let fixture: ComponentFixture<QuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionFormService: QuestionFormService;
  let questionService: QuestionService;
  let quizzService: QuizzService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuestionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionFormService = TestBed.inject(QuestionFormService);
    questionService = TestBed.inject(QuestionService);
    quizzService = TestBed.inject(QuizzService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quizz query and add missing value', () => {
      const question: IQuestion = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const quizz: IQuizz = { id: '40ee05ff-34d6-4ccd-9bfe-ebafa4c78fce' };
      question.quizz = quizz;

      const quizzCollection: IQuizz[] = [{ id: '4a9fda5b-c496-4921-9792-b973530a4b26' }];
      jest.spyOn(quizzService, 'query').mockReturnValue(of(new HttpResponse({ body: quizzCollection })));
      const additionalQuizzes = [quizz];
      const expectedCollection: IQuizz[] = [...additionalQuizzes, ...quizzCollection];
      jest.spyOn(quizzService, 'addQuizzToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(quizzService.query).toHaveBeenCalled();
      expect(quizzService.addQuizzToCollectionIfMissing).toHaveBeenCalledWith(
        quizzCollection,
        ...additionalQuizzes.map(expect.objectContaining),
      );
      expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const question: IQuestion = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const quizz: IQuizz = { id: '947e9580-efcd-406b-9a80-01ef7fd508ee' };
      question.quizz = quizz;

      activatedRoute.data = of({ question });
      comp.ngOnInit();

      expect(comp.quizzesSharedCollection).toContain(quizz);
      expect(comp.question).toEqual(question);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(questionFormService, 'getQuestion').mockReturnValue(question);
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionFormService.getQuestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionService.update).toHaveBeenCalledWith(expect.objectContaining(question));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(questionFormService, 'getQuestion').mockReturnValue({ id: null });
      jest.spyOn(questionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: question }));
      saveSubject.complete();

      // THEN
      expect(questionFormService.getQuestion).toHaveBeenCalled();
      expect(questionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestion>>();
      const question = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(questionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ question });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuizz', () => {
      it('Should forward to quizzService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(quizzService, 'compareQuizz');
        comp.compareQuizz(entity, entity2);
        expect(quizzService.compareQuizz).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
