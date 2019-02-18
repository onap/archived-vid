import { SdcUiCommon} from "onap-ui-angular";
import {IModalButtonComponent} from "onap-ui-angular/dist/modals/models/modal-config";

export class  MessageBoxData {
  title?: string;
  message?: string;
  size : SdcUiCommon.ModalSize;
  type: SdcUiCommon.ModalType;
  buttons: IModalButtonComponent[];

  constructor(title: string, message: string, type: SdcUiCommon.ModalType, size : SdcUiCommon.ModalSize, buttons: IModalButtonComponent[]) {
    this.title = title;
    this.message = message;
    this.size = size;
    this.type = type;
    this.buttons = buttons;
  }
}
