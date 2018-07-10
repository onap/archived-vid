import { AfterContentChecked, AfterViewInit, Directive, ElementRef, Input } from '@angular/core';
import { isNullOrUndefined } from 'util';


/*
          Temporary
  Changing svg color and size.
  color changing according to fill attribute
  size according to viewBox
*/
@Directive({
  selector: '[svg-directive]'
})
export class SvgDirective implements AfterContentChecked {
  @Input('fill') fill: string = "black";
  @Input('widthViewBox') widthViewBox: string = null;
  @Input('heightViewBox') heightViewBox: string = null;

  constructor(private elRef: ElementRef) {}
  ngAfterContentChecked(): void {
    if(this.elRef !== undefined && this.elRef.nativeElement.children !== undefined && this.elRef.nativeElement.children[0] !== undefined){
      this.elRef.nativeElement.children[0].children[1].children[0].style.fill = this.fill;
      if(this.elRef.nativeElement.children[0].children[1].children.length > 1){
        this.elRef.nativeElement.children[0].children[1].children[1].style.fill = this.fill;
        this.elRef.nativeElement.children[0].children[1].children[2].children[0].style.fill = this.fill;
      }

      if(this.widthViewBox && this.heightViewBox){
        this.elRef.nativeElement.firstChild.setAttribute('viewBox', "1 1 " + this.widthViewBox + " " + this.heightViewBox)
      }

    }
  }
}
