import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'digitToLetter',
  standalone: true,
})
export class DigitToLetterPipe implements PipeTransform {
  alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

  transform(value: unknown, ...args: unknown[]): unknown {
    if (typeof value === 'number') {
      return this.alphabet[value];
    }
    return value;
  }
}
