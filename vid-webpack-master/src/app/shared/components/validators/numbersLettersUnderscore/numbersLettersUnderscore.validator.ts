import { Injectable } from '@angular/core';
import { isNullOrUndefined, isString } from 'util';

@Injectable()
export class NumbersLettersUnderscoreValidator {
  static valid(control: any) {
    let reg =  /^[a-zA-Z0-9_]*$/;

    if(isNullOrUndefined(control)) return null;
    let val = isString(control) ? control : control.value;
    if (val === null) {
      return {'invalidNumberLettersUnderscore': true};
    }
    if (reg.test(val)) {
      return null;
    } else {
      return {'invalidNumberLettersUnderscore': true};
    }
  }
}
