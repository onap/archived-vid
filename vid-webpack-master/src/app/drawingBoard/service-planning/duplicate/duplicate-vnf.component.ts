import { Component } from '@angular/core';
import * as _ from 'lodash';
import {DuplicateService} from "./duplicate.service";

@Component({
  selector: 'duplicate-vnf',
  templateUrl : './duplicate-vnf.component.html',
  styleUrls : ['./duplicate-vnf.component.scss']
})
export class DuplicateVnfComponent {
  duplicateNumber : number = 1;
  duplicateOptions : number[] = [];
  duplicateService:DuplicateService;
  constructor( private _duplicateService: DuplicateService ){
    this.duplicateService = _duplicateService;
    this.duplicateOptions =  _.range(1, this._duplicateService.maxNumberOfDuplicate + 1);
    this.onDuplicateNumberChange();
  }

  onDuplicateNumberChange() {
    this.duplicateService.setNumberOfDuplicates(+this.duplicateNumber);
  }


}
