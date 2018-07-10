import {Directive, ElementRef} from '@angular/core';

@Directive({
  selector: '[patternInput]',
  host: {
    '(keypress)': 'onKeypress($event)'
  }
})
export class InputPreventionPatternDirective{
  inputElement : ElementRef;
  constructor(el: ElementRef) {
    this.inputElement = el;
  }

  onKeypress(event: KeyboardEvent) {
    const pattern = new RegExp(this.inputElement.nativeElement.pattern);
    if(pattern){
      if(!pattern.test(event['key'])){
        event.preventDefault();
      }
    }
    return event;
  }
}
