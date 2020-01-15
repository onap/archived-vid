import {
  AfterViewChecked,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output
} from "@angular/core";
import * as _ from 'lodash';

@Component({
  selector : 'vid-svg-icon',
  template: `
      <custom-icon
        [mode]="mode"
        [size]="size"
        [name]="name"
        [testId]="testId"
        [clickable]="clickable">
    </custom-icon>
  `,


})
export class SvgComponent implements AfterViewChecked{
  @Input() mode : string = 'primary';
  @Input() size : string = 'large';
  @Input() testId : string = null;
  @Input() name : string = null;
  @Input() clickable : boolean = false;
  @Input() fill : string ;
  @Input() widthViewBox: string = null;
  @Input() heightViewBox: string = null;

  constructor(private elRef: ElementRef) {}

  ngAfterViewChecked(): void {
    if(!_.isNil(this.fill)){
      this.elRef.nativeElement.children[0].children[0].children[0].style.fill = this.fill;
    }

    if(this.widthViewBox && this.heightViewBox){
      this.elRef.nativeElement.children[0].children[0].children[0].setAttribute('viewBox', "1 1 " + this.widthViewBox + " " + this.heightViewBox)
    }

  }
}
