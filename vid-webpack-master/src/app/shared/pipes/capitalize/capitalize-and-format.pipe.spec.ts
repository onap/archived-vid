
import {CapitalizeAndFormatPipe} from "./capitalize-and-format.pipe";

describe('Capitalize And Format Pipe', () => {
  let capitalizeAndFormatPipe: CapitalizeAndFormatPipe;

  beforeEach(() => {
    capitalizeAndFormatPipe = new CapitalizeAndFormatPipe();
  });

  it('Capitalize And Format Pipe should be defined', () => {
    expect(capitalizeAndFormatPipe).toBeDefined();
  });

  it('Capitalize And Format Pipe :  (UPPERCASE)', ()=> {
    let result: string = capitalizeAndFormatPipe.transform('PENDING');
    expect(result).toEqual('Pending');
  });

  it('Capitalize And Format Pipe (UPPERCASE) and Underscore should replace by -', ()=> {
    let result: string = capitalizeAndFormatPipe.transform('IN_PROGRESS');
    expect(result).toEqual('In-progress');
  });

});
