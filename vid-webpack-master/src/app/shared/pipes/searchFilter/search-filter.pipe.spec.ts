import {SearchFilterPipe} from "./search-filter.pipe";
import * as _ from 'lodash';

describe('Search filter pipe', () => {

  const items= [{'id':1, 'name': 'aaa'},
    {'id':12, 'name': 'bbb', 'children':{'first': 155, 'second': 2, 'third': 3}},
    {'id':3, 'name': 'ccc', 'children':{'first': 1, 'BbB': '3', 'third': 3}},
    {'id':4, 'name': 'aad', 'children':{'first': 1, 'second': 2, 'third': 3}}];

  test('should return items contains substring bb', () => {
    let filter = new SearchFilterPipe();
    let res:any[] = filter.transform(items,'bb');
    expect(_.map(res, 'name' )).toEqual(['bbb','ccc']);
  });

});
