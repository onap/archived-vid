import {DynamicInputsService} from "./dynamicInputs.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";
import {NgReduxTestingModule} from "@angular-redux/store/testing";
describe('DynamicInputs  service', () => {
  let injector;
  let service: DynamicInputsService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgReduxTestingModule],
      providers: [
        DynamicInputsService
      ]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    service = injector.get(DynamicInputsService);
  })().then(done).catch(done.fail));

  test('DynamicInputs should be defined', () => {
    expect(service).toBeDefined();
  });
});
