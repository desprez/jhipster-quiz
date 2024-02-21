import { Component, Input } from '@angular/core';
import { IQuizz } from '../../quizz.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-rules',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './rules.component.html',
})
export class RulesComponent {
  @Input({ required: true }) quizz!: IQuizz;
}
