import {DynamicInputsService} from "../../dynamicInputs.service";
import {ILevelNodeInfo} from "../basic.model.info";
import * as _ from "lodash";
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {SharedTreeService} from "../../shared.tree.service";
import {VfModuleTreeNode} from "../../../../../shared/models/vfModuleTreeNode";
import {VfModuleInstance} from "../../../../../shared/models/vfModuleInstance";
import {VfModule} from "../../../../../shared/models/vfModule";
import {NgRedux} from "@angular-redux/store";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {
  GenericFormPopupComponent,
  PopupType
} from "../../../../../shared/components/genericFormPopup/generic-form-popup.component";
import {DialogService} from "ng2-bootstrap-modal";
import {VfModulePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {AppState} from "../../../../../shared/store/reducers";
import {MessageBoxData} from "../../../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../../../shared/components/messageBox/messageBox.service";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {
  deleteActionVfModuleInstance,
  deleteVFModuleField,
  pauseActionVFModuleInstance,
  removePauseActionVFModuleInstance,
  removeVfModuleInstance,
  undoDeleteVfModuleInstance,
  undoUgradeVFModule,
  updateVFModulePosition,
  upgradeVFModule
} from "../../../../../shared/storeUtil/utils/vfModule/vfModule.actions";
import {ComponentInfoService} from "../../../component-info/component-info.service";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {VfModuleUpgradePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";
import {FeatureFlagsService, Features} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {Utils} from "../../../../../shared/utils/utils";

export class VFModuleModelInfo implements ILevelNodeInfo {
  constructor(private _dynamicInputsService: DynamicInputsService,
              private _sharedTreeService: SharedTreeService,
              private _dialogService: DialogService,
              private _vfModulePopupService: VfModulePopupService,
              private _vfModuleUpgradePopupService: VfModuleUpgradePopupService,
              private _iframeService: IframeService,
              private _featureFlagsService: FeatureFlagsService,
              private _store: NgRedux<AppState>,
              private _componentInfoService: ComponentInfoService) {
  }

  name: string = 'vfModules';
  type: string = 'Module';
  typeName: string = 'M';
  componentInfoType = ComponentInfoType.VFMODULE;

  /***********************************************************
   * return if user should provide instance name or not.
   *        get info from parent (VNF)
   * @param currentModel - current Model object
   * @param parentModel - current parent Model object
   ************************************************************/
  isEcompGeneratedNaming(currentModel, parentModel): boolean {
    const ecompGeneratedNaming = !_.isNil(parentModel.properties) ? parentModel.properties.ecomp_generated_naming : undefined;
    return ecompGeneratedNaming === "true";
  }

  /***********************************************************
   * return model dynamic inputs
   * @param currentModel - current Model object
   ************************************************************/
  updateDynamicInputsDataFromModel = (currentModel): any => {
    let displayInputs = _.isNil(currentModel) ? [] : currentModel.inputs;
    return _.isEmpty(displayInputs) ? [] : this._dynamicInputsService.getArbitraryInputs(displayInputs);
  };

  /***********************************************************
   * return vfModule model
   * @param instanceModel - The model of the instance (usually extracted from
   *        serviceHierarchy store)
   ************************************************************/
  getModel = (instanceModel: any): Partial<VfModule> => {
    if (!_.isNil(instanceModel)) {
      return new VfModule(instanceModel, this._featureFlagsService.getAllFlags());
    }
    return {};
  };

  createNode(instance: VfModuleInstance, currentModel: VfModule, parentModel: VNFModel, modelName: string, index: number, serviceModelId: string): VfModuleTreeNode {
    let dynamicModelName = Object.keys(instance)[index];
    instance = instance[Object.keys(instance)[index]];
    const isEcompGeneratedNaming: boolean = this.isEcompGeneratedNaming(currentModel, parentModel);

    const dynamicInputs = this.updateDynamicInputsDataFromModel(currentModel);
    let newVfModule: VfModuleTreeNode = new VfModuleTreeNode(instance, currentModel, modelName, dynamicInputs, isEcompGeneratedNaming, dynamicModelName);

    newVfModule.missingData = this._sharedTreeService.hasMissingData(instance, newVfModule.dynamicInputs, isEcompGeneratedNaming, []);
    newVfModule.typeName = this.typeName;
    newVfModule.menuActions = this.getMenuAction(<any>newVfModule, serviceModelId);
    newVfModule.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    newVfModule.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
    newVfModule.pauseInstantiation = instance.pauseInstantiation;
    newVfModule.position = instance.position;
    newVfModule = this._sharedTreeService.addingStatusProperty(newVfModule);
    return newVfModule;
  }

  createInstanceTreeNode(instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any {
    let numberOfChilds = Object.keys(instance).length;
    if (numberOfChilds > 1) {
      let result: VfModuleTreeNode[] = [];
      for (let i = 0; i < numberOfChilds; i++) {
        result.push(this.createNode(instance, model, parentModel, storeKey, i, serviceModelId));
      }
      return result;
    } else {
      return this.createNode(instance, model, parentModel, storeKey, 0, serviceModelId);
    }
  }

  /***********************************************************
   * return next level object (null because is last level)
   ************************************************************/
  getNextLevelObject(): any {
    return null;
  }

  getTooltip = (): string => 'VFmodule';

  getType = (): string => 'VFmodule';

  /***********************************************************
   * return if instance has missing data
   * @param instance - vnf instance
   * @param dynamicInputs
   * @param isEcompGeneratedNaming
   ************************************************************/
  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return this._sharedTreeService.hasMissingData(instance, dynamicInputs, isEcompGeneratedNaming, []);
  }


  /***********************************************************
   * return if instance has missing data
   * @param node - VFModule node
   * @param serviceModelId - current service id
   ************************************************************/
  onClickAdd(node: ITreeNode, serviceModelId: string): void {
    const vnfStoreKey = this._sharedTreeService.getSelectedVNF() || this.getDefaultVNF(node.parent, serviceModelId);
    if (vnfStoreKey) {
      this._dialogService.addDialog(GenericFormPopupComponent, {
        type: PopupType.VF_MODULE,
        uuidData: <any>{
          serviceId: serviceModelId,
          modelName: node.data.name,
          vFModuleStoreKey: null,
          vnfStoreKey: vnfStoreKey,
          modelId: node.data.modelId,
          type: node.data.type,
          popupService: this._vfModulePopupService
        },
        node: node,
        isUpdateMode: false
      });
    } else {
      let messageBoxData: MessageBoxData = new MessageBoxData(
        "Select a parent",  // modal title
        "There are multiple instances on the right side that can contain this vf-module Please select the VNF instance, to add this vf-module to, on the right side and then click the + sign",
        <any>"warning",
        <any>"md",
        [
          {text: "Close", size: "medium", closeModal: true}
        ]);
      MessageBoxService.openModal.next(messageBoxData);
    }
  }

  getDefaultVNF(node: ITreeNode, serviceModelId: string): string {
    let keys = _.keys(_.pickBy(this._store.getState().service.serviceInstance[serviceModelId].vnfs, vnf => {
      return (this._sharedTreeService.modelUniqueId(vnf) === node.data.modelUniqueId);
    }));
    return keys.length === 1 ? this._store.getState().service.serviceInstance[serviceModelId].vnfs[keys[0]].vnfStoreKey : null;
  }

  /***********************************************************
   * return number of existing instances (in all VNF's)
   * @param node - VfModule node
   * @param serviceModelId - current service id
   ************************************************************/
  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    const vnfs = this._store.getState().service.serviceInstance[serviceModelId].vnfs;
    let count: number = 0;
    if (!_.isNil(this._store.getState().service.serviceInstance) && !_.isNil(this._store.getState().service.serviceInstance[serviceModelId])) {
      const selectedVNF: string = this._sharedTreeService.getSelectedVNF();
      if (selectedVNF) {
        count += this.countNumberOfVFModule(vnfs[selectedVNF], node);
      }else {
        for (let vnfKey in vnfs) {
          count += this.countNumberOfVFModule(vnfs[vnfKey], node);
        }
      }
      return count;
    }
    return count;
  }


  countNumberOfVFModule(vnf, node): number {
    let count = 0;
    for (let vfModuleKey in vnf['vfModules']) {
      for (let vfModule in vnf['vfModules'][vfModuleKey]) {
        if (this._sharedTreeService.modelUniqueId(vnf['vfModules'][vfModuleKey][vfModule]) === node.data.modelUniqueId) {
          const vfModuleObj = vnf['vfModules'][vfModuleKey][vfModule];
          if (!(!_.isNil(vfModuleObj) && !_.isNil(vfModuleObj.action) && vfModuleObj.action.split('_').pop() === 'Delete')) count++;
        }
      }
    }
    return count;
  }

  getCountVFModuleOfSelectedVNF(node: ITreeNode, vnfStoreKey: string, serviceModelId: string): number {
    let count: number = 0;
    if (!_.isNil(this._store.getState().service.serviceInstance) && !_.isNil(this._store.getState().service.serviceInstance[serviceModelId])) {
      const vnf = this._store.getState().service.serviceInstance[serviceModelId].vnfs[vnfStoreKey];
      count += this.countNumberOfVFModule(vnf, node);
      return count;
    }
    return count;
  }


  /***********************************************************
   * should show node icon
   * @param node - current ITrees node
   * @param serviceModelId - service id
   ************************************************************/
  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    const selectedVNF: string = this._sharedTreeService.getSelectedVNF();
    if (selectedVNF) {
      return this.showVFModuleOnSelectedVNF(node, selectedVNF, serviceModelId);
    } else {
      const optionalSelected = this.getOptionalVNFs(serviceModelId, node.parent.data.modelUniqueId);
      if (optionalSelected.length === 1) {
        return this.showVFModuleOnSelectedVNF(node, optionalSelected[0].vnfStoreKey, serviceModelId);
      } else {
        return new AvailableNodeIcons(false, false);
      }
    }
  }


  showVFModuleOnSelectedVNF(node: ITreeNode, selectedVNF: string, serviceModelId: string): AvailableNodeIcons {
    if (!_.isNil(this._store.getState().service.serviceInstance[serviceModelId].vnfs[selectedVNF])
        && node.parent.data.modelUniqueId === this._sharedTreeService.modelUniqueId(this._store.getState().service.serviceInstance[serviceModelId].vnfs[selectedVNF])) {
      const existingVFModules = this.getCountVFModuleOfSelectedVNF(node, selectedVNF, serviceModelId);
      const reachedLimit = this.isVFModuleReachedLimit(node, this._store.getState().service.serviceHierarchy, serviceModelId, existingVFModules);
      const showAddIcon = this._sharedTreeService.shouldShowAddIcon() && !reachedLimit;
      return new AvailableNodeIcons(showAddIcon, reachedLimit);
    }
    return new AvailableNodeIcons(false, false);

  }

  getOptionalVNFs(serviceUUID: string, vnfModelUniqueId: string): any[] {
    let result = [];
    if (!_.isNil(this._store.getState().service.serviceInstance) && !_.isNil(this._store.getState().service.serviceInstance[serviceUUID])) {
      const serviceVNFsInstances = this._store.getState().service.serviceInstance[serviceUUID].vnfs;
      for (let vnfKey in serviceVNFsInstances) {
        if (this._sharedTreeService.modelUniqueId(serviceVNFsInstances[vnfKey]) === vnfModelUniqueId) {
          serviceVNFsInstances[vnfKey].vnfStoreKey = vnfKey;
          result.push(serviceVNFsInstances[vnfKey]);
        }
      }
    }
    return result;
  }


  /************************************************
   return number of instances with action Delete
   @type: vnfs networks, vngGroups (only vfModule)
   @node : node model from the left tree
   ************************************************/
  getExistingInstancesWithDeleteMode(node, serviceModelId: string, selectedVNF: string): number {
    const existingInstances = this._store.getState().service.serviceInstance[serviceModelId].vnfs[selectedVNF].vfModules;
    let counter = 0;
    const modelUuid = node.data.uuid;

    if (!_.isNil(existingInstances)) {
      for (let instanceKey in existingInstances) {
        let size = Object.keys(existingInstances[instanceKey]).length;
        for (let i = 0; i < size; i++) {
          let vfModuleDynamicKey = Object.keys(existingInstances[instanceKey])[i];
          if (!_.isNil(existingInstances[instanceKey][vfModuleDynamicKey].action)) {
            if (existingInstances[instanceKey][vfModuleDynamicKey]['uuid'] === modelUuid && existingInstances[instanceKey][vfModuleDynamicKey].action.split('_').pop() === 'Delete') {
              counter++;
            }
          }
        }

      }
    }
    return counter;
  }

  isVFModuleReachedLimit(node: any, serviceHierarchy: any, serviceModelId: string, currentNodeCount: number): boolean {
    const flags = this._featureFlagsService.getAllFlags();
    let vnfModules = serviceHierarchy[serviceModelId].vfModules;
    const maxInstances = vnfModules[node.data.name]
      ? Utils.getMaxVfModule(vnfModules[node.data.name].properties, flags)
      : null;
    if (_.isNil(maxInstances)) {
      return false;
    }

    return !(maxInstances > currentNodeCount);
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return {
      edit: {
        method: (node, serviceModelId) => {
          this._iframeService.addClassOpenModal('content');
          this._dialogService.addDialog(GenericFormPopupComponent, {
            type: PopupType.VF_MODULE,
            uuidData: <any>{
              serviceId: serviceModelId,
              modelName: node.data.modelName,
              vFModuleStoreKey: node.data.dynamicModelName,
              vnfStoreKey: node.parent.data.vnfStoreKey,
              modelId: node.data.modelId,
              type: node.data.type,
              popupService: this._vfModulePopupService
            },
            node: node,
            isUpdateMode: true
          });
        },
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
      },
      showAuditInfo: {
        method: (node, serviceModelId) => {
          let instance = this._store.getState().service.serviceInstance[serviceModelId].vnfs[node.parent.data.vnfStoreKey].vfModules[node.data.modelName][node.data.modelVersionId];
          this._sharedTreeService.openAuditInfoModal(node, serviceModelId, instance, 'VFMODULE', this);
        },
        visible: (node) => this._sharedTreeService.shouldShowAuditInfo(node),
        enable: (node) => this._sharedTreeService.shouldShowAuditInfo(node)
      },
      remove: {
        method: (node, serviceModelId) => {
          this.removeVFM(serviceModelId,node);
        },
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
      },
      delete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(deleteActionVfModuleInstance(node.data.dynamicModelName, node.parent.data.vnfStoreKey, serviceModelId, node.data.modelName))
        },
        visible: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId),
        enable: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId)
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(undoDeleteVfModuleInstance(node.data.dynamicModelName, node.parent.data.vnfStoreKey, serviceModelId, node.data.modelName));
          this._store.dispatch(deleteVFModuleField(node.data.modelName,  node.parent.data.vnfStoreKey, node.data.servicedId ,node.data.dynamicModelName, 'retainAssignments'));
        },
        visible: (node) => this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => this._sharedTreeService.shouldShowUndoDelete(node) && this._sharedTreeService.shouldShowDelete(node.parent, serviceModelId) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
      },
      upgrade: {
        method: (node, serviceModelId) => {
          this.upgradeVFM(serviceModelId, node);
        },
        visible: (node,serviceModelId) => {
          return this._sharedTreeService.shouldShowUpgrade(node, serviceModelId);
        },
        enable:  (node, serviceModelId) => {
          return this._sharedTreeService.shouldShowUpgrade(node, serviceModelId);
        }
      },
      undoUpgrade: {
        method: (node, serviceModelId) => {
          this._sharedTreeService.undoUpgradeBottomUp(node, serviceModelId);
          this._store.dispatch(undoUgradeVFModule(node.data.modelName, node.parent.data.vnfStoreKey, serviceModelId, node.data.dynamicModelName));
        },
        visible: (node) => {
          return this._sharedTreeService.shouldShowUndoUpgrade(node);
        },
        enable: (node) => {
          return this._sharedTreeService.shouldShowUndoUpgrade(node);
        }
      },
      pauseInstantiation: {
        method: (node, serviceModelId) => {
          this._store.dispatch(pauseActionVFModuleInstance(node.data.dynamicModelName, node.parent.data.vnfStoreKey, serviceModelId, node.data.modelName));
        },
        visible: () => this._sharedTreeService.shouldShowPauseInstantiation(node),
        enable: () => true,
      },
      removePause: {
        method: (node, serviceModelId) => {
          this._store.dispatch(removePauseActionVFModuleInstance(node.data.dynamicModelName, node.parent.data.vnfStoreKey, serviceModelId, node.data.modelName));
        },
        visible: () => this._sharedTreeService.shouldShowRemovePause(node),
        enable: () => true,
      }
    };
  }

  private upgradeVFM(serviceModelId, node) {
    if (FeatureFlagsService.getFlagState(Features.FLAG_2002_VFM_UPGRADE_ADDITIONAL_OPTIONS, this._store)) {
      this._iframeService.addClassOpenModal('content');
      this._dialogService.addDialog(GenericFormPopupComponent, {
        type: PopupType.VF_MODULE_UPGRADE,
        uuidData: <any>{
          serviceId: serviceModelId,
          modelName: node.data.modelName,
          vFModuleStoreKey: node.data.dynamicModelName,
          vnfStoreKey: node.parent.data.vnfStoreKey,
          modelId: node.data.modelId,
          type: node.data.type,
          popupService: this._vfModuleUpgradePopupService,
          vfModule : _.cloneDeep(node)
        },
        node,
        isUpdateMode: true
      });
    } else {
      this._sharedTreeService.upgradeBottomUp(node, serviceModelId);
      this._store.dispatch(upgradeVFModule(node.data.modelName,  node.parent.data.vnfStoreKey, serviceModelId ,node.data.dynamicModelName));
    }
  }

  updatePosition(that, node,instanceId, parentStoreKey): void {
    that.store.dispatch(updateVFModulePosition(node.modelName,node.dynamicModelName, node.position,instanceId, parentStoreKey));
  }


  getNodePosition(instance, deepDynamicName): number {
    return (!_.isNil(instance) && !_.isNil(instance[deepDynamicName])) ? instance[deepDynamicName].position : null;
  }

  getInfo(model:Partial<VfModule>, instance): ModelInformationItem[] {
    const modelInformation = !_.isEmpty(model) ? [
      ModelInformationItem.createInstance("Base module", model.baseModule),
      ModelInformationItem.createInstance("Min instances", !_.isNull(model.min) ? String(model.min) : null),
      this._sharedTreeService.createMaximumToInstantiateModelInformationItem(model),
      ModelInformationItem.createInstance("Initial instances count", !_.isNull(model.initial) ? String(model.initial) : null)
    ] : [];

    const instanceInfo = [];
    const result = [modelInformation, instanceInfo];
    return _.uniq(_.flatten(result));
  }

  private removeVFM(serviceModelId, node) {
    if (FeatureFlagsService.getFlagState(Features.FLAG_2008_CREATE_VFMODULE_INSTANTIATION_ORDER_NUMBER, this._store)) {
      let nodeArray = node.parent.children;
      let totalNumOfNodes = nodeArray.length;
      let removedNodeIndex = node.index;
      let remainingNodes = totalNumOfNodes - (removedNodeIndex+1);
      if(!_.isNull(remainingNodes)) {
        if(remainingNodes !== 0 && remainingNodes >0) {
          for(let i= 0;i<remainingNodes;i++) {
            let temp = nodeArray[removedNodeIndex];
            nodeArray[removedNodeIndex] = nodeArray[removedNodeIndex + 1];
            nodeArray[removedNodeIndex + 1] = temp;
            removedNodeIndex= removedNodeIndex+1;
          }
          let removedNode = nodeArray[nodeArray.length - 1];
          this._store.dispatch(removeVfModuleInstance(removedNode.data.modelName, serviceModelId, removedNode.parent.data.modelName, removedNode.parent.data.vnfStoreKey, removedNode.data.dynamicModelName));
          nodeArray.pop();
          nodeArray.forEach((item, index) => {
            if (item.position !== index + 1) {
              item.position = index + 1;
              this._store.dispatch(updateVFModulePosition(item.data.modelName,item.data.dynamicModelName, item.position,serviceModelId, item.parent.data.vnfStoreKey));
            }
          });


        }else{
          this._store.dispatch(removeVfModuleInstance(node.data.modelName, serviceModelId, node.parent.data.modelName, node.parent.data.vnfStoreKey, node.data.dynamicModelName));

        }
      }
    } else {
      this._store.dispatch(removeVfModuleInstance(node.data.modelName, serviceModelId, node.parent.data.modelName, node.parent.data.vnfStoreKey, node.data.dynamicModelName));
    }
  }
}
