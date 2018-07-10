import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/map';
import { Constants } from '../shared/utils/constants';
import { VidConfiguration } from '../configuration/vid.configuration';

@Injectable()
export class SdcService {


  constructor (private http: HttpClient) {
  }

  public getServicesModels(): any {
    let pathQuery: string = Constants.Path.SERVICES_DIST_STATUS_PATH + VidConfiguration.ASDC_MODEL_STATUS;

    if ( VidConfiguration.ASDC_MODEL_STATUS === Constants.Status.ALL) {
      pathQuery = Constants.Path.SERVICES_PATH;
    }

    return this.http.get(pathQuery);
  }

  getService(uuid: string) {
    return this.http.get(Constants.Path.SERVICES_PATH + uuid);
  }

}
