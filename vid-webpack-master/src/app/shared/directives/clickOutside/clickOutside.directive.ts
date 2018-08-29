import {Directive, EventEmitter, HostListener, Input, Output} from "@angular/core";
import * as _ from 'lodash';
/**********************************************************************
  Click outside directive
  can get one list of class's or id's (Not together)
  if all elements are not the target then clickOutside is trigger
 *******************************************************************/
@Directive({
  selector : '[clickOutside]'
})
export class ClickOutsideDirective {
  @Input() clickOutside : string;
  @Input() classElements : string[] = [];
  @Input() idElements : string[] = [];
  @Output() public clickOutsideTrigger: EventEmitter<any> = new EventEmitter();

  @HostListener('document:click', ['$event.target']) public onClick(targetElement) {
    if(!_.isNil(this.classElements)){this.hostListener(targetElement, this.classElements, 'getElementsByClassName');}
    if(!_.isNil(this.idElements)){this.hostListener(targetElement, this.idElements, 'getElementById');}
  }


/********************************************************************************
 hostListener - iterate over all elements and check if the element is the target.
 @param targetElement - the target element
 @param elements - elements to check
 @param documentMethod - method to find the element in dom
 ********************************************************************************/
hostListener(targetElement, elements, documentMethod): void{
  let result = true;
for(let i = 0 ; i < elements.length; i++){
  const classElements = document[documentMethod](elements[i]);
  for (let j = 0 ; j < classElements.length; j++){
    const clickedInside = document[documentMethod](elements[i])[j].contains(targetElement);
    if(clickedInside) return;
    result = result && clickedInside;
  }
}
if (!result) {this.clickOutsideTrigger.emit(null); }
}
}
