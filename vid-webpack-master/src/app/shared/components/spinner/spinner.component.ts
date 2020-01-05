import {Component} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import * as _ from 'lodash';

@Component({
  selector : 'spinner-component',
  templateUrl: './spinner.component.html',
  styleUrls : ['./spinner.component.scss'],
  providers : []

})
export class SpinnerComponent{
  show : boolean;
  size = "large";
  global = true;

  requestMap = {};

  static showSpinner: Subject<SpinnerInfo> = new Subject<SpinnerInfo>();

  constructor(){
    SpinnerComponent.showSpinner.subscribe((spinnerInfo) => {
      let status = spinnerInfo['status'];
      let requestType = spinnerInfo['requestType'];
      let requestUrl = spinnerInfo['requestUrl'];

      if(status && requestType === 'json'){
        this.requestMap[requestUrl] = true;
      }else {
        delete this.requestMap[requestUrl]
      }
      this.show = !_.isEmpty(this.requestMap) && this.requestMap !== undefined;

    })
  }
}


export class SpinnerInfo {
  status : boolean;
  requestUrl : string;
  requestType : string;

  constructor(status : boolean, requestUrl : string, requestType : string){
    this.status = status;
    this.requestUrl = requestUrl;
    this.requestType = requestType;
  }
}
