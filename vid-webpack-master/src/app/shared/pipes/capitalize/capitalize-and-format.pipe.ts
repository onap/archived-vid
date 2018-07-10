import {PipeTransform, Pipe} from '@angular/core';

@Pipe({ name: 'capitalizeAndFormat' })
export class CapitalizeAndFormatPipe implements PipeTransform {
  transform(text: string): string {
    if (text) {
      text = text.toLowerCase().replace('_', '-');
      return text.charAt(0).toUpperCase() + text.slice(1);
    }
    return text;
  }
}
