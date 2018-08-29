import {SafePipe} from "./safe.pipe";
import {DomSanitizer} from "@angular/platform-browser";
import {getTestBed, TestBed} from "@angular/core/testing";


describe('Safe pipe', () => {

  let injector;
  let pipe: SafePipe;
  let sanitizer: DomSanitizer;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      providers: [SafePipe]
    });
    await TestBed.compileComponents();

    injector = getTestBed();
    sanitizer = injector.get(DomSanitizer);
    pipe = injector.get(SafePipe);

  })().then(done).catch(done.fail));

  test('safe pipe should return Safe object', () => {
    let options = [
      {
        value: 'value',
        type: 'html',
        func: 'bypassSecurityTrustHtml'
      },
      {
        value: 'value',
        type: 'style',
        func: 'bypassSecurityTrustStyle'
      },
      {
        value: 'value',
        type: 'script',
        func: 'bypassSecurityTrustScript'
      },
      {
        value: 'value',
        type: 'url',
        func: 'bypassSecurityTrustUrl'
      },
      {
        value: 'value',
        type: 'resourceUrl',
        func: 'bypassSecurityTrustResourceUrl'
      }
    ];

    for (let option of options) {
      jest.spyOn(sanitizer, <any>option.func);
      pipe.transform(option.value, option.type);
      expect(sanitizer[option.func]).toHaveBeenCalledWith(option.value);
    }
  });

});
