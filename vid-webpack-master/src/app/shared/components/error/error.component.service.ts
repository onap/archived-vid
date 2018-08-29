import {Injectable} from "@angular/core";
import {MessageBoxService } from '../messageBox/messageBox.service';
import {MessageBoxData} from '../messageBox/messageBox.data';
import { SdcUiCommon} from "onap-ui-angular";
@Injectable()
export class ErrorService {
  static showErrorWithMessage(error : ErrorMessage) : void {
    setTimeout(()=>{
        let messageBoxData : MessageBoxData = new MessageBoxData(
          error.title,
          error.text,
          SdcUiCommon.ModalType.error,
          SdcUiCommon.ModalSize.medium,
          [
            {text:"Close", size:"large", closeModal:true}
          ]);
        MessageBoxService.openModal.next(messageBoxData);
      }
      ,500);
  }
}

export class ErrorMessage {
  title : string;
  text : string;
  errorNumber : number;

  constructor( title : string, text : string,errorNumber : number){
    this.title = title;
    this.text = text;
    this.errorNumber = errorNumber;
  }
}

