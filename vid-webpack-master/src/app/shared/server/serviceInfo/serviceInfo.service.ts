import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ServiceInfoModel} from './serviceInfo.model';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map'
import {Constants} from '../../utils/constants';
import {forkJoin} from "rxjs/observable/forkJoin";

@Injectable()
export class ServiceInfoService {
  constructor(private _http: HttpClient) {
  }

  getServicesJobInfo(filterByUser : boolean): Observable<ServiceInfoModel[]> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH;
    return this._http.get<ServiceInfoModel[]>(pathQuery).map(res => res );
  }

  deleteJob(jobId: string): Observable<any> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH + '/job/' + jobId;
    return this._http.delete<any>(pathQuery).map(res => res);
  }

  hideJob(jobId: string): Observable<any> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH + '/hide/' + jobId;
    return this._http.post<any>(pathQuery, null).map(res => res);
  }

  getJobAuditStatus(jobId : string) : Observable<Object[]>{
    let pathQueryVID = Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + jobId + '?source=VID';
    let pathQueryMSO = Constants.Path.SERVICES_JOB_INFO_PATH + Constants.Path.SERVICES_JOB_AUDIT_PATH + '/' + jobId + '?source=MSO';

    let vidObs = this._http.get(pathQueryVID);
    let msoObs = this._http.get(pathQueryMSO);
    return forkJoin([vidObs, msoObs]);
  }

}
