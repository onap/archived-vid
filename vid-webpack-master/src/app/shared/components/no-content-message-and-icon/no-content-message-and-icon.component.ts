import {Component, Input} from '@angular/core';

@Component({
  selector: 'no-content-message-and-icon',
  templateUrl: './no-content-message-and-icon.component.html',
  styleUrls: ['./no-content-message-and-icon.component.scss']
})


export class NoContentMessageAndIconComponent {
  constructor() {}

  @Input() title: string;
  @Input() subtitle: string;
  @Input() iconPath: string;

  @Input() titleClass: string="";
  @Input() subtitleClass: string="";
  @Input() iconClass: string="";

}


