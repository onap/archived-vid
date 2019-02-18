import { TestBed, getTestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { MessageBoxService } from './messageBox.service';
import {MessageBoxData} from './messageBox.data';
import { SdcUiCommon} from "onap-ui-angular";

describe('MessageBoxService', () => {
  let injector;
  let service: MessageBoxService;
  let httpMock: HttpTestingController;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MessageBoxService]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(MessageBoxService);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));

  describe('#setConfig', () => {
    test('should return <IModalConfig>', () => {
      let title = "Delete Instantiation";
      let message = "You are about to stop the instantiation process of this service. \nAll data will be lost. Are you sure you want to stop?";
      let messageBoxData : MessageBoxData = new MessageBoxData(
        title,
        message,
        SdcUiCommon.ModalType.warning,
        SdcUiCommon.ModalSize.medium,
        [
          {text:"Stop Instantiation", size:"large", closeModal:true},
          {text:"Cancel", size:"medium", closeModal:true}
        ]);

      let result = service.setConfig(messageBoxData);
      expect(result.title).toEqual(title);
      expect(result.message).toEqual(message);
      expect(result.buttons.length).toEqual(2);
      expect(result.type).toEqual(SdcUiCommon.ModalType.warning);
      expect(result.size).toEqual(SdcUiCommon.ModalSize.medium);
    });
  });
});
