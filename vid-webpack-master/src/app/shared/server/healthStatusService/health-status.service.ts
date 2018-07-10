import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Constants} from "../../utils/constants";
import {ExternalComponentStatus} from "../../models/externalComponentStatus";

@Injectable()
export class HealthStatusService {

  constructor(private _http: HttpClient) {
  }

  getProbe(): Observable<Array<ExternalComponentStatus>> {
    let pathQuery = Constants.Path.SERVICES_PROBE_PATH;
    return this._http.get<Array<ExternalComponentStatus>>(pathQuery).map(res => res);
  }
}
