import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { IModalConfig, MessageBoxData, ModalSize, ModalType } from './messageBox.data';

@Injectable()
export class MessageBoxService {
  static openModal: Subject<MessageBoxData> = new Subject<MessageBoxData>();
  setConfig(messageBoxData: MessageBoxData) : IModalConfig{
    return {
      size : ModalSize.medium,
      title : messageBoxData.title,
      type : messageBoxData.type,
      message : messageBoxData.message,
      buttons: messageBoxData.buttons
    };
  }

}
