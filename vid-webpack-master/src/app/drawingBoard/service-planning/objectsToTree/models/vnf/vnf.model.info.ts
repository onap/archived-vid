import {DynamicInputsService} from "../../dynamicInputs.service";
import {ILevelNodeInfo} from "../basic.model.info";
import {VFModuleModelInfo} from "../vfModule/vfModule.model.info";
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {VnfInstance} from "../../../../../shared/models/vnfInstance";
import {VnfTreeNode} from "../../../../../shared/models/vnfTreeNode";
import {InputType} from "../../../../../shared/models/inputTypes";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {DefaultDataGeneratorService} from "../../../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import {
  GenericFormPopupComponent,
  PopupType
} from "../../../../../shared/components/genericFormPopup/generic-form-popup.component";
import {DialogService} from 'ng2-bootstrap-modal';
import {VnfPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vnf/vnf.popup.service";
import {VfModulePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModule/vfModule.popup.service";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {DuplicateVnfComponent} from "../../../duplicate/duplicate-vnf.component";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {changeInstanceCounter, removeInstance} from "../../../../../shared/storeUtil/utils/general/general.actions";
import {MessageBoxData} from "../../../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../../../shared/components/messageBox/messageBox.service";
import {ServiceInstanceActions} from "../../../../../shared/models/serviceInstanceActions";
import {
  deleteActionVnfInstance,
  undoDeleteActionVnfInstance,
  undoUpgradeVnf,
  updateVnfPosition,
  upgradeVnf
} from "../../../../../shared/storeUtil/utils/vnf/vnf.actions";
import * as _ from 'lodash';
import {IModalConfig} from "onap-ui-angular/dist/modals/models/modal-config";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ComponentInfoService} from "../../../component-info/component-info.service";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {VfModuleUpgradePopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/vfModuleUpgrade/vfModule.upgrade.popuop.service";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {ModalService} from "../../../../../shared/components/customModal/services/modal.service";

export class VnfModelInfo implements ILevelNodeInfo {
  constructor(private _dynamicInputsService: DynamicInputsService,
              private _sharedTreeService: SharedTreeService,
              private _defaultDataGeneratorService: DefaultDataGeneratorService,
              private _dialogService: DialogService,
              private _vnfPopupService: VnfPopupService,
              private _vfModulePopupService: VfModulePopupService,
              private _vfModuleUpgradePopupService: VfModuleUpgradePopupService,
              private _duplicateService: DuplicateService,
              private modalService: ModalService,
              private _iframeService: IframeService,
              private _componentInfoService: ComponentInfoService,
              private _featureFlagsService: FeatureFlagsService,
              private _store: NgRedux<AppState>) {
  }

  name: string = 'vnfs';
  type: string = 'VNF';
  childNames: string[] = ['vfModules'];
  typeName: string = 'VNF';
  componentInfoType = ComponentInfoType.VNF;

  /***********************************************************
   * return if user should provide instance name or not.
   * @param currentModel - current Model object
   ************************************************************/
  isEcompGeneratedNaming = (currentModel): boolean => {
    const ecompGeneratedNaming = currentModel.properties.ecomp_generated_naming;
    return ecompGeneratedNaming === "true";
  };

  /***********************************************************
   * return model dynamic inputs
   * @param currentModel - current Model object
   ************************************************************/
  updateDynamicInputsDataFromModel = (currentModel): any => {
    let displayInputs;
    return _.isEmpty(displayInputs) ? [] : this._dynamicInputsService.getArbitraryInputs(displayInputs);
  };

  /***********************************************************
   * return vnf model
   * @param vnfModelId - current Model id
   * @param instance - vnf instance
   * @param serviceHierarchy - serviceHierarchy
   ************************************************************/
  getModel = (instanceModel: any): VNFModel => {
    return new VNFModel(
      instanceModel,
      this._featureFlagsService.getAllFlags());
  };

  /***********************************************************
   * return vnf instance tree node
   * @param instance - vnf instance
   * @param model - vnf model
   * @param parentModel
   * @param storeKey - store key if exist
   * @param serviceModelId
   ************************************************************/
  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => {
    let node = new VnfTreeNode(instance, model, storeKey);
    node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
    node.typeName = this.typeName;
    node.menuActions = this.getMenuAction(<any>node, serviceModelId);
    node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
    node = this._sharedTreeService.addingStatusProperty(node);
    return node;
  };

  /***********************************************************
   * return next level object (VFModule)
   ************************************************************/
  getNextLevelObject = (): VFModuleModelInfo => {
    return new VFModuleModelInfo(this._dynamicInputsService, this._sharedTreeService, this._dialogService, this._vfModulePopupService, this._vfModuleUpgradePopupService, this._iframeService, this._featureFlagsService, this._store, this._componentInfoService);
  };

  /***********************************************************
   * return if instance has missing data
   * @param instance - vnf instance
   * @param dynamicInputs
   * @param isEcompGeneratedNaming
   ************************************************************/
  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return this._sharedTreeService.hasMissingData(instance, dynamicInputs, isEcompGeneratedNaming, [InputType.LCP_REGION, InputType.TENANT, InputType.PLATFORM]);
  }

  getTooltip = (): string => 'VF';

  getType = (): string => 'VF';

  /***********************************************************
   * return if instance has missing data
   * @param node - VNF node
   * @param serviceModelId - current service id
   ************************************************************/
  onClickAdd(node, serviceModelId: string): void {
    this._dialogService.addDialog(GenericFormPopupComponent, {
      type: PopupType.VNF,
      uuidData: <any>{
        serviceId: serviceModelId,
        modelName: node.data.name,
        vnfStoreKey: null,
        modelId: node.data.modelVersionId,
        type: node.data.type,
        popupService: this._vnfPopupService
      },
      node: node,
      isUpdateMode: false
    })
  }

  /***********************************************************
   * return number of existing instances
   * @param node - VNF node
   * @param serviceModelId - current service id
   ************************************************************/
  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    let map = null;
    if (!_.isNil(this._store.getState().service.serviceInstance[serviceModelId])) {
      map = this._store.getState().service.serviceInstance[serviceModelId].existingVNFCounterMap || 0;

      if (!_.isNil(map)) {
        let count = map[node.data.modelUniqueId] || 0;
        count -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'vnfs');
        return count;
      }
    }
    return 0;
  }

  /***********************************************************
   * should show node icon
   * @param node - current ITrees node
   * @param serviceModelId - service id
   ************************************************************/
  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    let counter: number = !_.isNil(this._store.getState().service.serviceInstance[serviceModelId]) ?
      (this._store.getState().service.serviceInstance[serviceModelId].existingVNFCounterMap[node.data.modelUniqueId] || 0) : 0;
    counter -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'vnfs');

    const properties = this._store.getState().service.serviceHierarchy[serviceModelId].vnfs[node.data.name].properties;
    const flags = FeatureFlagsService.getAllFlags(this._store);
    const isReachedLimit: boolean = this._sharedTreeService.isReachedToMaxInstances(properties, counter, flags);
    const showAddIcon = this._sharedTreeService.shouldShowAddIcon() && !isReachedLimit;
    return new AvailableNodeIcons(showAddIcon, isReachedLimit)
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return <any>{
      edit: {
        method: (node, serviceModelId) => {
          this._iframeService.addClassOpenModal('content');
          this._dialogService.addDialog(GenericFormPopupComponent, {
            type: PopupType.VNF,
            uuidData: <any>{
              serviceId: serviceModelId,
              modelName: node.data.modelName,
              vnfStoreKey: node.data.vnfStoreKey,
              modelId: node.data.modelId,
              type: node.data.type,
              popupService: this._vnfPopupService
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
          const instance = this._store.getState().service.serviceInstance[serviceModelId].vnfs[node.data.vnfStoreKey];
          this._sharedTreeService.openAuditInfoModal(node, serviceModelId, instance, 'VNF', this);
        },
        visible: (node) => this._sharedTreeService.shouldShowAuditInfo(node),
        enable: (node) => this._sharedTreeService.shouldShowAuditInfo(node)
      },
      duplicate: {
        method: (node, serviceModelId) => {
          const storeKey = node.data.vnfStoreKey;
          let modalConfig: IModalConfig = this._duplicateService.openDuplicateModal(serviceModelId, node.data.modelUniqueId, node.data.modelName, storeKey, 1, this._store, node);
          this.modalService.openCustomModal(modalConfig, DuplicateVnfComponent);
        },
        visible: (node) => this._sharedTreeService.shouldShowDuplicate(node) && !_.isNil(node.data) && !_.isNil(node.data.action) && node.data.action === ServiceInstanceActions.Create && this._duplicateService.canDuplicate(node),
        enable: (node, serviceModelId) => this._duplicateService.isEnabled(node, this._store, serviceModelId)
      },
      remove: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            let storeKey: string = node.data.vnfStoreKey;
            this._store.dispatch(removeInstance(node.data.vnfStoreKey, serviceModelId, storeKey, node));
            this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceModelId, -1, node));
            this._sharedTreeService.selectedNF = null;
          } else {
            let messageBoxData: MessageBoxData = new MessageBoxData(
              "Remove VNF",  // modal title
              "You are about to remove this VNF and all its children from this service. Are you sure you want to remove it?",
              <any>"warning",
              <any>"md",
              [
                {
                  text: "Remove VNF",
                  size: "large",
                  callback: this.removeVnf.bind(this, node, serviceModelId),
                  closeModal: true
                },
                {text: "Don’t Remove", size: "medium", closeModal: true}
              ]);

            MessageBoxService.openModal.next(messageBoxData);
          }
        },
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
      },
      delete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(deleteActionVnfInstance(node.data.vnfStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.shouldShowDeleteInstanceWithChildrenModal(node, serviceModelId, (node, serviceModelId) => {
              this._sharedTreeService.removeDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
                this._store.dispatch(deleteActionVnfInstance(node.data.vnfStoreKey, serviceModelId));
              });
            });
          }
        },
        visible: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId),
        enable: (node) => this._sharedTreeService.shouldShowDelete(node, serviceModelId)
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(undoDeleteActionVnfInstance(node.data.vnfStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.undoDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
              this._store.dispatch(undoDeleteActionVnfInstance(node.data.vnfStoreKey, serviceModelId));
            });
          }
        },
        visible: (node) => this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => this._sharedTreeService.shouldShowUndoDelete(node) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
      },
      upgrade: {
        method: (node, serviceModelId) => {
          this._store.dispatch(upgradeVnf(node.data.vnfStoreKey, serviceModelId));
        },
        visible: () => false,
        enable: () => false
      },
      undoUpgrade: {
        method: (node, serviceModelId) => {
          this._store.dispatch(undoUpgradeVnf(node.data.vnfStoreKey, serviceModelId));
        },
        visible: () => false,
        enable: () => false
      },
    }
  }

  removeVnf(this, node, serviceModelId) {
    this._store.dispatch(removeInstance(node.data.modelName, serviceModelId, node.data.vnfStoreKey, node));
    this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceModelId, -1, node));
    this._sharedTreeService.selectedNF = null;
  }

  /***********************************************************
   * should update node position inside the tree
   * @param node - current ITrees node
   ************************************************************/
  updatePosition(that, node, instanceId): void {
    that.store.dispatch(updateVnfPosition(node, instanceId, node.vnfStoreKey));
  }

  /***********************************************************
   * return the position of the current node in the tree
   * @param instance - current ITrees node instance
   ************************************************************/
  getNodePosition(instance): number {
    return !_.isNil(instance) ? instance.position : null;
  }

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
}
