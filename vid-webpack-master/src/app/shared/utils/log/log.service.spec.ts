

import {LogService} from "./log.service";
import {TestBed} from "@angular/core/testing";

describe('log service service', () => {

  let logService : LogService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({

    });
    await TestBed.compileComponents();
    logService = new LogService();


  })().then(done).catch(done.fail));


  test('check all ILogger function are defined', ()=>{
    expect(logService.log).toBeDefined();
    expect(logService.assert).toBeDefined();
    expect(logService.error).toBeDefined();
    expect(logService.group).toBeDefined();
    expect(logService.groupEnd).toBeDefined();
    expect(logService.info).toBeDefined();
    expect(logService.warn).toBeDefined();
  });

  test('test getPrefixLog function: with data', ()=> {
    let args = ['message', [1,2,3,4,5]];
    let result  = LogService.getPrefixLog(args);
    expect(result).toBeDefined();
  });

  test('log assert', ()=> {
    jest.spyOn(console, 'assert');
    logService.assert('someArg');
    expect(console.assert).toHaveBeenCalled();
  });

  test('log group', ()=> {
    jest.spyOn(console, 'group');
    logService.group('someArg');
    expect(console.group).toHaveBeenCalled();
  });

  test('log groupEnd', ()=> {
    jest.spyOn(console, 'groupEnd');
    logService.groupEnd('someArg');
    expect(console.groupEnd).toHaveBeenCalled();
  });

  test('log log', ()=> {
    jest.spyOn(console, 'log');
    logService.log('someArg');
    expect(console.log).toHaveBeenCalled();
  });

  test('log warn', ()=> {
    spyOn(console, 'warn');
    logService.warn('someArg');
    expect(console.warn).toHaveBeenCalled();
  });

});
