import { Component, forwardRef, Input } from '@angular/core';
import { ControlValueAccessor, FormControl, FormGroup, FormsModule, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { Duration } from './duration';

type DurationUnit = 'years' | 'months' | 'weeks' | 'days' | 'hours' | 'minutes' | 'seconds';

@Component({
  selector: 'jhi-duration',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './duration.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DurationComponent),
      multi: true,
    },
  ],
})
export class DurationComponent implements ControlValueAccessor {
  @Input() units: DurationUnit[] = ['years', 'months', 'weeks', 'days', 'hours', 'minutes', 'seconds'];

  unitConfig: { [key: string]: { label: string } } = {
    years: { label: 'Years' },
    months: { label: 'Months' },
    weeks: { label: 'Weeks' },
    days: { label: 'Days' },
    hours: { label: 'Hours' },
    minutes: { label: 'Minutes' },
    seconds: { label: 'Seconds' },
  };

  duration: Duration | undefined;

  durationFormGroup = new FormGroup({
    years: new FormControl(),
    months: new FormControl(),
    weeks: new FormControl(),
    days: new FormControl(),
    hours: new FormControl(),
    minutes: new FormControl(),
    seconds: new FormControl(),
  });

  onChange: any = () => {};
  onTouch: any = () => {};

  constructor() {
    this.durationFormGroup.valueChanges.subscribe(value => {
      const durationStr = dayjs.duration(value).toISOString();
      this.onChange(durationStr);
      this.onTouch(durationStr);
    });
  }

  writeValue(value: string) {
    if (value === undefined || value === null) {
      return;
    }
    const duration = new Duration(value);
    this.durationFormGroup.setValue({
      years: duration.years,
      months: duration.months,
      weeks: duration.weeks,
      days: duration.days,
      hours: duration.hours,
      minutes: duration.minutes,
      seconds: duration.seconds,
    });
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouch = fn;
  }

  setDisabledState(isDisabled: boolean) {
    isDisabled ? this.durationFormGroup.disable() : this.durationFormGroup.enable();
  }

  trackBy(i: number, unit: DurationUnit) {
    return unit;
  }
}
