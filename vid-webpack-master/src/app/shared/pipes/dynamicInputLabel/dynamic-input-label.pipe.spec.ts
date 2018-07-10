import { DynamicInputLabelPipe } from './dynamic-input-label.pipe';

describe('Dynamic input label Pipe', () => {
  let dynamicInputLabelPipe: DynamicInputLabelPipe;

  beforeEach(() => {
    dynamicInputLabelPipe = new DynamicInputLabelPipe();
  });

  it('Dynamic input label Pipe should be defined', () => {
    expect(dynamicInputLabelPipe).toBeDefined();
  });

  it('Dynamic input label Pipe : Empty string should return empty string', ()=> {
      let result: string = dynamicInputLabelPipe.transform('');
      expect(result).toEqual(':*');
  });

  it('Dynamic input label Pipe: vnf should be VNF (UPPERCASE)', ()=> {
    let result: string = dynamicInputLabelPipe.transform('vnf');
    expect(result).toEqual('VNF:*');
  });

  it('Dynamic input label Pipe : nf should be NF (UPPERCASE)\'', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nf');
    expect(result).toEqual('NF:*');
  });

  it('Dynamic input label Pipe : Underscore should replace by empty character', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nf_Test');
    expect(result).toEqual('NF test:*');
  });

  it('Dynamic input label Pipe : Complex string', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nf_Test_vnf_nf');
    expect(result).toEqual('NF test VNF NF:*');
  });

  it('Dynamic input label Pipe : First letter should be uppercase', ()=> {
    let result: string = dynamicInputLabelPipe.transform('nfr');
    expect(result).toEqual('Nfr:*');
  });
});
