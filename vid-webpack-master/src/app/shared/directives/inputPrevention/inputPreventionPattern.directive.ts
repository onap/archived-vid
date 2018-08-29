import {Directive, ElementRef, HostBinding, HostListener} from '@angular/core';

@Directive({
  selector: '[patternInput]'
})
export class InputPreventionPatternDirective{
  @HostListener('keypress', ['$event']) onKeypress(event: KeyboardEvent) {
    const pattern = new RegExp(this.inputElement.nativeElement.pattern);
    if(pattern){
      if(!pattern.test(event['key'])){
        event.preventDefault();
      }
    }
    return event;
  }

  inputElement : ElementRef;
  constructor(el: ElementRef) {
    this.inputElement = el;
  }
}
