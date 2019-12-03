import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";

import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Observable} from "rxjs";
import {AppState} from "../../store/reducers";
import {InstantiationTemplatesService} from "../../services/templateService/instantiationTemplates.service";
import {forkJoin} from "rxjs/observable/forkJoin";
import {AaiService} from "../../services/aaiService/aai.service";
import {ServiceInstance} from "../../models/serviceInstance";

@Injectable()
export class RecreateResolver implements Resolve<Observable<[any, ServiceInstance]>> {
  constructor(private _templateService: InstantiationTemplatesService,
              private _aaiService: AaiService,
              private _store: NgRedux<AppState>) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<[any, ServiceInstance]> {
    const serviceModelId: string = route.queryParamMap.get("serviceModelId");
    const jobId: string = route.queryParamMap.get("jobId");

    let serviceModelApi = this._aaiService.getServiceModelById(serviceModelId);
    let instantiationTemplateApi = this._templateService.retrieveAndStoreInstantiationTemplateTopology(jobId, serviceModelId);

    return forkJoin([serviceModelApi, instantiationTemplateApi])
  }

}
