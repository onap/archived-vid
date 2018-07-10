import { Component, Input } from '@angular/core';

@Component({
  selector : 'custom-ellipsis',
  template: `
    <span 
          class="ellipsis"
          id="{{id}}"
          tooltip="{{value}}">{{value}}</span>`,
  styles : [
    `
      .ellipsis {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        display: inline-block;
        width: 99%;
        text-align: left;
      }
    `
  ]
})
export class EllipsisComponent {
  @Input() value : string;
  @Input() id : string;

}
