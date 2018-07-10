

import {LogService} from "./log.service";

describe('log service service', () => {
  let logService : LogService;

  beforeEach(() => {
    logService = new LogService();
  });


  it('check all ILogger function are defined', ()=>{
    expect(logService.log).toBeDefined();
    expect(logService.assert).toBeDefined();
    expect(logService.error).toBeDefined();
    expect(logService.group).toBeDefined();
    expect(logService.groupEnd).toBeDefined();
    expect(logService.info).toBeDefined();
    expect(logService.warn).toBeDefined();
  });

  it('test getPrefixLog function', ()=> {
    let args = ['message', [1,2,3,4,5]];
    let result  = LogService.getPrefixLog(args);
    expect(result).toBeDefined();
  });

});
