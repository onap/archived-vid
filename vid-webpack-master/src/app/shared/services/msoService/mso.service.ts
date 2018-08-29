import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Constants} from "../../utils/constants";
import {ServiceInstance} from "../../models/serviceInstance";

@Injectable()
export class MsoService {
  httpClient: HttpClient;

  constructor(http: HttpClient) {
    this.httpClient = http;
  }


  public submitMsoTask(instanceFields): Observable<any> {
    let path = Constants.Path.SERVICES_JOB_INFO_PATH + '/bulk';
    return this.httpClient.post(path, instanceFields);
  }

  public retryMsoTask(jobId: string): Observable<any> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH + '/retry/' + jobId;
    return this.httpClient.post<any>(pathQuery, null);
  }

  public retryBulkMsoTask(jobId: string, instanceFields: ServiceInstance): Observable<any> {
    let pathQuery = Constants.Path.SERVICES_JOB_INFO_PATH + '/retryJobWithChangedData/'+ jobId;
    return this.httpClient.post<any>(pathQuery, instanceFields);
  }

  public createVnf(requestDetails, serviceInstanceId): Observable<any> {
    let pathQuery: string = Constants.Path.MSO_CREATE_VNF_INSTANCE + serviceInstanceId;

    return this.httpClient.post( pathQuery, {
      requestDetails : requestDetails
    });
  }
}
