import {getTestBed, TestBed} from "@angular/core/testing";
import {IframeService} from "./iframe.service";
import {DialogService} from "ng2-bootstrap-modal";

export class DialogServiceMock extends DialogService {
  removeDialog: (that) => ({})
}

describe('Iframe service', () => {
  let injector;
  let service: IframeService;
  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers : [
        IframeService
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(IframeService);

  })().then(done).catch(done.fail));


  test('service should be defined', ()=>{
    expect(service).toBeDefined();
  });

  test('closeIframe: should call removeClassCloseModal', ()=>{
    const dialogService = new DialogServiceMock(null, null, null, null);
    spyOn(service, 'removeClassCloseModal');
    spyOn(dialogService, 'removeDialog');
    service.closeIframe(dialogService, {})

    expect(service.removeClassCloseModal).toBeCalledWith('content');
    expect(dialogService.removeDialog).toBeCalledWith({});
  });

});
