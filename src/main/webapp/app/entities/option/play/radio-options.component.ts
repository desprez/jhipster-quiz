import { NgForOf } from '@angular/common';
import { Component, DestroyRef, Input, OnInit, forwardRef, inject } from '@angular/core';
import { ControlValueAccessor, FormControl, FormGroup, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { IOption } from '../option.model';
import { debounceTime, noop, tap } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'jhi-radio-options',
  standalone: true,
  imports: [ReactiveFormsModule, NgForOf],
  templateUrl: './radio-options.component.html',
  styleUrl: './radio-options.component.scss',
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => PlayRadioOptionsComponent), multi: true }],
})
export class PlayRadioOptionsComponent implements ControlValueAccessor, OnInit {
  @Input({ required: true }) options: IOption[] = [];

  //selectedOption: any; // We'll use this variable to hold the selected option

  readonly radioForm: FormGroup = new FormGroup<{ value: FormControl<string | null> }>({
    value: new FormControl<string>(''),
  });

  private readonly destroyRef: DestroyRef = inject(DestroyRef);

  onChange: (value: unknown) => void = noop;
  onTouch: () => void = noop;

  ngOnInit(): void {
    this.radioForm.valueChanges
      .pipe(
        debounceTime(200),
        tap(({ value }) => this.onChange(value)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe();
    console.log('options' + this.options);
  }

  registerOnChange(fn: (value: unknown) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouch = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    isDisabled ? this.radioForm.disable() : this.radioForm.enable();
  }

  writeValue(value: string): void {
    this.radioForm.patchValue({ value }, { emitEvent: false });
  }
}
