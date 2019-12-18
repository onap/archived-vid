import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ServiceInfoModel} from './serviceInfo.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Constants} from '../../utils/constants';
import {forkJoin} from "rxjs/observable/forkJoin";
import * as _ from 'lodash';
import {AuditStatus} from "./AuditStatus.model";

@Injectable()
export class ServiceInfoService {
  constructor(private _http: HttpClient) {
  }

  getServicesJobInfo(showSpinner: boolean = true): Observable<ServiceInfoModel[]> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH;
    let headers = new HttpHeaders({'x-show-spinner': showSpinner.toString()});
    return this._http.get<ServiceInfoModel[]>(pathQuery, { headers: headers});
  }

  getTemplatesInfo(showSpinner: boolean = true, serviceModelId: string): Observable<ServiceInfoModel[]> {
    let pathQuery = Constants.Path.INSTANTIATION_TEMPLATES_PATH;
    let headers = new HttpHeaders({'x-show-spinner': showSpinner.toString()});
    let params = {serviceModelId};
    return this._http.get<ServiceInfoModel[]>(pathQuery, { headers: headers, params });
  }

  deleteJob(jobId: string): Observable<any> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH + '/job/' + jobId;
    return this._http.delete<any>(pathQuery).map(res => res);
  }

  hideJob(jobId: string): Observable<any> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH + '/hide/' + jobId;
    return this._http.post<any>(pathQuery, null).map(res => res);
  }

  getJobAuditStatus(jobData: ServiceInfoModel) : Observable<Object[]>{
    let pathQueryBySource: string = Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + jobData.jobId + '?source=';
    let pathQueryVID: string = pathQueryBySource + 'VID';
    let pathQueryMSO: string;

    // just call it
    if(jobData.aLaCarte) {
      let requestParams: string[] = [];
      pathQueryMSO = Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + jobData.jobId + '/mso';
      if (!_.isEmpty(jobData.requestId)) {
        requestParams[requestParams.length] = 'requestId=' + jobData.requestId;
      }
      if (!_.isEmpty(jobData.serviceInstanceId)) {
        requestParams[requestParams.length] = 'serviceInstanceId=' + jobData.serviceInstanceId;
      }
      if (requestParams.length > 0) {
        pathQueryMSO += '?' + requestParams.join('&');
      }
    }
    else {
      pathQueryMSO = pathQueryBySource + 'MSO';
    }


    let vidObs = this._http.get(pathQueryVID);
    let msoObs = this._http.get(pathQueryMSO);
    return forkJoin([vidObs, msoObs]);
  }

  getInstanceAuditStatus(instanceId: string, type :string) : Observable<AuditStatus[]>{
    let pathQuery: string =  Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + type + '/' + instanceId + '/mso';
    return this._http.get<any>(pathQuery).map(res => res);
  }

  getAuditStatusForRetry(trackById: string) : Observable<AuditStatus>{
    let pathQuery: string = Constants.Path.AUDIT_STATUS_FOR_RETRY_PATH + '/' + trackById ;
    return this._http.get<any>(pathQuery).map(res => res);
  }
}
