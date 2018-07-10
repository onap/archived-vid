import {PipeTransform, Pipe} from '@angular/core';

@Pipe({ name: 'highlight' })
export class HighlightPipe implements PipeTransform {
  transform(text: string, search: string): string {
    let pattern = search.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, '\\$&');
    let regex = new RegExp(pattern, 'gi');
    return search ? text.replace(regex, (match) => `<span class="highlight">${match}</span>`) : text;
  }
}
