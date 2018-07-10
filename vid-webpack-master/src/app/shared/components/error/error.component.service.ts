import {Injectable} from "@angular/core";
import {Subject} from "rxjs/Subject";
import { MessageBoxService } from '../messageBox/messageBox.service';
import { MessageBoxData, ModalSize, ModalType } from '../messageBox/messageBox.data';

@Injectable()
export class ErrorService {
  static showErrorWithMessage(error : ErrorMessage) : void {
    setTimeout(()=>{
        let messageBoxData : MessageBoxData = new MessageBoxData(
          error.title,  // modal title
          error.text,

          ModalType.error,
          ModalSize.medium,
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
