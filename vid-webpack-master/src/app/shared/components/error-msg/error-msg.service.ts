import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {ErrorMsgObject} from "./error-msg.model";

@Injectable()
export class ErrorMsgService {
  triggerShowError: Subject<ErrorMsgObject> = new Subject<ErrorMsgObject>();
  triggerClearError: Subject<boolean> = new Subject<boolean>();
  errorMsgObject: ErrorMsgObject = null;

  getScalingErrorObject(): ErrorMsgObject {
    return new ErrorMsgObject("Error : Too many members",
      "One or more VNF groups, marked below, exceeds the maximum allowed number of members to associate",
      "Please make sure the total amount of VNF instances is less than that amount.");
  }

  getRetryErrorObject(numberOfFailed: number): ErrorMsgObject {
    return new ErrorMsgObject("ERROR!",
      `Attention: You are currently viewing instances from the MSO. \n ${numberOfFailed} of the instances failed, please try again.`,
      null);
  }
}
