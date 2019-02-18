import {Injectable} from "@angular/core";
import {Subject} from "rxjs";

@Injectable()
export class FormGeneralErrorsService {
  static checkForErrorTrigger : Subject<boolean> = new Subject<boolean>();
}
