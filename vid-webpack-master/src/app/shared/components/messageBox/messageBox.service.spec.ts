import { TestBed, getTestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { MessageBoxService } from './messageBox.service';
import {MessageBoxData, ModalSize, ModalType } from './messageBox.data';

describe('MessageBoxService', () => {
  let injector;
  let service: MessageBoxService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MessageBoxService]
    });

    injector = getTestBed();
    service = injector.get(MessageBoxService);
    httpMock = injector.get(HttpTestingController);
  });

  describe('#setConfig', () => {
    it('should return <IModalConfig>', (done: DoneFn) => {
      let title = "Delete Instantiation";
      let message = "You are about to stop the instantiation process of this service. \nAll data will be lost. Are you sure you want to stop?";
      let messageBoxData : MessageBoxData = new MessageBoxData(
        title,
        message,
        ModalType.alert,
        ModalSize.medium,
        [
          {text:"Stop Instantiation", size:"large", closeModal:true},
          {text:"Cancel", size:"medium", closeModal:true}
        ]);

      let result = service.setConfig(messageBoxData);
      expect(result.title).toEqual(title);
      expect(result.message).toEqual(message);
      expect(result.buttons.length).toEqual(2);
      expect(result.type).toEqual(ModalType.alert);
      expect(result.size).toEqual(ModalSize.medium);
      done();
    });
  });
});
