import { Component, OnDestroy } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { QuizzPlayComponent } from './quizz-play.component';

@Component({
  standalone: true,
  selector: 'jhi-modal-container',
  template: '',
})
export class ModalContainerComponent implements OnDestroy {
  destroy = new Subject<any>();
  currentDialog: NgbModalRef | undefined;

  constructor(
    private modalService: NgbModal,
    route: ActivatedRoute,
    router: Router,
  ) {
    route.data.pipe(takeUntil(this.destroy)).subscribe(data => {
      // When router navigates on this component is takes the data and opens up the QuizzPlay modal
      this.currentDialog = this.modalService.open(QuizzPlayComponent, { centered: true, backdrop: 'static', size: 'lg' });
      this.currentDialog.componentInstance.quizz = data.quizz;

      // Go back to home page after the modal is closed
      this.currentDialog.result.then(
        result => {
          router.navigateByUrl('/');
        },
        reason => {
          router.navigateByUrl('/');
        },
      );
    });
  }

  ngOnDestroy() {
    this.destroy.next(undefined);
  }
}
