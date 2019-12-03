import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {HighlightPipe} from "../../pipes/highlight/highlight-filter.pipe";
import * as _ from 'lodash';

@Component({
  selector: 'custom-ellipsis',
  template: `
    <span
      sdc-tooltip
      class="ellipsis"
      [attr.data-tests-id]="dataTestId"
      id="{{id}}"
      [innerHtml]="displayValue | safe : 'html'"
      [ngStyle]="{'white-space' :  showDots ? 'nowrap' : 'initial'}"
      [ngClass]="{'breakWord' : breakWord == true}"
      [tooltip-text]="value">
      </span>`,
  styles: [
      `
      .ellipsis {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        display: inline-block;
        width: 99%;
        text-align: left;
      }

      .breakWord {
        word-wrap: break-word;
        white-space: normal;
      }
    `
  ],
  providers: [HighlightPipe]
})
export class EllipsisComponent implements OnChanges {
  @Input() value: string;
  @Input() id: string;
  @Input() hightlight: string;
  @Input() breakWord: boolean = false;
  @Input() dataTestId: string;
  @Input() showDots: boolean = false;

  displayValue: string;

  constructor(private _highlightPipe: HighlightPipe) {
    this.displayValue = this.value;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.displayValue = this.value;
    if (!_.isNil(this.hightlight)) {
      this.displayValue = this._highlightPipe.transform(this.value, this.hightlight ? this.hightlight : '');
    }
  }
}
