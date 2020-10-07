import {Injectable} from "@angular/core";
import {IframeService} from "../../utils/iframe.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../store/reducers";
import {ServicePopupService} from "./genericFormServices/service/service.popup.service";
import {ActivatedRoute} from "@angular/router";
import {AaiService} from "../../services/aaiService/aai.service";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {UUIDData} from "./generic-form-popup.component";
import {FormPopupDetails} from "../../models/formControlModels/formPopupDetails.model";
import {Subject} from "rxjs";
import {deleteAllServiceInstances} from "../../storeUtil/utils/service/service.actions";
import {FeatureFlagsService, Features} from "../../services/featureFlag/feature-flags.service";

@Injectable()
export class GenericFormPopupService {
  constructor(private _iframeService : IframeService,
              private _store: NgRedux<AppState>,
              private _servicePopupService : ServicePopupService,
              public _activatedRoute : ActivatedRoute,
              private _featureFlagsService : FeatureFlagsService,
              private _aaiService : AaiService){

  }
  getGenericFormDetails(uuidData : UUIDData, node : ITreeNode, isUpdateMode : boolean) : FormPopupDetails {
    switch (uuidData.type){
      case 'VL' : {
        return uuidData.popupService.getGenericFormPopupDetails(
          uuidData['serviceId'],
          uuidData['networkId'],
          uuidData['networkStoreKey'],
          node,
          uuidData,
          isUpdateMode
        );
      }
      case 'VFmodule' : {
        return uuidData.popupService.getGenericFormPopupDetails(
          uuidData['serviceId'],
          uuidData['vnfStoreKey'],
          uuidData['vFModuleStoreKey'],
          node,
          uuidData,
          isUpdateMode
        );
      }
      case 'VF' : {
        return uuidData.popupService.getGenericFormPopupDetails(
          uuidData['serviceId'],
          uuidData['modelName'],
          uuidData['vnfStoreKey'],
          node,
          uuidData,
          isUpdateMode
        );
      }
      case 'PNF' : {
        return uuidData.popupService.getGenericFormPopupDetails(
          uuidData['serviceId'],
          uuidData['modelName'],
          uuidData['pnfStoreKey'],
          node,
          uuidData,
          isUpdateMode
        );
      }
      case 'VnfGroup' : {
        return uuidData.popupService.getGenericFormPopupDetails(
          uuidData['serviceId'],
          uuidData['modelName'],
          uuidData['vnfGroupStoreKey'],
          node,
          uuidData,
          isUpdateMode
        );
      }
      case 'service' : {
        uuidData['bulkSize'] = this._store.getState().service.serviceInstance[uuidData['serviceId']].bulkSize || 1;
        return uuidData.popupService.getGenericFormPopupDetails(
          uuidData['serviceId'],
          null,
          null,
          node,
          uuidData,
          isUpdateMode
        );
      }
    }
  }

  initReduxOnCreateNewService() : Promise<string> {
    return new Promise((resolve, reject) => {
      this._activatedRoute
        .queryParams
        .subscribe(params => {
          this._store.dispatch(deleteAllServiceInstances());
          this._aaiService.getServiceModelById(params.serviceModelId).subscribe(()=>{
            resolve(params.serviceModelId);
          });
      });
    });
  }

  refreshModalCheckForGeneralErrorTrigger : Subject<boolean> = new Subject<boolean>();



  shouldShowTemplateBtn = (isInstantiationTemplateExists: boolean) : boolean => {
    const instantiationTemplateFlag =  this._featureFlagsService.getFlagState(Features.FLAG_2004_INSTANTIATION_TEMPLATES_POPUP);
    if(instantiationTemplateFlag){
      return isInstantiationTemplateExists;
    }
    return false;
  }
}
