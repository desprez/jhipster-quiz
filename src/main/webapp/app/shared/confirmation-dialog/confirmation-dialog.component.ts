import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from '../shared.module';

@Component({
  selector: 'jhi-confirmation-dialog',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './confirmation-dialog.component.html',
})
export class ConfirmationDialogComponent implements OnInit {
  @Input() title: string = 'Please confirm...';
  @Input() message: string = 'Do you really want to ... ?';
  @Input() warning: string = 'Warn...';
  @Input() btnOkText: string = 'OK';
  @Input() btnCancelText: string = 'Cancel';

  constructor(private activeModal: NgbActiveModal) {}

  ngOnInit() {}

  public decline() {
    this.activeModal.close(false);
  }

  public accept() {
    this.activeModal.close(true);
  }

  public dismiss() {
    this.activeModal.dismiss();
  }
}
