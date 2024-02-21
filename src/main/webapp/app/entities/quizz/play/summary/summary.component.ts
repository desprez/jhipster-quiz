import { Component, EventEmitter, Output } from '@angular/core';
import { PlayMode } from '../play-mode';
import { IQuestion } from 'app/entities/question/question.model';

@Component({
  selector: 'jhi-sumary',
  standalone: true,
  imports: [],
  templateUrl: './summary.component.html',
})
export class SummaryComponent {
  // goTo(index: number) {
  //   if (index >= 0 && index < this.pager.count) {
  //     this.pager.index = index;
  //     this.mode = 'quiz';
  //   }
  // }
  // isAnswered(question: IQuestion) {
  //   return question.options.find(x => x.selected) ? 'Answered' : 'Not Answered';
  // };
}
