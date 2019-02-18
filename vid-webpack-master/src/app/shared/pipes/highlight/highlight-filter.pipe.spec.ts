
import {HighlightPipe} from "./highlight-filter.pipe";
import {TestBed} from "@angular/core/testing";

describe('Highlight Pipe', () => {
  let highlightPipe: HighlightPipe;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({});
    await TestBed.compileComponents();

    highlightPipe = new HighlightPipe();

  })().then(done).catch(done.fail));

  test('Highlight Pipe should be defined', () => {
    expect(highlightPipe).toBeDefined();
  });

  test('Highlight Pipe should return "HTML" with highlight class if match exist', () => {
    let result : string = highlightPipe.transform('Hello World', 'Wor');
    expect(result).toEqual('Hello <span class="highlight">Wor</span>ld');
  });

  test('Highlight Pipe should not return "HTML" with highlight class if no match exist', () => {
    let result : string = highlightPipe.transform('Hello World', 'ABC');
    expect(result).toEqual('Hello World');
  });
});
