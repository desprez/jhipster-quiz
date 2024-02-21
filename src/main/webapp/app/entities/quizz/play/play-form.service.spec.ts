import { TestBed } from '@angular/core/testing';

import { PlayFormService } from './play-form.service';

describe('PlayFormService', () => {
  let service: PlayFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlayFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
