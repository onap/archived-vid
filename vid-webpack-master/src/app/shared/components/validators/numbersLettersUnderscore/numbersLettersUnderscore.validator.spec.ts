import { ReflectiveInjector } from '@angular/core';
import { NumbersLettersUnderscoreValidator } from './numbersLettersUnderscore.validator';

describe('Numbers letters underscore validator', () => {
  let injector;
  let service: NumbersLettersUnderscoreValidator;

  beforeEach(() => {

    let injector = ReflectiveInjector.resolveAndCreate([
      NumbersLettersUnderscoreValidator
    ]);

    service = injector.get(NumbersLettersUnderscoreValidator);
  });


  describe('#valid', () => {
    it("'legal' should return null", ()=> {
      let legalVal: string = "legal";
      let result = NumbersLettersUnderscoreValidator.valid(legalVal);
      expect(result).toBeNull();
    });

    it("'illegal' should return object with invalidNumberLettersUnderscore true", ()=> {
      let illegalVal: string = "illegal-Val";
      let result = NumbersLettersUnderscoreValidator.valid(illegalVal);
      expect(result.invalidNumberLettersUnderscore).toBeTruthy();
    });

    it("'null' should return null", ()=> {
      let nullVal: string = null
      let result = NumbersLettersUnderscoreValidator.valid(nullVal);
      expect(result).toBeNull();
    });


  });
});
