import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OptionMakerComponent } from './option-maker.component';

describe('OptionMakerComponent', () => {
  let component: OptionMakerComponent;
  let fixture: ComponentFixture<OptionMakerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OptionMakerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(OptionMakerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
