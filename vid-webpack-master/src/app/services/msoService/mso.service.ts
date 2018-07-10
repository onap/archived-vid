import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Constants} from "../../shared/utils/constants";

@Injectable()
export class MsoService {
  httpClient: HttpClient;

  constructor(http: HttpClient) {
    this.httpClient = http;
  }


  public submitMsoTask(instanceFields): Observable<any> {
    let path = '../../asyncInstantiation/bulk';
    return this.httpClient.post(path, instanceFields);
  }
  public createVnf(requestDetails, serviceInstanceId): Observable<any> {
    let pathQuery: string = Constants.Path.MSO_CREATE_VNF_INSTANCE + serviceInstanceId;

    return this.httpClient.post( pathQuery, {
      requestDetails : requestDetails
    });
  }
}
