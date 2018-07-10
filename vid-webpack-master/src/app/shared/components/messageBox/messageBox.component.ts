/************************************************************************************************
 * @Component: Message box component
 * In order to use component you need to do a number of things:
 *  1) Inside your component constructor you need to add listener to the button trigger.
 *  2) Inside the listener you should write your callback logic.
 *
 *  Example:
 *   @Component({
 *    selector : 'some-component'
 *    ...
 *   })
 *
 *   export class SomeComponent {
 *      openModal() : void {
 *        let messageBoxData : MessageBoxData = new MessageBoxData(
 *            "title",  // modal title
 *            "message", ModalType.alert, // modal type
  *           [
                {text:"Save", size:"'x-small'",  callback: this.someFunction.bind(this), closeModal:true},
                {text:"Cancel", size:"'x-small'", closeModal:true}
          ]);
 *
 *        MessageBoxService.openModal.next(messageBoxData); // open modal
 *      }
 *   }

 ************************************************************************************************/


import { Component } from '@angular/core';
import { MessageBoxData} from './messageBox.data';
import { MessageBoxService } from './messageBox.service';
import { SdcUiComponents } from 'sdc-ui/lib/angular';

@Component({
  selector: 'message-box',
  template: '<div id="message-box"></div>'
})

export class MessageBoxComponent {
  modalService: SdcUiComponents.ModalService;

  constructor(modalService: SdcUiComponents.ModalService, private _messageBoxService : MessageBoxService) {
    this.modalService = modalService;
    MessageBoxService.openModal.subscribe((messageBoxData: MessageBoxData) => {
      modalService.openModal(this._messageBoxService.setConfig(messageBoxData))
    });
  }
}

