import {FeatureFlagsService, Features} from "./feature-flags.service";
import {getTestBed, TestBed} from "@angular/core/testing";
import {NgRedux} from "@angular-redux/store";
import each from 'jest-each';
let flagValue:boolean;

class MockReduxStore<T> {

  getState() {
    return {
      "global": {
        "flags": {
          "FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST": flagValue,
        },
      },
    }
  };
}

describe('Feature flags Service', () => {

  let injector;
  let service: FeatureFlagsService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers: [
        FeatureFlagsService,
        {provide: NgRedux, useClass: MockReduxStore}]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    service = injector.get(FeatureFlagsService);

  })().then(done).catch(done.fail));

    let flagValueDataProvider = [
      ['flag is true', true],
      ['flag is false', false]
    ];

    each(flagValueDataProvider).test("should return the correct flag %s", (desc: string, flag: boolean) => {
        flagValue = flag;
        expect(service.getFlagState(Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST)).toEqual(flag);
    });
});
