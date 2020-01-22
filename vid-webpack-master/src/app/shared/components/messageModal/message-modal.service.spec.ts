import {MessageModal} from "./message-modal.service";
import {getTestBed, TestBed} from "@angular/core/testing";
import {MessageModalTypeEnum} from "./message-modal-type.enum";
import {SdcUiCommon} from "onap-ui-angular";
import each from "jest-each";
import {MessageBoxService} from "../messageBox/messageBox.service";
import {MessageModalModel} from "./message-modal.model";
import {MessageBoxData} from "../messageBox/messageBox.data";


describe('Message Modal Service', () => {
  let injector;
  let service: MessageModal;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [MessageModal]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(MessageModal);
  })().then(done).catch(done.fail));


  each([
    [MessageModalTypeEnum.error, SdcUiCommon.ModalType.error],
    [MessageModalTypeEnum.info, SdcUiCommon.ModalType.info],
    [MessageModalTypeEnum.success, SdcUiCommon.ModalType.success]
  ]).test('getModalType with type %s should return %s', (inputMessageType, expectedResult) => {

    let message = {
      type: inputMessageType
    };

    const type = MessageModal.getModalType(<any>message)
    expect(type).toEqual(expectedResult);
  });


  each([
    [MessageModalTypeEnum.error, SdcUiCommon.ModalType.error],
    [MessageModalTypeEnum.info, SdcUiCommon.ModalType.info],
    [MessageModalTypeEnum.success, SdcUiCommon.ModalType.success]
  ]).test('getModalButtonType with type %s should return %s', (inputMessageType, expectedResult) => {

    let message = {
      type: inputMessageType
    };

    const type = MessageModal.getModalButtonType(<any>message)
    expect(type).toEqual(expectedResult);
  });


  test('showMessageModal should call open modal with all data' , async (done)=>{
    spyOn(MessageBoxService.openModal, 'next');
    let message : MessageModalModel  = new MessageModalModel('title', 'text',  MessageModalTypeEnum.success, []);

    MessageModal.showMessageModal(message);
    setTimeout(()=>{
      const messageBoxData = new MessageBoxData( message.title,
        message.text,
        SdcUiCommon.ModalType.success,
        SdcUiCommon.ModalSize.medium,
        message.buttons)
      expect(MessageBoxService.openModal.next).toHaveBeenCalledWith(messageBoxData);
      done();
    }, 500)
  });

});
