import {Injectable} from '@angular/core';
import {ServiceInfoModel, ServiceInfoUiModel} from '../shared/server/serviceInfo/serviceInfo.model';
import {isNullOrUndefined} from "util";
import { Observable } from 'rxjs/Observable';
import 'rxjs/observable/of';

export let PENDING : string = "pending";
export let INPROGRESS : string = "inprogress";
export let PAUSE : string = "pause";
export let X_O : string = "X_o";
export let SUCCESS_CIRCLE : string = "success+Circle";
export let STOPED : string = "stoped";


@Injectable()
export class InstantiationStatusComponentService {
  generateServiceInfoDataMapping(arr: Array<ServiceInfoModel>) : { [serviceInstanceId: string]: Array<ServiceInfoModel>}{
    let serviceInfoData: { [serviceInstanceId: string]: Array<ServiceInfoModel>; } = {};
    for(let item of arr){
      if(isNullOrUndefined(serviceInfoData[item.templateId])){
        serviceInfoData[item.templateId] = [item];
      }else {
        serviceInfoData[item.templateId].push(item);
      }
    }
    return serviceInfoData;
  }

  convertObjectToArray(arr: Array<ServiceInfoModel>) : Observable<Array<ServiceInfoUiModel>>{
    const obj = this.generateServiceInfoDataMapping(arr);
    let index:number = 0;
    let result = [];
    for(let item in obj) {
      obj[item].map(item => {
        item['serviceStatus'] = this.getStatus(item.jobStatus);
        item['serviceIndex'] = index;
      });
      index++;
      result = result.concat(obj[item]);
    }

    console.log(result);
    return Observable.of(result);
  }

  getStatus(status : string) : ServiceStatus {
    switch(status.toUpperCase()) {
      case  'PENDING' :
        return new ServiceStatus(PENDING, '#009FDB', 'Pending: The service will automatically be sent for instantiation as soon as possible.');
      case  'IN_PROGRESS' :
        return new ServiceStatus(INPROGRESS, '#009FDB', 'In-progress: the service is in process of instantiation.');
      case  'PAUSED' :
        return new ServiceStatus(PAUSE, '#009FDB', 'Paused: Service has paused and waiting for your action.\n Select actions from the menu to the right.');
      case  'FAILED' :
        return new ServiceStatus(X_O, '#D02B2B', 'Failed: Service instantiation has failed, load the service to see the error returned.');
      case  'COMPLETED' :
        return new ServiceStatus(SUCCESS_CIRCLE, '#53AD15', 'Completed successfully: Service is successfully instantiated.');
      case  'STOPPED' :
        return new ServiceStatus(STOPED, '#D02B2B', 'Stopped: Due to previous failure, will not be instantiated.');
    }
  }
}


export class ServiceStatus {
  iconClassName : string;
  color : string;
  tooltip : string;

  constructor(_iconClassName : string, _color : string, _tooltip : string){
    this.iconClassName = _iconClassName;
    this.color = _color;
    this.tooltip = _tooltip;
  }
}
