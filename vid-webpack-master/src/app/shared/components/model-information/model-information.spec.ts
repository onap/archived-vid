import {ModelInformationItem} from "./model-information.component";

describe('ModelInformationItem', () => {
  test('when use createInstance, values initialized as expected', () =>{
    const modelInformationItem:ModelInformationItem = ModelInformationItem.createInstance("aStr", 4);
    expect(modelInformationItem.label).toEqual("aStr");
    expect(modelInformationItem.testsId).toEqual("aStr");
    expect(modelInformationItem.values).toEqual([4]);
    expect(modelInformationItem.mandatory).toBeFalsy();
    expect(modelInformationItem.toolTipText).toEqual("");
  });
});
