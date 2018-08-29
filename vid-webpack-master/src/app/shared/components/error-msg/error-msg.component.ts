import {Component, OnChanges, SimpleChanges} from '@angular/core';
import {ErrorMsgService} from "./error-msg.service";
import {ErrorMsgObject} from "./error-msg.model";

@Component({
  selector: 'error-msg',
  templateUrl: './error-msg.component.html',
  styleUrls: ['./error-msg.component.scss']
})

export class ErrorMsgComponent implements OnChanges {

  errorMsgService: ErrorMsgService;
  constructor(private _errorMsgService: ErrorMsgService) {
    this.errorMsgService = _errorMsgService;
    this._errorMsgService.triggerShowError.subscribe((error: ErrorMsgObject) => {
      this.errorMsgService.errorMsgObject = error;
    });

    this._errorMsgService.triggerClearError.subscribe(() => {
      this.errorMsgService.errorMsgObject = null;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
  }
}

