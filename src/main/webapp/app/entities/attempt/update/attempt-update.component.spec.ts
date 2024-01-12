import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IQuizz } from 'app/entities/quizz/quizz.model';
import { QuizzService } from 'app/entities/quizz/service/quizz.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAttempt } from '../attempt.model';
import { AttemptService } from '../service/attempt.service';
import { AttemptFormService } from './attempt-form.service';

import { AttemptUpdateComponent } from './attempt-update.component';

describe('Attempt Management Update Component', () => {
  let comp: AttemptUpdateComponent;
  let fixture: ComponentFixture<AttemptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attemptFormService: AttemptFormService;
  let attemptService: AttemptService;
  let quizzService: QuizzService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AttemptUpdateComponent],
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
      .overrideTemplate(AttemptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttemptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attemptFormService = TestBed.inject(AttemptFormService);
    attemptService = TestBed.inject(AttemptService);
    quizzService = TestBed.inject(QuizzService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quizz query and add missing value', () => {
      const attempt: IAttempt = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const quizz: IQuizz = { id: '6389850f-8f43-48e4-b6c7-f3132f3e58aa' };
      attempt.quizz = quizz;

      const quizzCollection: IQuizz[] = [{ id: '478be149-803a-4d24-993f-350f7be8f105' }];
      jest.spyOn(quizzService, 'query').mockReturnValue(of(new HttpResponse({ body: quizzCollection })));
      const additionalQuizzes = [quizz];
      const expectedCollection: IQuizz[] = [...additionalQuizzes, ...quizzCollection];
      jest.spyOn(quizzService, 'addQuizzToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attempt });
      comp.ngOnInit();

      expect(quizzService.query).toHaveBeenCalled();
      expect(quizzService.addQuizzToCollectionIfMissing).toHaveBeenCalledWith(
        quizzCollection,
        ...additionalQuizzes.map(expect.objectContaining),
      );
      expect(comp.quizzesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const attempt: IAttempt = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const user: IUser = { id: 12657 };
      attempt.user = user;

      const userCollection: IUser[] = [{ id: 7674 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attempt });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const attempt: IAttempt = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const quizz: IQuizz = { id: '16794828-d817-4d40-91b8-a9565e1a6ea2' };
      attempt.quizz = quizz;
      const user: IUser = { id: 25358 };
      attempt.user = user;

      activatedRoute.data = of({ attempt });
      comp.ngOnInit();

      expect(comp.quizzesSharedCollection).toContain(quizz);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.attempt).toEqual(attempt);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttempt>>();
      const attempt = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(attemptFormService, 'getAttempt').mockReturnValue(attempt);
      jest.spyOn(attemptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attempt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attempt }));
      saveSubject.complete();

      // THEN
      expect(attemptFormService.getAttempt).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attemptService.update).toHaveBeenCalledWith(expect.objectContaining(attempt));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttempt>>();
      const attempt = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(attemptFormService, 'getAttempt').mockReturnValue({ id: null });
      jest.spyOn(attemptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attempt: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attempt }));
      saveSubject.complete();

      // THEN
      expect(attemptFormService.getAttempt).toHaveBeenCalled();
      expect(attemptService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttempt>>();
      const attempt = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(attemptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attempt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attemptService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
