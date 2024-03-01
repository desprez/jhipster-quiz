import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { OptionFormGroup, QuizzFormService } from '../../update/quizz-form.service';
import { ControlContainer, FormArray, FormControl, FormGroup, FormGroupDirective, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IOption } from 'app/entities/option/option.model';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-option-maker',
  standalone: true,
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
  templateUrl: './option-maker.component.html',
  viewProviders: [{ provide: ControlContainer, useExisting: FormGroupDirective }],
})
export class OptionMakerComponent implements OnChanges {
  constructor(protected quizzFormService: QuizzFormService) {
    this.correctOptionIndexControl.valueChanges.subscribe(this.onChange);
  }

  @Input()
  formArray = new FormArray<OptionFormGroup>([]);

  @Input()
  correctOptionIndex = 0;

  correctOptionIndexControl = new FormControl(0);

  @Output()
  onSelectOption = new EventEmitter<number>();

  getGroup(index: number) {
    return this.formArray.at(index) as FormGroup;
  }

  get options(): FormArray<OptionFormGroup> {
    return this.formArray;
  }

  addOption(): void {
    const option = this.newOption(this.options.length + 1);
    this.options.push(this.quizzFormService.initOption(option));
  }

  removeOption(optionIndex: number): void {
    this.options.removeAt(optionIndex);
  }

  newOption(nextOptionIndex: number): IOption {
    return { id: '', statement: '', index: nextOptionIndex };
  }

  ngOnChanges(changes: SimpleChanges): void {
    // if (changes['correctOptionIndex']) {
    //   this.formArray.get('correctOptionIndex')?.setValue(this.correctOptionIndex);
    // }
    this.correctOptionIndexControl = new FormControl(this.correctOptionIndex ? this.correctOptionIndex : 0);
  }

  onChange = () => {
    console.log('on change');
    this.onSelectOption.emit(this.correctOptionIndex);
  };
}
