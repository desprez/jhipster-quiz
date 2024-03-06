import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';

@Pipe({
  name: 'dayJS',
  standalone: true,
})
export default class DayJSPipe implements PipeTransform {
  transform(value: dayjs.Dayjs | null | undefined, method: 'fromNow' | 'toNow', withoutSuffix: boolean = false): string {
    switch (true) {
      case method === 'fromNow':
        return dayjs(value).fromNow(withoutSuffix);
      case method === 'toNow':
        return dayjs(value).toNow(withoutSuffix);
      default:
        return ''; // Add a default return statement
    }
  }
}
