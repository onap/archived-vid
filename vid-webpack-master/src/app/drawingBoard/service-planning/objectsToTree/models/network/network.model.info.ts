import {DynamicInputsService} from "../../dynamicInputs.service";
import {ILevelNodeInfo} from "../basic.model.info";
import {NetworkModel} from "../../../../../shared/models/networkModel";
import {NetworkTreeNode} from "../../../../../shared/models/networkTreeNode";
import {SharedTreeService} from "../../shared.tree.service";
import {InputType} from "../../../../../shared/models/inputTypes";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {
  GenericFormPopupComponent,
  PopupType
} from "../../../../../shared/components/genericFormPopup/generic-form-popup.component";
import {DialogService} from "ng2-bootstrap-modal";
import {NetworkPopupService} from "../../../../../shared/components/genericFormPopup/genericFormServices/network/network.popup.service";
import * as _ from "lodash";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {DuplicateVnfComponent} from "../../../duplicate/duplicate-vnf.component";
import {changeInstanceCounter, removeInstance} from "../../../../../shared/storeUtil/utils/general/general.actions";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {ServiceInstanceActions} from "../../../../../shared/models/serviceInstanceActions";
import {
  deleteActionNetworkInstance,
  undoDeleteActionNetworkInstance,
  updateNetworkPosition
} from "../../../../../shared/storeUtil/utils/network/network.actions";
import {IModalConfig} from "onap-ui-angular/dist/modals/models/modal-config";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {FeatureFlagsService} from "../../../../../shared/services/featureFlag/feature-flags.service";
import {ModalService} from "../../../../../shared/components/customModal/services/modal.service";

export class NetworkModelInfo implements ILevelNodeInfo {
  constructor(private _dynamicInputsService: DynamicInputsService,
              private _sharedTreeService: SharedTreeService,
              private _dialogService: DialogService,
              private _networkPopupService: NetworkPopupService,
              private _duplicateService: DuplicateService,
              private modalService: ModalService,
              private _iframeService: IframeService,
              private _featureFlagsService: FeatureFlagsService,
              private _store: NgRedux<AppState>) {
  }

  name: string = 'networks';
  type: string = 'Network';
  typeName: string = 'N';
  childNames: string[];
  componentInfoType = ComponentInfoType.NETWORK;

  /***********************************************************
   * return model dynamic inputs
   * @param currentModel - current Model object
   ************************************************************/
  isEcompGeneratedNaming(currentModel): boolean {
    const ecompGeneratedNaming = currentModel.properties.ecomp_generated_naming;
    return ecompGeneratedNaming === "true";
  }

  updateDynamicInputsDataFromModel = (currentModel): any => {
    let displayInputs;
    return _.isEmpty(displayInputs) ? [] : this._dynamicInputsService.getArbitraryInputs(displayInputs);
  };

  /***********************************************************
   * return a NodeModel object instance
   * @param instanceModel - The model of the instance (usually extracted from serviceHierarchy store)
   ************************************************************/
  getModel = (instanceModel: any): NetworkModel => {
    return new NetworkModel(
      instanceModel,
      this._featureFlagsService.getAllFlags());
  };


  /***********************************************************
   * return network instance tree node
   * @param instance - network instance
   * @param model - network model
   * @param parentModel
   * @param storeKey - store key if exist
   ************************************************************/
  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => {
    let node = new NetworkTreeNode(instance, model, storeKey);
    node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
    node.typeName = this.typeName;
    node.menuActions = this.getMenuAction(<any>node, serviceModelId);
    node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
    node = this._sharedTreeService.addingStatusProperty(node);
    return node;
  };

  /***********************************************************
   * return next level object (null because is last level)
   ************************************************************/
  getNextLevelObject(): any {
    return null;
  }

  getTooltip = (): string => 'Network';

  getType = (): string => 'Network';

  /***********************************************************
   * return if instance has missing data
   * @param instance - vnf instance
   * @param dynamicInputs
   * @param isEcompGeneratedNaming
   ************************************************************/
  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return this._sharedTreeService.hasMissingData(instance, dynamicInputs, isEcompGeneratedNaming, [InputType.LCP_REGION, InputType.TENANT, InputType.PLATFORM]);
  }


  /***********************************************************
   * return if instance has missing data
   * @param node - Network node
   * @param serviceModelId - current service id
   ************************************************************/
  onClickAdd(node, serviceModelId: string): void {
    this._dialogService.addDialog(GenericFormPopupComponent, <any>{
      type: PopupType.NETWORK,
      uuidData: {
        serviceId: serviceModelId,
        networkId: node.data.name,
        networkStoreKey: null,
        type: 'VL',
        popupService: this._networkPopupService
      },
      node: node,
      isUpdateMode: false
    });
  }

  /***********************************************************
   * return number of existing instances
   * @param node - Network node
   * @param serviceModelId - current service id
   ************************************************************/
  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    let map = null;
    if (!_.isNil(this._store.getState().service.serviceInstance[serviceModelId])) {
      map = this._store.getState().service.serviceInstance[serviceModelId].existingNetworksCounterMap || 0;
      if (!_.isNil(map)) {
        let count = map[node.data.modelUniqueId] || 0;
        count -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'networks');
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
      (this._store.getState().service.serviceInstance[serviceModelId].existingNetworksCounterMap[node.data.modelUniqueId] || 0) : 0;

    counter -= this._sharedTreeService.getExistingInstancesWithDeleteMode(node, serviceModelId, 'networks');

    const properties = this._store.getState().service.serviceHierarchy[serviceModelId].networks[node.data.name].properties;
    const flags = FeatureFlagsService.getAllFlags(this._store);
    const isReachedLimit: boolean = this._sharedTreeService.isReachedToMaxInstances(properties, counter, flags);
    const showAddIcon = this._sharedTreeService.shouldShowAddIcon() && !isReachedLimit;

    return new AvailableNodeIcons(showAddIcon, isReachedLimit)
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    const mode = this._store.getState().global.drawingBoardStatus;
    return {
      edit: {
        method: (node, serviceModelId) => {
          this._iframeService.addClassOpenModal('content');
          this._dialogService.addDialog(GenericFormPopupComponent, {
            type: PopupType.NETWORK,
            uuidData: <any>{
              serviceId: serviceModelId,
              networkId: node.data.modelName,
              networkStoreKey: node.data.networkStoreKey,
              type: node.data.type,
              popupService: this._networkPopupService
            },
            node: node,
            isUpdateMode: true
          });
        },
        visible: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowRemoveAndEdit(node)
      },
      showAuditInfo: {
        method: (node, serviceModelId) => {
          let instance = this._store.getState().service.serviceInstance[serviceModelId].networks[node.data.networkStoreKey];
          this._sharedTreeService.openAuditInfoModal(node, serviceModelId, instance, 'NETWORK', this);
        },
        visible: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowAuditInfo(node),
        enable: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowAuditInfo(node)
      },
      duplicate: {
        method: (node, serviceModelId) => {
          const storeKey = node.data.networkStoreKey;
          let modalConfig: IModalConfig = this._duplicateService.openDuplicateModal(serviceModelId, node.data.modelUniqueId, node.data.modelName, storeKey, 1, this._store, node);
          this.modalService.openCustomModal(modalConfig, DuplicateVnfComponent);
        },
        visible: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowDuplicate(node) && !_.isNil(node.data) && !_.isNil(node.data.action) && node.data.action === ServiceInstanceActions.Create && this._duplicateService.canDuplicate(node),
        enable: (node, serviceModelId) => node.data.parentType !== 'VRF' && this._duplicateService.isEnabled(node, this._store, serviceModelId)
      },
      remove: {
        method: (node, serviceModelId) => {
          let storeKey: string = node.data.networkStoreKey;
          this._store.dispatch(removeInstance(node.data.networkStoreKey, serviceModelId, storeKey, node));
          this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceModelId, -1, node));
          this._sharedTreeService.selectedVNF = null;
        },
        visible: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowRemoveAndEdit(node),
      },
      delete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(deleteActionNetworkInstance(node.data.networkStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.removeDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
              this._store.dispatch(deleteActionNetworkInstance(node.data.networkStoreKey, serviceModelId));
            });
          }
        },
        visible: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowDelete(node, serviceModelId),
        enable: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowDelete(node, serviceModelId)
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(undoDeleteActionNetworkInstance(node.data.networkStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.undoDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
              this._store.dispatch(undoDeleteActionNetworkInstance(node.data.networkStoreKey, serviceModelId));
            });
          }
        },
        visible: (node) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => node.data.parentType !== 'VRF' && this._sharedTreeService.shouldShowUndoDelete(node) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
      }

    };
  }

  /***********************************************************
   * should update node position inside the tree
   * @param node - current ITrees node
   ************************************************************/
  updatePosition(that, node, instanceId): void {
    that.store.dispatch(updateNetworkPosition(node));
  }

  getNodePosition(instance): number {
    return !_.isNil(instance) ? instance.position : null;
  }

  getInfo(model, instance): ModelInformationItem[] {
    const modelInformation = !_.isEmpty(model)  ? [
      ModelInformationItem.createInstance("Min instances", !_.isNull(model.min) ? String(model.min) : null),
      this._sharedTreeService.createMaximumToInstantiateModelInformationItem(model)
    ] : [];

    if (!_.isEmpty(model) && !_.isEmpty(model.properties)) {
      modelInformation.push(ModelInformationItem.createInstance("Network role", model.properties.network_role))
    }

    const instanceInfo = !_.isEmpty(instance) ? [
        ModelInformationItem.createInstance("Route target id", instance.routeTargetId ? instance.routeTargetId : null),
        ModelInformationItem.createInstance("Route target role", instance.routeTargetRole ? instance.routeTargetRole : null)] :
      [];
    const result = [modelInformation, instanceInfo];
    return _.uniq(_.flatten(result));
  }
}
