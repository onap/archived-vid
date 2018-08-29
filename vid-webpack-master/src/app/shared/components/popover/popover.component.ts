import {Component, Input} from "@angular/core";
@Component({
  selector: 'custom-popover',
  templateUrl: 'popover.component.html'
})

export class PopoverComponent{
  @Input() value: string;
  @Input() extraStyle : string;
  @Input() placement : string = PopoverPlacement.LEFT;
  @Input() popoverType : string = PopoverType.CUSTOM;

}

export enum PopoverPlacement{
  TOP = 'top',
  BOTTOM = 'bottom',
  LEFT = 'left',
  RIGHT = 'right',
  AUTO ='auto',
}

export enum PopoverType {
  ERROR  = 'error',
  WARNING = 'warning',
  SUCCESS = 'success',
  CUSTOM = 'custom'
}

