import { DynamicInputLabelPipe } from './dynamic-input-label.pipe';
import {TestBed} from "@angular/core/testing";

describe('Dynamic input label Pipe', () => {

  let dynamicInputLabelPipe: DynamicInputLabelPipe;
  beforeAll(done => (async () => {
    TestBed.configureTestingModule({});
    await TestBed.compileComponents();
    dynamicInputLabelPipe = new DynamicInputLabelPipe();

  })().then(done).catch(done.fail));

  test('Dynamic input label Pipe should be defined', () => {
    expect(dynamicInputLabelPipe).toBeDefined();
  });

  test('Dynamic input label Pipe : Empty string should return empty string', ()=> {
      let result: string = dynamicInputLabelPipe.transform('');
      expect(result).toEqual(':');
  });

  test('Dynamic input label Pipe: vnf should be VNF (UPPERCASE)', ()=> {
    let result: string = dynamicInputLabelPipe.transform('vnf');
    expect(result).toEqual('VNF:');
  });

  test('Dynamic input label Pipe : nf should be NF (UPPERCASE)\'', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nf');
    expect(result).toEqual('NF:');
  });

  test('Dynamic input label Pipe : Underscore should replace by empty character', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nf_Test');
    expect(result).toEqual('NF test:');
  });

  test('Dynamic input label Pipe : Complex string', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nf_Test_vnf_nf');
    expect(result).toEqual('NF test VNF NF:');
  });

  test('Dynamic input label Pipe : First letter should be uppercase', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nfr');
    expect(result).toEqual('Nfr:');
  });
});
