import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationDialogComponent } from './confirmation-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { translationNotFoundMessage } from 'app/config/translation.config';

@Injectable({ providedIn: 'root' })
export class ConfirmationDialogService {
  constructor(
    private modalService: NgbModal,
    private translateService: TranslateService,
  ) {}

  public confirm(
    titleKey: string,
    messageKey: string,
    warningKey: string = '',
    btnOkKey: string = 'OK',
    btnCancelKey: string = 'Cancel',
    dialogSize: 'sm' | 'lg' = 'sm',
  ): Promise<boolean> {
    const modalRef = this.modalService.open(ConfirmationDialogComponent, { size: dialogSize });
    modalRef.componentInstance.title = this.translate(titleKey);
    modalRef.componentInstance.message = this.translate(messageKey);
    modalRef.componentInstance.warning = this.translate(warningKey);
    modalRef.componentInstance.btnOkText = this.translate(btnOkKey);
    modalRef.componentInstance.btnCancelText = this.translate(btnCancelKey);

    return modalRef.result;
  }

  translate(translationKey: string): string {
    if (translationKey) {
      const translatedMessage = this.translateService.instant(translationKey);
      // if translation key exists
      if (translatedMessage !== `${translationNotFoundMessage}[${translationKey}]`) {
        return translatedMessage;
      }
    }
    return translationKey;
  }
}
