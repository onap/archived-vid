import {ILevelNodeInfo} from "../basic.model.info";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {VrfTreeNode} from "../../../../../shared/models/vrfTreeNode";
import {VrfModel} from "../../../../../shared/models/vrfModel";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {SharedTreeService} from "../../shared.tree.service";
import {DialogService} from 'ng2-bootstrap-modal';
import {SearchElementsModalComponent} from "../../../../../shared/components/searchMembersModal/search-elements-modal.component";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {NetworkStepService} from "./vrfModal/networkStep/network.step.service";
import {VpnStepService} from "./vrfModal/vpnStep/vpn.step.service";
import {NetworkModelInfo} from "../network/network.model.info";
import {VpnModelInfo} from "../vpn/vpn.model.info";
import {
  clearAllGenericModalhelper,
  updateGenericModalhelper,
  updateGenericModalTableDataHelper
} from "../../../../../shared/storeUtil/utils/global/global.actions";
import {
  deleteActionVrfInstance,
  undoDeleteActionVrfInstance
} from "../../../../../shared/storeUtil/utils/vrf/vrf.actions";
import * as _ from "lodash";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";

export class VrfModelInfo implements ILevelNodeInfo {
  constructor(private _store: NgRedux<AppState>,
              private _sharedTreeService: SharedTreeService,
              private _dialogService: DialogService,
              private _iframeService: IframeService,
              private _featureFlagsService : FeatureFlagsService,
              private _networkStepService: NetworkStepService,
              private _vpnStepService: VpnStepService) {
  }

  name: string = 'vrfs';
  type: string = 'VRF';
  typeName: string = 'VRF';
  childNames: string [] = ["networks", "vpns"];
  componentInfoType = ComponentInfoType.VRF;

  isEcompGeneratedNaming(currentModel): boolean {
    return false;
  }

  updateDynamicInputsDataFromModel = (currentModel): any => [];

  getModel = (instanceModel: any): VrfModel => {
    return new VrfModel(instanceModel);
  };


  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => {
    let node = new VrfTreeNode(instance, model, storeKey);
    node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
    node.typeName = this.typeName;
    node.menuActions = this.getMenuAction(<any>node, serviceModelId);
    node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
    return node;
  };


  getNextLevelObject = (nextLevelType: string): any => {
    if (nextLevelType === 'vpns') {
      return new VpnModelInfo(this._store, this._sharedTreeService);
    } else {
      if (nextLevelType === 'networks') {
        return new NetworkModelInfo(null, this._sharedTreeService, null, null, null, null, null,this._featureFlagsService,  this._store);
      }
    }
  };

  getTooltip = (): string => 'VRF';

  getType = (): string => 'VRF';

  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return false;
  }

  onClickAdd(node, serviceModelId: string): void {
    this._store.dispatch(clearAllGenericModalhelper());
    this._store.dispatch(updateGenericModalTableDataHelper('currentVRF', {
      model: this._store.getState().service.serviceHierarchy[serviceModelId].vrfs[node.data.name],
      instance: null
    }));
    this._iframeService.addFullScreen();
    let modalSteps = [this._vpnStepService, this._networkStepService];
    const serviceInstance = this._store.getState().service.serviceInstance[serviceModelId];
    this._dialogService.addDialog(SearchElementsModalComponent, {
        modalInformation: this._networkStepService.getNetworkStep(serviceInstance, serviceModelId, ...modalSteps)
      }
    )
  }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    //TODO
    return 0;
  }

  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    const serviceHierarchy = this._store.getState().service.serviceHierarchy[serviceModelId];

    let counter: number = !_.isNil(this._store.getState().service.serviceInstance[serviceModelId]) ?
      (this._store.getState().service.serviceInstance[serviceModelId].existingVRFCounterMap[node.data.modelUniqueId] || 0) : 0;
    counter -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'vrfs');

    const instanceModel = this._sharedTreeService.modelByIdentifiers(
      serviceHierarchy, node.data.modelTypeName,
      this._sharedTreeService.modelUniqueNameOrId(node.data), node.data.name
    );

    const model = node.data.getModel(instanceModel);
    const maxInstances: number = model.max;
    const isReachedLimit = !(maxInstances > counter);
    const showAddIcon = this._sharedTreeService.shouldShowAddIcon() && !isReachedLimit;

    return new AvailableNodeIcons(showAddIcon, isReachedLimit)
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return <any>{
      changeAssociations: {
        method: (node, serviceModelId) => {
          let modalSteps = [this._vpnStepService, this._networkStepService];
          this._store.dispatch(clearAllGenericModalhelper());
          const vrfInstance = this._store.getState().service.serviceInstance[serviceModelId].vrfs[node.data.vrfStoreKey];
          const vrfModel = this._store.getState().service.serviceHierarchy[serviceModelId].vrfs[node.data.name];
          this._store.dispatch(updateGenericModalTableDataHelper('currentVRF', {
            model: vrfModel,
            instance: vrfInstance,
            vrfStoreKey: node.data.vrfStoreKey
          }));


          for (let networkKey in vrfInstance.networks) {
            this._store.dispatch(updateGenericModalhelper(`selectedNetwork`, vrfInstance.networks[networkKey], modalSteps[1].uniqObjectField));
          }

          for (let networkKey in vrfInstance.vpns) {
            this._store.dispatch(updateGenericModalhelper(`selectedVPN`, vrfInstance.vpns[networkKey], modalSteps[0].uniqObjectField));
          }

          this._iframeService.addFullScreen();

          const serviceInstance = this._store.getState().service.serviceInstance[serviceModelId];
          this._dialogService.addDialog(SearchElementsModalComponent, {
              modalInformation: this._networkStepService.getNetworkStep(serviceInstance, serviceModelId, ...modalSteps)
            }
          )
        },
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
      },
      delete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(deleteActionVrfInstance(node.data.vrfStoreKey, serviceModelId));
        },
        visible: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId),
        enable: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId)
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(undoDeleteActionVrfInstance(node.data.vrfStoreKey, serviceModelId));
        },
        visible: (node) => this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => this._sharedTreeService.shouldShowUndoDelete(node) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
      }
    }
  }

  updatePosition(that, node, instanceId): void {
    //TODO
  }

  getNodePosition(instance): number {
    //TODO
    return 0;
  }

  getInfo(model, instance): ModelInformationItem[] {
    const modelInformation = !_.isEmpty(model) ? [
      ModelInformationItem.createInstance("Min instances", "1"),
      ModelInformationItem.createInstance("Max instances", "1"),
      ModelInformationItem.createInstance("Association", "L3-Network - VPN")] : [];
    return modelInformation;
  }
}

