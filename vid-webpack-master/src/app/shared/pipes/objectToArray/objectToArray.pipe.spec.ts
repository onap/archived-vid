import {TestBed} from "@angular/core/testing";
import {ObjectToArrayPipe} from "./objectToArray.pipe";


describe('Object To Array Pipe', () => {
  let pipe: ObjectToArrayPipe;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({

    });
    await TestBed.compileComponents();
    pipe = new ObjectToArrayPipe();

  })().then(done).catch(done.fail));


  test('should flat object to array', () => {
    let object = {
      "a" : {
        "name" :  "A"
      },
      "b" : {
        "name" :  "B"
      },
      "c" : {
        "name" :  "C"
      }
    };
    let result = pipe.transform(object);
    expect(result[0]).toEqual({"name" :  "A"});
    expect(result[1]).toEqual({"name" :  "B"});
    expect(result[2]).toEqual({"name" :  "C"});
  });
});
