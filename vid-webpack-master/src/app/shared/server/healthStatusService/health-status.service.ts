import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Constants} from "../../utils/constants";
import {ExternalComponentStatus} from "../../models/externalComponentStatus";

@Injectable()
export class HealthStatusService {

  constructor(private _http: HttpClient) {
  }

  getProbe(): Observable<ExternalComponentStatus[]> {
    let pathQuery = Constants.Path.SERVICES_PROBE_PATH;
    return this._http.get<ExternalComponentStatus[]>(pathQuery).map(res => res);
  }
}
