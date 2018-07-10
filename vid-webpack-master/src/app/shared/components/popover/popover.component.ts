import {Component, Input} from "@angular/core";

@Component({
  selector: 'custom-popover',
  templateUrl: 'popover.component.html',
  styles: [`
        :host >>> .popover {
          font-size: 13px;
          text-align: left;
          z-index: 10000;
        }
    `]
})

export class PopoverComponent {
  @Input() value: String;
}
