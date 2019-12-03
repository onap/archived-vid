import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {Observable} from "rxjs";
import {ServiceInstance} from "../../models/serviceInstance";
import {Constants} from "../../utils/constants";
import {createServiceInstance} from "../../storeUtil/utils/service/service.actions";

@Injectable()
export class InstantiationTemplatesService {
  constructor(private http: HttpClient, private store: NgRedux<AppState>) {
  }

  retrieveInstantiationTemplateTopology(jobId: string): Observable<ServiceInstance> {
    let pathQuery: string = `${Constants.Path.INSTANTIATION_TEMPLATE_TOPOLOGY}/${jobId}`;
    return this.http.get<ServiceInstance>(pathQuery)
  }

  public retrieveAndStoreInstantiationTemplateTopology(jobId: string, serviceModelId: string): Observable<ServiceInstance> {
    return this.retrieveInstantiationTemplateTopology(jobId).do((instantiationTemplate: ServiceInstance) => {
      this.store.dispatch(createServiceInstance(instantiationTemplate, serviceModelId));
    });
  };

}
