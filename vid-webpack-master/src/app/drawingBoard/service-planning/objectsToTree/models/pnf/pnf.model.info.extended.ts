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
import {changeInstanceCounter, removeInstance} from "../../../../../shared/storeUtil/utils/general/general.actions";
import {MessageBoxData} from "../../../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../../../shared/components/messageBox/messageBox.service";
import {IframeService} from "../../../../../shared/utils/iframe.service";
import {DuplicateService} from "../../../duplicate/duplicate.service";
import {ModalService} from "../../../../../shared/components/customModal/services/modal.service";
import {
  deleteActionPnfInstance,
  undoDeleteActionPnfInstance,
  updatePnfPosition
} from "../../../../../shared/storeUtil/utils/pnf/pnf.actions";
import {DynamicInputsService} from "../../dynamicInputs.service";

export class PnfModelInfoExtended implements ILevelNodeInfo {

  constructor(
    private _store: NgRedux<AppState>,
    private _sharedTreeService: SharedTreeService,
    private _dialogService: DialogService,
    private _pnfPopupService: PnfPopupService,
    private _iframeService: IframeService,
    private _duplicateService: DuplicateService,
    private modalService: ModalService,
    private _dynamicInputsService: DynamicInputsService
  ) {}

  name: string = 'pnfs';
  type: string = 'PNF';
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

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return <any>{
      edit: {
        method: (node, serviceModelId) => {
          this._iframeService.addClassOpenModal('content');
          this._dialogService.addDialog(GenericFormPopupComponent, {
            type: PopupType.PNF,
            uuidData: <any>{
              serviceId: serviceModelId,
              modelName: node.data.modelName,
              pnfStoreKey: node.data.pnfStoreKey,
              modelId: node.data.modelId,
              type: node.data.type,
              popupService: this._pnfPopupService
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
          const instance = this._store.getState().service.serviceInstance[serviceModelId].pnfs[node.data.pnfStoreKey];
          this._sharedTreeService.openAuditInfoModal(node, serviceModelId, instance, 'PNF', this);
        },
        visible: (node) => this._sharedTreeService.shouldShowAuditInfo(node),
        enable: (node) => this._sharedTreeService.shouldShowAuditInfo(node)
      },
      remove: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            let storeKey: string = node.data.pnfStoreKey;
            this._store.dispatch(removeInstance(node.data.pnfStoreKey, serviceModelId, storeKey, node));
            this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceModelId, -1, node));
            this._sharedTreeService.selectedNF = null;
          } else {
            let messageBoxData: MessageBoxData = new MessageBoxData(
              "Remove PNF",  // modal title
              "You are about to remove this PNF from this service. Are you sure you want to remove it?",
              <any>"warning",
              <any>"md",
              [
                {
                  text: "Remove PNF",
                  size: "large",
                  callback: this.removePnf.bind(this, node, serviceModelId),
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
            this._store.dispatch(deleteActionPnfInstance(node.data.pnfStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.shouldShowDeleteInstanceWithChildrenModal(node, serviceModelId, (node, serviceModelId) => {
              this._sharedTreeService.removeDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
                this._store.dispatch(deleteActionPnfInstance(node.data.pnfStoreKey, serviceModelId));
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
            this._store.dispatch(undoDeleteActionPnfInstance(node.data.pnfStoreKey, serviceModelId));
          } else {
            this._sharedTreeService.undoDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
              this._store.dispatch(undoDeleteActionPnfInstance(node.data.pnfStoreKey, serviceModelId));
            });
          }
        },
        visible: (node) => this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => this._sharedTreeService.shouldShowUndoDelete(node) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
      }
    }
  }

  removePnf(this, node, serviceModelId) {
    this._store.dispatch(removeInstance(node.data.modelName, serviceModelId, node.data.pnfStoreKey, node));
    this._store.dispatch(changeInstanceCounter(node.data.modelUniqueId, serviceModelId, -1, node));
    this._sharedTreeService.selectedNF = null;
  }

  getModel = (instanceModel: any): PNFModel => {
    return new PNFModel(instanceModel);
  };

  getNextLevelObject(): any {
    return null;
  }

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

  /***********************************************************
   * return if user should provide instance name or not.
   * @param currentModel - current Model object
   ************************************************************/
  isEcompGeneratedNaming = (currentModel): boolean => {
    const ecompGeneratedNaming = currentModel.properties.ecomp_generated_naming;
    return ecompGeneratedNaming === "true";
  };

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

  updateDynamicInputsDataFromModel = (currentModel): any => {
    let displayInputs = _.isNil(currentModel) ? [] : currentModel.inputs;
    return _.isEmpty(displayInputs) ? [] : this._dynamicInputsService.getArbitraryInputs(displayInputs);
  };

  updatePosition(that, node, instanceId): void {
    that.store.dispatch(updatePnfPosition(node, instanceId, node.vnfStoreKey));
  }
}
