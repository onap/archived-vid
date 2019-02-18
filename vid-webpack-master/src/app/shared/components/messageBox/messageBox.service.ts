import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import {MessageBoxData} from "./messageBox.data";
import { SdcUiCommon} from "onap-ui-angular";

@Injectable()
export class MessageBoxService {
  static openModal: Subject<MessageBoxData> = new Subject<MessageBoxData>();
  setConfig(messageBoxData: MessageBoxData) : SdcUiCommon.IModalConfig{
    return {
      size : SdcUiCommon.ModalSize.medium,
      title : messageBoxData.title,
      type : messageBoxData.type,
      message : messageBoxData.message,
      buttons: messageBoxData.buttons
    };
  }
}
