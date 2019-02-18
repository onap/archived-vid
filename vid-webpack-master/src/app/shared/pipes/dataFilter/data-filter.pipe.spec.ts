import {TestBed} from "@angular/core/testing";
import {DataFilterPipe} from "./data-filter.pipe";

describe('Data filter pipe', () => {

  let dataFilterPipe: DataFilterPipe;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({});
    await TestBed.compileComponents();

    dataFilterPipe = new DataFilterPipe();

  })().then(done).catch(done.fail));
  const items= [{'id':1, 'name': 'aaa'},
    {'id':12, 'name': 'bbb', 'children':{'first': 155, 'second': 2, 'third': 3}},
    {'id':3, 'name': 'ccc', 'children':{'first': 1, 'second': 2, 'third': 3}},
    {'id':4, 'name': 'aad', 'children':{'first': 1, 'second': 2, 'third': 3}}];
  const keys : string[][] = [["id"],["name"],["children", "first"]];


  test('should return items contains substring, keys not provided', () => {
    const expected =  [{'id':1, 'name': 'aaa'}, {'id':4, 'name': 'aad', 'children':{'first': 1, 'second': 2, 'third': 3}}];
    let res:any[] = dataFilterPipe.transform(items,'aa');
    expect(res).toEqual(expected);
  });


  test('should return no result, keys not provided', () => {
    const expected =  [];
    let res:any[] = dataFilterPipe.transform(items,'5');
    expect(res).toEqual(expected);
  });

  test('should return no result, deep keys provided', () => {
    const expected =  [];
    let res:any[] = dataFilterPipe.transform(items,'6', keys);
    expect(res).toEqual(expected);
  });

  test('should return expected result, deep keys provided', () => {
    const expected =  [{'id':12, 'name': 'bbb', 'children':{'first': 155, 'second': 2, 'third': 3}}];
    let res:any[] = dataFilterPipe.transform(items,'155', keys);
    expect(res).toEqual(expected);
  });

  test('should return expected result, case insensitive', () => {
    const expected =  [{'id':12, 'name': 'bbb', 'children':{'first': 155, 'second': 2, 'third': 3}}];
    let res:any[] = dataFilterPipe.transform(items,'BBB', keys);
    expect(res).toEqual(expected);
  });
});
