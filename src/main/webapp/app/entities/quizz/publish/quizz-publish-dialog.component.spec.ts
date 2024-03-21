import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuizzPublishDialogComponent } from './quizz-publish-dialog.component';

describe('QuizzPublishDialogComponent', () => {
  let component: QuizzPublishDialogComponent;
  let fixture: ComponentFixture<QuizzPublishDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizzPublishDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(QuizzPublishDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
