import { Component, Input } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { IQuizz } from '../../quizz.model';

@Component({
  selector: 'jhi-welcome',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './welcome.component.html',
})
export class WelcomeComponent {
  @Input({ required: true }) quizz!: IQuizz;
}
