import {ModelInformationService} from "./model-information.service";
import {ModelInformationItem} from "./model-information.component";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {getTestBed, TestBed} from "@angular/core/testing";

describe('ModelInformationService', () => {
  let injector;
  let service: ModelInformationService;
  let httpMock: HttpTestingController;


  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ModelInformationService
      ]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(ModelInformationService);
    httpMock = injector.get(HttpTestingController);

  })().then(done).catch(done.fail));


  test('when call to filterModelItems then items with empty values are filtered', () =>{
    expect(service.filterModelItems([
      ModelInformationItem.createInstance("emptyValue", ""),
      ModelInformationItem.createInstance("nullValue", null),
      ModelInformationItem.createInstance("undefinedValue", undefined),
      ModelInformationItem.createInstance("spacesValue", " "),
      new ModelInformationItem("emptyArray", "id", [], "c", false)
    ])).toHaveLength(0);
  });

  test('when call to filterModelItems then mandatory items with empty values are not filtered', () =>{
    const mandatoryItem:ModelInformationItem = new ModelInformationItem("a", "b", [""], "c", true);
    expect(service.filterModelItems([mandatoryItem])).toEqual([mandatoryItem]);
  });

  test('when call to filterModelItems then items with values are not filtered', () =>{
    expect(service.filterModelItems([
      ModelInformationItem.createInstance("withString", "a"),
      ModelInformationItem.createInstance("withNumber", 1),
    ])).toHaveLength(2);
  });

});
