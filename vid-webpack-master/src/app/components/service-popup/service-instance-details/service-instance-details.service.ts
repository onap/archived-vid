import { Injectable } from '@angular/core';
import { isNullOrUndefined } from 'util';
import { FormGroup } from '@angular/forms';
import * as _ from 'lodash';
import { createVFModuleInstance, updateVFModuleInstance, updateVNFInstance } from '../../../service.actions';
import { NgRedux } from '@angular-redux/store';
import { AppState } from '../../../store/reducers';

@Injectable()
export class ServiceInstanceDetailsService {
  static controlsFieldsStatus = {};

  constructor(private store: NgRedux<AppState>) { }
  hasApiError(controlName: string, data: Array<any>, serviceInstanceDetailsFormGroup: FormGroup) {
    if (!isNullOrUndefined(data)) {
      if (!serviceInstanceDetailsFormGroup.controls[controlName].disabled && data.length === 0) {
          return true;
      }
    }
    return false;
  }
}
