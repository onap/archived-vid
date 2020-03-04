import {Injectable} from "@angular/core";
import {Subscriber} from "../../../models/subscriber";
import {NetworkModel} from "../../../models/networkModel";
import {AppState} from "../../../store/reducers";
import {NgRedux} from "@angular-redux/store";
import {FormControlModel} from "../../../models/formControlModels/formControl.model";
import {DefaultDataGeneratorService} from "../../../services/defaultDataServiceGenerator/default.data.generator.service";
import {ControlGeneratorUtil} from "../../genericForm/formControlsServices/control.generator.util.service";
import * as _ from 'lodash';
import {VfModule} from "../../../models/vfModule";
import {VNFModel} from "../../../models/vnfModel";
import {VnfGroupModel} from "../../../models/vnfGroupModel";
import {FeatureFlagsService} from "../../../services/featureFlag/feature-flags.service";
import {ModelInformationItem} from "../../model-information/model-information.component";
import {Constants} from "../../../utils/constants";

@Injectable()
export class BasicPopupService {
  constructor(private _store: NgRedux<AppState>,
              private _defaultDataGeneratorService : DefaultDataGeneratorService,
              private _basicControlGenerator : ControlGeneratorUtil){}

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
        return new NetworkModel(rawModel, flags);
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

  createMaximumToInstantiateModelInformationItem(model): ModelInformationItem {
    return new ModelInformationItem(
      "Maximum to instantiate",
      "max",
      [!_.isNil(model.max) ? model.max.toString() : Constants.ModelInfo.UNLIMITED_DEFAULT],
      "",
      true
    );
  }

  getVersionEitherFromInstanceOrFromHierarchy(nodeData, model): string | undefined {
    if(nodeData && nodeData.instanceModelInfo && nodeData.instanceModelInfo.modelVersion) {
      return nodeData.instanceModelInfo.modelVersion;
    }else if(model && model.version) {
      return model.version;
    }
    return undefined;
  }
}
