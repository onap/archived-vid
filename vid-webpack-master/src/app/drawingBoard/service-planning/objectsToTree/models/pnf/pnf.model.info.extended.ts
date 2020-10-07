import {ILevelNodeInfo} from "../basic.model.info";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";

import {PNFModel} from "../../../../../shared/models/pnfModel";
import {SharedTreeService} from "../../shared.tree.service";
import * as _ from "lodash";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {
  GenericFormPopupComponent,
  PopupType
} from "../../../../../shared/components/genericFormPopup/generic-form-popup.component";
import {DialogService} from "ng2-bootstrap-modal";
import {PnfPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/pnf/pnf.popup.service";
import {PnfTreeNode} from "../../../../../shared/models/pnfTreeNode";


export class PnfModelInfoExtended implements ILevelNodeInfo{

  constructor(
    private _store: NgRedux<AppState>,
    private _sharedTreeService: SharedTreeService,
    private _dialogService: DialogService,
    private _pnfPopupService: PnfPopupService
  ){}

  name: string = 'pnfs';
  type: string ='PNF';
  typeName: string = 'PNF';
  childNames: string[];
  componentInfoType = ComponentInfoType.PNF;

  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => {
    let node = new PnfTreeNode(instance, model, storeKey);
    node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
    node.typeName = this.typeName;
    node.menuActions = this.getMenuAction(<any>node, serviceModelId);
    node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
    node = this._sharedTreeService.addingStatusProperty(node);
    return node;
  };

  getInfo(model, instance): ModelInformationItem[] {
    const modelInformation = !_.isEmpty(model) ? [
      ModelInformationItem.createInstance("Min instances", !_.isNil(model.min) ? String(model.min) : null),
      this._sharedTreeService.createMaximumToInstantiateModelInformationItem(model)
    ] : [];

    const instanceInfo = !_.isEmpty(instance) ? [
      ModelInformationItem.createInstance("NF type", instance.nfType),
      ModelInformationItem.createInstance("NF role", instance.nfRole)
    ] : [];

    const result = [modelInformation, instanceInfo];
    return _.uniq(_.flatten(result));
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function; visible: Function; enable: Function } } {
    return {};
  }

  getModel = (instanceModel: any): PNFModel => {
    return new PNFModel(instanceModel);
  };

  getNextLevelObject(): any { return null;  }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    let map = null;
    if (!_.isNil(this._store.getState().service.serviceInstance[serviceModelId])) {
      map = this._store.getState().service.serviceInstance[serviceModelId].existingPNFCounterMap || 0;

      if (!_.isNil(map)) {
        let count = map[node.data.modelUniqueId] || 0;
        count -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'pnfs');
        return count;
      }
    }
    return 0;
  }

  getNodePosition(instance): number {
    return !_.isNil(instance) ? instance.position : null;
  }

  getTooltip = (): string => 'PNF';


  getType = (): string => 'PNF';


  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return false;
  }

  isEcompGeneratedNaming(currentModel): boolean {
    return false;
  }

  onClickAdd(node, serviceModelId: string): void {
    this._dialogService.addDialog(GenericFormPopupComponent, {
      type: PopupType.PNF,
      uuidData: <any>{
        serviceId: serviceModelId,
        modelName: node.data.name,
        pnfStoreKey: null,
        modelId: node.data.modelVersionId,
        type: node.data.type,
        popupService: this._pnfPopupService
      },
      node: node,
      isUpdateMode: false
    })
  }

  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    let counter: number = !_.isNil(this._store.getState().service.serviceInstance[serviceModelId]) ?
      (this._store.getState().service.serviceInstance[serviceModelId].existingPNFCounterMap[node.data.modelUniqueId] || 0) : 0;
    counter -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'pnfs');

    const properties = this._store.getState().service.serviceHierarchy[serviceModelId].pnfs[node.data.name].properties;
    const flags = FeatureFlagsService.getAllFlags(this._store);
    const isReachedLimit: boolean = this._sharedTreeService.isReachedToMaxInstances(properties, counter, flags);
    const showAddIcon = this._sharedTreeService.shouldShowAddIcon() && !isReachedLimit;
    return new AvailableNodeIcons(showAddIcon, isReachedLimit)
  }

  updateDynamicInputsDataFromModel = (currentModel): any => [];

  updatePosition(that, node, instanceId): void {
  }

}
