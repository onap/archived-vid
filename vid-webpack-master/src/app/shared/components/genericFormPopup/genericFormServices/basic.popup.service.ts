import {Injectable} from "@angular/core";
import {Subscriber} from "../../../models/subscriber";
import {NetworkModel} from "../../../models/networkModel";
import {AppState} from "../../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {FormControlModel} from "../../../models/formControlModels/formControl.model";
import {DefaultDataGeneratorService} from "../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {BasicControlGenerator} from "../../genericForm/formControlsServices/basic.control.generator";
import * as _ from 'lodash';
import {VfModule} from "../../../models/vfModule";
import {VNFModel} from "../../../models/vnfModel";
import {VnfGroupModel} from "../../../models/vnfGroupModel";
import {FeatureFlagsService} from "../../../services/featureFlag/feature-flags.service";
import {Utils} from "../../../utils/utils";

@Injectable()
export class BasicPopupService {
  constructor(private _store: NgRedux<AppState>,
              private _defaultDataGeneratorService : DefaultDataGeneratorService,
              private _utils: Utils,
              private _basicControlGenerator : BasicControlGenerator){}

  extractSubscriberNameBySubscriberId(subsriberId: string) {
    let result: string = null;
    let filteredArray: any = _.filter(this._store.getState().service.subscribers, function (o: Subscriber) {
      return o.id === subsriberId
    });
    if (filteredArray.length > 0) {
      result = filteredArray[0].name;
    }
    return result;
  }

  getModelFromResponse(result: any, modelType: string, modelName: string) {
    let flags = FeatureFlagsService.getAllFlags(this._store);
    let rawModel = result[modelType][modelName];
    if (!rawModel) return;
    switch (modelType){
      case 'vnfs' : {
        return new VNFModel(rawModel, flags);
      }
      case 'vfModules' : {
        return new VfModule(rawModel, flags);
      }
      case 'networks' : {
        return new NetworkModel(this._utils, rawModel);
      }
      case 'vnfGroups' : {
        return new VnfGroupModel(rawModel);
      }
    }
  }

  getDynamicInputs(serviceId : string, modelName : string, storeKey : string, type : string) : FormControlModel[]{
    let dynamic = this._defaultDataGeneratorService.getArbitraryInputs(this._store.getState().service.serviceHierarchy[serviceId][type][modelName].inputs);
      return this._basicControlGenerator.getDynamicInputsByType(dynamic, serviceId, storeKey, type );
  }
}
