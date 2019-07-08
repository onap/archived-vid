import {Injectable} from "@angular/core";
import {ILevelNodeInfo} from "./models/basic.model.info";
import {VnfModelInfo} from "./models/vnf/vnf.model.info";
import {NetworkModelInfo} from "./models/network/network.model.info";
import {ConfigurationModelInfo} from "./models/configuration/configuration.model.info";
import {DefaultDataGeneratorService} from "../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {DynamicInputsService} from "./dynamicInputs.service";
import {SharedTreeService} from "./shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {DialogService} from "ng2-bootstrap-modal";
import {VnfPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {NetworkPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import {VfModulePopuopService} from "../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popuop.service";
import {VnfGroupingModelInfo} from "./models/vnfGrouping/vnfGrouping.model.info";
import {VnfGroupPopupService} from "../../../shared/components/genericFormPopup/genericFormServices/vnfGroup/vnfGroup.popup.service";
import {DuplicateService} from "../duplicate/duplicate.service";
import {SdcUiServices} from "onap-ui-angular";
import {IframeService} from "../../../shared/utils/iframe.service";
import {ComponentInfoService} from "../component-info/component-info.service";
import {PnfModelInfo} from "./models/pnf/pnf.model.info";
import {CollectionResourceModelInfo} from "./models/collectionResource/collectionResource.model.info";
import {AaiService} from "../../../shared/services/aaiService/aai.service";
import {VrfModelInfo} from "./models/vrf/vrf.model.info";
import {NetworkStepService} from "./models/vrf/vrfModal/networkStep/network.step.service";
import {VpnStepService} from "./models/vrf/vrfModal/vpnStep/vpn.step.service";

@Injectable()
export class ObjectToTreeService {
  constructor(private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private _dynamicInputsService: DynamicInputsService,
              private _sharedTreeService : SharedTreeService,
              private _dialogService : DialogService,
              private _vnfPopupService : VnfPopupService,
              private  _networkPopupService :  NetworkPopupService,
              private _vfModulePopupService : VfModulePopuopService,
              private _vnfGroupPopupService : VnfGroupPopupService,
              private _duplicateService : DuplicateService,
              private _modalService: SdcUiServices.ModalService,
              private _iframeService : IframeService,
              private _componentInfoService : ComponentInfoService,
              private _networkStepService : NetworkStepService,
              private _vpnStepService : VpnStepService,
              private _aaiService : AaiService,
              private _store : NgRedux<AppState>) {
  }




  /***********************************************************
   * return all first optional first level of the model tree
   ************************************************************/
  getFirstLevelOptions(): ILevelNodeInfo[] {
    return [new VnfModelInfo(this._dynamicInputsService, this._sharedTreeService, this._defaultDataGeneratorService, this._dialogService, this._vnfPopupService, this._vfModulePopupService, this._duplicateService, this._modalService, this._iframeService, this._componentInfoService, this._store)
      , new NetworkModelInfo(this._dynamicInputsService, this._sharedTreeService, this._dialogService, this._networkPopupService, this._duplicateService, this._modalService, this._iframeService,  this._store),
      new PnfModelInfo(),
      new VrfModelInfo(this._store, this._sharedTreeService, this._dialogService, this._iframeService, this._networkStepService, this._vpnStepService),
      new CollectionResourceModelInfo(this._store, this._sharedTreeService),
      new ConfigurationModelInfo(this._dynamicInputsService, this._sharedTreeService),
      new VnfGroupingModelInfo(this._dynamicInputsService, this._sharedTreeService, this._dialogService, this._vnfGroupPopupService, this._iframeService, this._aaiService, this._store)];
  };
}
