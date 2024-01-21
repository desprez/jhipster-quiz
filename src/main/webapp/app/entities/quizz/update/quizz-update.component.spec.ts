import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { QuizzService } from '../service/quizz.service';
import { IQuizz } from '../quizz.model';

import { QuizzFormService } from './quizz-form.service';

import { QuizzUpdateComponent } from './quizz-update.component';

describe('Quizz Management Update Component', () => {
  let comp: QuizzUpdateComponent;
  let fixture: ComponentFixture<QuizzUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizzFormService: QuizzFormService;
  let quizzService: QuizzService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QuizzUpdateComponent],
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
      .overrideTemplate(QuizzUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizzUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizzFormService = TestBed.inject(QuizzFormService);
    quizzService = TestBed.inject(QuizzService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const quizz: IQuizz = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const user: IUser = { id: 18755 };
      quizz.user = user;

      const userCollection: IUser[] = [{ id: 26652 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quizz });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quizz: IQuizz = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const user: IUser = { id: 26675 };
      quizz.user = user;

      activatedRoute.data = of({ quizz });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.quizz).toEqual(quizz);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizz>>();
      const quizz = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(quizzFormService, 'getQuizz').mockReturnValue(quizz);
      jest.spyOn(quizzService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizz }));
      saveSubject.complete();

      // THEN
      expect(quizzFormService.getQuizz).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizzService.update).toHaveBeenCalledWith(expect.objectContaining(quizz));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizz>>();
      const quizz = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(quizzFormService, 'getQuizz').mockReturnValue({ id: null });
      jest.spyOn(quizzService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizz: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizz }));
      saveSubject.complete();

      // THEN
      expect(quizzFormService.getQuizz).toHaveBeenCalled();
      expect(quizzService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizz>>();
      const quizz = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(quizzService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizzService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
