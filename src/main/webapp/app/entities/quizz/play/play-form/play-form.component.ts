import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IAttemptAnswer } from 'app/entities/attempt-answer/attempt-answer.model';
import { IQuestion } from 'app/entities/question/question.model';

@Component({
  selector: 'jhi-play-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './play-form.component.html',
  styles: `
  .modal-body {
    max-height: 80vh; overflow-y: auto;
  }
  `,
})
export class PlayFormComponent implements OnChanges {
  private _answer!: IAttemptAnswer;
  playForm!: FormGroup;

  // decorators to define input (= question) and output (= when choice is made)
  @Input({ required: true }) question!: IQuestion;

  @Input({ required: true }) answer!: IAttemptAnswer;

  @Output() onChoiceMade = new EventEmitter<string>();

  constructor() {
    this.playForm = new FormGroup({
      choice: new FormControl(),
    });
    this.playForm.valueChanges.subscribe(this.onChange);
  }

  // method called, once component has received all inputs
  // initialized form controller will link model and view
  // also wire up the form controller with onChange method
  ngOnInit(): void {
    this.playForm = new FormGroup({
      choice: new FormControl(this.answer.option?.id),
    });
    this.playForm.valueChanges.subscribe(this.onChange);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['answer']) {
      this.playForm.get('choice')?.setValue(this.answer.option?.id);
    }
  }

  onChange = () => {
    this.onChoiceMade.emit(this.playForm.value.choice);
  };
}
