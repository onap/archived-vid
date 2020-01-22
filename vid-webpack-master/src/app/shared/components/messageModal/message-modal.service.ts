import {Injectable} from "@angular/core";
import {MessageBoxService} from '../messageBox/messageBox.service';
import {MessageBoxData} from '../messageBox/messageBox.data';
import {SdcUiCommon} from "onap-ui-angular";
import {MessageModalModel} from "./message-modal.model";

@Injectable()
export class MessageModal {
  static showMessageModal(message: MessageModalModel): void {
    setTimeout(() => {
        let messageBoxData: MessageBoxData = new MessageBoxData(
          message.title,
          message.text,
          this.getModalType(message),
          SdcUiCommon.ModalSize.medium,
          message.buttons);
        MessageBoxService.openModal.next(messageBoxData);
      }
      , 500);
  };


  static getModalType = (message: MessageModalModel): string => {
    switch (message.type) {
      case "error": {
        return SdcUiCommon.ModalType.error
      }
      case "info": {
        return  SdcUiCommon.ModalType.info;
      }
      case "success":  {
        return  SdcUiCommon.ModalType.success;
      }
    }
  };
}



