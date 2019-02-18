import {Utils} from "./utils";
import {TestBed} from "@angular/core/testing";


describe('Util', () => {
  let util: Utils;

  beforeAll(done => (async () => {
    TestBed.configureTestingModule({

    });
    await TestBed.compileComponents();

    util = new Utils();

  })().then(done).catch(done.fail));

  test('should be defined', () => {
    expect(util).toBeDefined();
  });

  test('hasContents should return false if object is undefined or null or empty', () => {
    expect(Utils.hasContents(undefined)).toBeFalsy();
    expect(Utils.hasContents(null)).toBeFalsy();
    expect(Utils.hasContents("")).toBeFalsy();
  });

  test('hasContents should return true if object is not undefined and not null and not empty', () => {
    expect(Utils.hasContents("someValue")).toBeTruthy();
  });
});
