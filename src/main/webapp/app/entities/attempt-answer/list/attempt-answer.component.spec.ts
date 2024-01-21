import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AttemptAnswerService } from '../service/attempt-answer.service';

import { AttemptAnswerComponent } from './attempt-answer.component';

describe('AttemptAnswer Management Component', () => {
  let comp: AttemptAnswerComponent;
  let fixture: ComponentFixture<AttemptAnswerComponent>;
  let service: AttemptAnswerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'attempt-answer', component: AttemptAnswerComponent }]),
        HttpClientTestingModule,
        AttemptAnswerComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(AttemptAnswerComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttemptAnswerComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttemptAnswerService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.attemptAnswers?.[0]).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
  });

  describe('trackId', () => {
    it('Should forward to attemptAnswerService', () => {
      const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(service, 'getAttemptAnswerIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAttemptAnswerIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
