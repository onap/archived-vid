import {ModelInformationService} from "./model-information.service";
import {ModelInformationItem} from "./model-information.component";

describe('ModelInformationService', () => {
  let underTest:ModelInformationService;

  beforeEach(() => {
    underTest = new ModelInformationService();
  });

  test('when call to filterModelItems then items with empty values are filtered', () =>{
    expect(underTest.filterModelItems([
      ModelInformationItem.createInstance("emptyValue", ""),
      ModelInformationItem.createInstance("nullValue", null),
      ModelInformationItem.createInstance("undefinedValue", undefined),
      ModelInformationItem.createInstance("spacesValue", " "),
      new ModelInformationItem("emptyArray", "id", [], "c", false)
    ])).toHaveLength(0);
  });

  test('when call to filterModelItems then mandatory items with empty values are not filtered', () =>{
    const mandatoryItem:ModelInformationItem = new ModelInformationItem("a", "b", [""], "c", true);
    expect(underTest.filterModelItems([mandatoryItem])).toEqual([mandatoryItem]);
  });

  test('when call to filterModelItems then items with values are not filtered', () =>{
    expect(underTest.filterModelItems([
      ModelInformationItem.createInstance("withString", "a"),
      ModelInformationItem.createInstance("withNumber", 1),
    ])).toHaveLength(2);
  });
});
