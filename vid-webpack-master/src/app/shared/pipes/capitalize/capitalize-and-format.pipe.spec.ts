import {CapitalizeAndFormatPipe} from "./capitalize-and-format.pipe";
import {TestBed} from "@angular/core/testing";

describe('Capitalize And Format Pipe', () => {
  let capitalizeAndFormatPipe: CapitalizeAndFormatPipe;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({});
    await TestBed.compileComponents();
    capitalizeAndFormatPipe = new CapitalizeAndFormatPipe();
  })().then(done).catch(done.fail));


  test('Capitalize And Format Pipe should be defined', () => {
    expect(capitalizeAndFormatPipe).toBeDefined();
  });

  test('Capitalize And Format Pipe :  (UPPERCASE)', ()=> {
    let result: string = capitalizeAndFormatPipe.transform('PENDING');
    expect(result).toEqual('Pending');
  });

  test('Capitalize And Format Pipe (UPPERCASE) and Underscore should replace by -', ()=> {
    let result: string = capitalizeAndFormatPipe.transform('IN_PROGRESS');
    expect(result).toEqual('In-progress');
  });

  test('Capitalize And Format Pipe (COMPLETED_WITH_ERRORS) and All Underscores should replace by -', ()=> {
    let result: string = capitalizeAndFormatPipe.transform('COMPLETED_WITH_ERRORS');
    expect('Completed-with-errors').toEqual(result);
  });

});
