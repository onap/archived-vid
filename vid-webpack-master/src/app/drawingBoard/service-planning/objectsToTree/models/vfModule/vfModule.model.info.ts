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
import {GenericFormPopupComponent, PopupType} from "../../../../../shared/components/genericFormPopup/generic-form-popup.component";
import {DialogService} from "ng2-bootstrap-modal";
import {VfModulePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {AppState} from "../../../../../shared/store/reducers";
import {MessageBoxData} from "../../../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../../../shared/components/messageBox/messageBox.service";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {deleteActionVfModuleInstance, deleteVFModuleField, removeVfModuleInstance, undoDeleteVfModuleInstance, undoUgradeVFModule, updateVFModulePosition, upgradeVFModule} from "../../../../../shared/storeUtil/utils/vfModule/vfModule.actions";
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
   * @param vfModuleModelId - current Model id
   * @param instance
   * @param serviceHierarchy - serviceHierarchy
   ************************************************************/
  getModel = (vfModuleModelId: string, instance, serviceHierarchy): Partial<VfModule> => {
    if (!_.isNil(serviceHierarchy)) {
      if (!_.isNil(serviceHierarchy[this.name]) && !_.isNil(serviceHierarchy[this.name][vfModuleModelId])) {
        return new VfModule(serviceHierarchy[this.name][vfModuleModelId], this._featureFlagsService.getAllFlags());
      }
    }
    return {};
  };

  createNode(instance: VfModuleInstance, currentModel: VfModule, parentModel: VNFModel, modelName: string, index: number): VfModuleTreeNode {
    let dynamicModelName = Object.keys(instance)[index];
    instance = instance[Object.keys(instance)[index]];
    const isEcompGeneratedNaming: boolean = this.isEcompGeneratedNaming(currentModel, parentModel);

    const dynamicInputs = this.updateDynamicInputsDataFromModel(currentModel);
    let newVfModule: VfModuleTreeNode = new VfModuleTreeNode(instance, currentModel, modelName, dynamicInputs, isEcompGeneratedNaming, dynamicModelName);

    newVfModule.missingData = this._sharedTreeService.hasMissingData(instance, newVfModule.dynamicInputs, isEcompGeneratedNaming, []);
    newVfModule.typeName = this.typeName;
    newVfModule.menuActions = this.getMenuAction(<any>newVfModule, currentModel.uuid);
    newVfModule.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    newVfModule.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";

    newVfModule = this._sharedTreeService.addingStatusProperty(newVfModule);
    return newVfModule;
  }

  createInstanceTreeNode(instance: VfModuleInstance, currentModel: VfModule, parentModel: VNFModel, modelName: string): VfModuleTreeNode | VfModuleTreeNode[] {
    let numberOfChilds = Object.keys(instance).length;
    if (numberOfChilds > 1) {
      let result: VfModuleTreeNode[] = [];
      for (let i = 0; i < numberOfChilds; i++) {
        result.push(this.createNode(instance, currentModel, parentModel, modelName, i));
      }
      return result;
    } else {
      return this.createNode(instance, currentModel, parentModel, modelName, 0);
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
      return (vnf.originalName == node.data.name);
    }));
    return keys.length === 1 ? this._store.getState().service.serviceInstance[serviceModelId].vnfs[keys[0]].vnfStoreKey : null;
  }

  /***********************************************************
   * return number of existing instances (in all VNF's)
   * @param node - VfModule node
   * @param serviceModelId - current service id
   ************************************************************/
  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    let count: number = 0;
    if (!_.isNil(this._store.getState().service.serviceInstance) && !_.isNil(this._store.getState().service.serviceInstance[serviceModelId])) {
      const vnfs = this._store.getState().service.serviceInstance[serviceModelId].vnfs;

      for (let vnfKey in vnfs) {
        count += this.countNumberOfVFModule(vnfs[vnfKey], node);
      }
      return count;
    }
    return count;
  }


  countNumberOfVFModule(vnf, node): number {
    let count = 0;
    for (let vfModuleKey in vnf['vfModules']) {
      for (let vfModule in vnf['vfModules'][vfModuleKey]) {
        if (vnf['vfModules'][vfModuleKey][vfModule]['modelInfo'].modelCustomizationId === node.data.modelUniqueId) {
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
      const vnfs = this._store.getState().service.serviceInstance[serviceModelId].vnfs;

      for (let vnfKey in vnfs) {
        count += this.countNumberOfVFModule(vnfs[vnfKey], node);
      }
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
      const optionalSelected = this.getOptionalVNFs(serviceModelId, node.parent.data.name);
      if (optionalSelected.length === 1) {
        return this.showVFModuleOnSelectedVNF(node, optionalSelected[0].vnfStoreKey, serviceModelId);
      } else {
        return new AvailableNodeIcons(false, false);
      }
    }
  }


  showVFModuleOnSelectedVNF(node: ITreeNode, selectedVNF: string, serviceModelId: string): AvailableNodeIcons {

    if (!_.isNil(this._store.getState().service.serviceInstance[serviceModelId].vnfs[selectedVNF]) && node.parent.data.name === this._store.getState().service.serviceInstance[serviceModelId].vnfs[selectedVNF].originalName) {
      const existingVFModules = this.getCountVFModuleOfSelectedVNF(node, selectedVNF, serviceModelId);
      const reachedLimit = this.isVFModuleReachedLimit(node, this._store.getState().service.serviceHierarchy, serviceModelId, existingVFModules);
      const showAddIcon = this._sharedTreeService.shouldShowAddIcon() && !reachedLimit;
      return new AvailableNodeIcons(showAddIcon, reachedLimit);
    }
    return new AvailableNodeIcons(false, false);

  }

  getOptionalVNFs(serviceUUID: string, vnfOriginalModelName: string): any[] {
    let result = [];
    if (!_.isNil(this._store.getState().service.serviceInstance) && !_.isNil(this._store.getState().service.serviceInstance[serviceUUID])) {
      const serviceVNFsInstances = this._store.getState().service.serviceInstance[serviceUUID].vnfs;
      for (let vnfKey in serviceVNFsInstances) {
        if (serviceVNFsInstances[vnfKey].originalName === vnfOriginalModelName) {
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
        method: (node, serviceModelId) => this._store.dispatch(removeVfModuleInstance(node.data.modelName, serviceModelId, node.parent.data.modelName, node.parent.data.vnfStoreKey, node.data.dynamicModelName)),
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
      },
      delete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(deleteActionVfModuleInstance(node.data.dynamicModelName, node.parent.data.vnfStoreKey, serviceModelId))
        },
        visible: (node) => this._sharedTreeService.shouldShowDelete(node),
        enable: (node) => this._sharedTreeService.shouldShowDelete(node)
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(undoDeleteVfModuleInstance(node.data.dynamicModelName, node.parent.data.vnfStoreKey, serviceModelId));
          this._store.dispatch(deleteVFModuleField(node.data.modelName,  node.parent.data.vnfStoreKey, node.data.servicedId ,node.data.dynamicModelName, 'retainAssignments'));
        },
        visible: (node) => this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => this._sharedTreeService.shouldShowUndoDelete(node) && this._sharedTreeService.shouldShowDelete(node.parent) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
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

  updatePosition(that, node, instanceId, parentStoreKey): void {
    that.store.dispatch(updateVFModulePosition(node, instanceId, parentStoreKey));
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
}
