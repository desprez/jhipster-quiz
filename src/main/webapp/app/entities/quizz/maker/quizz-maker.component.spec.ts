import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuizzMakerComponent } from './quizz-maker.component';

describe('QuizzMakerComponent', () => {
  let component: QuizzMakerComponent;
  let fixture: ComponentFixture<QuizzMakerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizzMakerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(QuizzMakerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
