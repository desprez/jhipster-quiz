import { Component, Input } from '@angular/core';
import { ControlContainer, FormArray, FormControl, FormGroup, FormGroupDirective, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IOption } from 'app/entities/option/option.model';
import { IQuestion } from 'app/entities/question/question.model';
import { OptionFormGroup, QuestionFormGroup, QuizzFormService } from '../../update/quizz-form.service';
import SharedModule from 'app/shared/shared.module';
import { OptionMakerComponent } from '../option-maker/option-maker.component';

@Component({
  selector: 'jhi-question-maker',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule, OptionMakerComponent],
  templateUrl: './question-maker.component.html',
  viewProviders: [{ provide: ControlContainer, useExisting: FormGroupDirective }],
})
export class QuestionMakerComponent {
  @Input() formArray = new FormArray<QuestionFormGroup>([]);

  constructor(protected quizzFormService: QuizzFormService) {}

  get questions(): FormArray<QuestionFormGroup> {
    return this.formArray;
  }

  getOptions(index: number): FormArray<OptionFormGroup> {
    return this.formArray.at(index).get('options') as unknown as FormArray<OptionFormGroup>;
  }

  getCorrectOptionIndex(index: number): number {
    return this.formArray.at(index).get('correctOptionIndex')?.value ?? 0;
  }

  getGroup(index: number) {
    return this.formArray.at(index) as FormGroup;
  }

  addQuestion(): void {
    const nextQuestionIndex = this.questions.length + 1;
    const newQuestion: IQuestion = {
      id: '',
      statement: '',
      index: nextQuestionIndex,
      correctOptionIndex: 1,
      options: [
        { id: '', statement: '', index: 1 },
        { id: '', statement: '', index: 2 },
      ],
    };
    this.questions.push(this.quizzFormService.initQuestion(newQuestion));
  }

  removeQuestion(questionIndex: number): void {
    this.questions.removeAt(questionIndex);
  }

  customTrackBy(index: number, obj: any): any {
    return index;
  }

  updateCorrectIndex(correctOptionIndex: number): void {
    console.log(correctOptionIndex);
  }
}
