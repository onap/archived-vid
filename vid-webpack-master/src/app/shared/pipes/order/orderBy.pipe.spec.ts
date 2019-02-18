import {OrderByPipe} from "./orderBy.pipe";
import {TestBed} from "@angular/core/testing";


describe('Sort Pipe', () => {
  let pipe: OrderByPipe;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({

    });
    await TestBed.compileComponents();
    pipe = new OrderByPipe();

  })().then(done).catch(done.fail));


  test('Sort should order the array with nested objects', () => {
    let list = [
      {
        id: 1,
        name: 'b'
      },
      {
        id: 3,
        name: 'a'
      },
      {
        id: 2,
        name: 'd'
      }
    ];

    let result = pipe.transform(list, {property : 'name'});
    expect(result.length).toEqual(3);
    expect(result).toEqual(<any>[
      {
        'id': 3,
        'name': 'a'
      },
      {
        'id': 1,
        'name': 'b'
      },
      {
        'id': 2,
        'name': 'd'
      }])

  });

  test('Sort should order the array', () => {
    let list = ['b', 'd', 'a'];

    let result = pipe.transform(list);
    expect(result.length).toEqual(3);
    expect(result).toEqual(<any>['a', 'b', 'd']);

  });
});
