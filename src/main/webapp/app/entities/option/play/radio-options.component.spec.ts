import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlayRadioOptionsComponent } from './radio-options.component';

describe('PlayRadioOptionsComponent', () => {
  let component: PlayRadioOptionsComponent;
  let fixture: ComponentFixture<PlayRadioOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayRadioOptionsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PlayRadioOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
