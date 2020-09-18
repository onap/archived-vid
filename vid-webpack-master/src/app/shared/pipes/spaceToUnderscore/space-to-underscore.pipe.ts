import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'spaceToUnderscore'
})
export class SpaceToUnderscorePipe implements PipeTransform {

  transform(text: string): string {
    if (text) {
      // @ts-ignore
      text = text.replaceAll(' ', '_');
      return text;
    }
    return text;
  }

}
