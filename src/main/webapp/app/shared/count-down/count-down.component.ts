import { Component, EventEmitter, Input, Output } from '@angular/core';
import { map, timer, takeWhile, finalize } from 'rxjs';
import { AsyncPipe, DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-count-down',
  standalone: true,
  templateUrl: './count-down.component.html',
  imports: [AsyncPipe, DatePipe],
})
export class CountDownComponent {
  @Input() seconds = 300;

  @Output() onTimeout: EventEmitter<void> = new EventEmitter();

  timeRemaining$ = timer(0, 1000).pipe(
    map(n => (this.seconds - n) * 1000),
    takeWhile(n => n >= 0),
    finalize(() => this.onTimeout.emit()),
  );
}
