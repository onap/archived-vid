import {async, getTestBed, TestBed} from '@angular/core/testing';
import {MultiselectFormControlService} from "./multiselect.formControl.service";
import {MultiselectFormControl} from "../../../../models/formControlModels/multiselectFormControl.model";
import {MultiSelectItem} from "./multiselect.model";

describe('Multi Select Form Control Service', () => {

  let injector;
  let service: MultiselectFormControlService;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [MultiselectFormControlService]
    });
    await TestBed.compileComponents();
    injector = getTestBed();
    service = injector.get(MultiselectFormControlService);
  })().then(done).catch(done.fail));


  const options = [
      {
        id: 'A',
        name: 'a'
      },
      {
        id: 'B',
        name: 'b',
        keepMe: -42
      },
      {
        id: 'C',
        name: 'c'
      }
    ],
    selectedFieldName = 'name',
    ngValue = 'id';


  test('convertOriginalItems should convert options array to <MultiSelectItem> list', async(() => {
    let control: MultiselectFormControl = <any>{
      ngValue,
      selectedFieldName,
      options$: options
    };

    service.convertOriginalItems(control).then((result: MultiSelectItem[]) => {
      expect(result).toEqual([
        {"id": 1, "itemId": 'A', "itemName": 'a'},
        {"id": 2, "itemId": 'B', "itemName": 'b'},
        {"id": 3, "itemId": 'C', "itemName": 'c'}
      ]);
    });
  }));

  test('convertOriginalItems should return empty list when options list is empty', async(() => {
    let control: MultiselectFormControl = <any>{
      ngValue,
      selectedFieldName,
      options$: []
    };

    service.convertOriginalItems(control).then((result) => {
      expect(result).toEqual([]);
    });
  }));

  test('convertOptionsToHashMap - should convert any object to hash map with ngValue', async(() => {

    let control: MultiselectFormControl = <any>{
      ngValue,
      selectedFieldName,
      options$: options
    };

    let map = service.convertOptionsToHashMap(control);

    expect(Object.keys(map)).toHaveLength(3);
    expect(map).toEqual({
      'A': {
        id: 'A',
        name: 'a',
        index: 1
      },
      'B': {
        id: 'B',
        name: 'b',
        keepMe: -42,
        index: 2
      },
      'C': {
        id: 'C',
        name: 'c',
        index: 3
      }

    })
  }));

  test('convertOptionsToHashMap - should convert any object to hash map with ngValue: empty options', async(() => {
    let control: MultiselectFormControl = <any>{
      ngValue,
      selectedFieldName,
      options$: []
    };

    let map = service.convertOptionsToHashMap(control);

    expect(Object.keys(map)).toHaveLength(0)
  }));

  test('convertSelectedItems - should convert select item to multi select list', async(() => {
    let control: MultiselectFormControl = <any>{
      ngValue,
      selectedFieldName,
      options$: options,
      value: ['A', 'C']
    };

    service.convertSelectedItems(control).then((selectedOptions) => {
      expect(selectedOptions).toHaveLength(2);
      expect(selectedOptions[0].itemName).toEqual('a');
      expect(selectedOptions[1].itemName).toEqual('c');
    })
  }));

  test('convertSelectedItems - should convert select item to multi select list with special convert function', async(() => {
    let control: MultiselectFormControl = <any>{
      ngValue,
      selectedFieldName,
      options$: options,
      value: 'A,C',
      convertOriginalDataToArray: (value) => {
        return value.split(',');
      }
    };

    service.convertSelectedItems(control).then((selectedOptions) => {
      expect(selectedOptions).toHaveLength(2);
      expect(selectedOptions[0].itemName).toEqual('a');
      expect(selectedOptions[1].itemName).toEqual('c');
    })
  }));


  test('convertSelectedItems - should return empty list iof value is empty list', async(() => {
    let control: MultiselectFormControl = <any>{
      ngValue,
      selectedFieldName,
      options$: options,
      value: []
    };

    service.convertSelectedItems(control).then((selectedOptions) => {
      expect(selectedOptions).toHaveLength(0);
    })
  }));
});
