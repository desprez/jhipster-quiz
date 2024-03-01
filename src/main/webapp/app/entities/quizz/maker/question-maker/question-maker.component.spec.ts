import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionMakerComponent } from './question-maker.component';

describe('QuestionMakerComponent', () => {
  let component: QuestionMakerComponent;
  let fixture: ComponentFixture<QuestionMakerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionMakerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(QuestionMakerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
