import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import {MessageBoxData} from "../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../shared/components/messageBox/messageBox.service";
import * as _ from "lodash";
import {DrawingBoardModes} from "../drawing-board.modes";
import {AuditInfoModalComponent} from "../../../shared/components/auditInfoModal/auditInfoModal.component";
import {VnfModelInfo} from "./models/vnf/vnf.model.info";
import {ILevelNodeInfo} from "./models/basic.model.info";
import {ComponentInfoModel, ComponentInfoType} from "../component-info/component-info-model";
import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";

@Injectable()
export class SharedTreeService {
  private _sharedTreeService: SharedTreeService;
  constructor(private _store: NgRedux<AppState>) {
  }

  /***********************************************************
   * return if instance has missing data
   * @param instance - vnf instance
   * @param dynamicInputs - from the instance
   * @param isEcompGeneratedNaming
   ************************************************************/
  selectedVNF: string = null;


  getSelectedVNF(): string {
    return this.selectedVNF;
  }

  setSelectedVNF(node): void {
    if (_.isNil(node) || node.data.type !== 'VF') {
      this.selectedVNF = null;
    } else {
      this.selectedVNF = node.data.vnfStoreKey;
    }
  }

  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean, requiredFields: string[]): boolean {
    if (!isEcompGeneratedNaming && _.isEmpty(instance.instanceName)) {
      return true;
    }

    for (let field of requiredFields) {
      if (_.isEmpty(instance[field])) {
        return true;
      }
    }

    for (let field of dynamicInputs) {
      if (field.isRequired && !_.isNil(instance.instanceParams) && _.isEmpty(instance.instanceParams[0][field.id])) {
        return true;
      }
    }
    return false;
  }


  addingStatusProperty(node) {
    node['statusProperties'] = [];
    node['statusProperties'].push({key: 'Prov Status:', value: node.provStatus, testId: 'provStatus'});
    node['statusProperties'].push({key: 'Orch Status:', value: node.orchStatus, testId: 'orchStatus'});
    if (node.inMaint) {
      node['statusProperties'].push({key: 'In-maintenance', value: '', testId: 'inMaint'});
    }
    return node;
  }

  /**********************************************
   * should delete or remove child instance's
   "new" -> should remove
   !new" -> should change action status
   **********************************************/
  removeDeleteAllChild(node, serviceModelId: string, callback): void {
    for (let nodeChild of node.children) {
      if (nodeChild.data.action === ServiceInstanceActions.Create) {
        if (!_.isNil(nodeChild.data) && !_.isNil(nodeChild.data.menuActions) && !_.isNil(nodeChild.data.menuActions['remove'])) {
          nodeChild.data.menuActions['remove']['method'](nodeChild, serviceModelId);
        }
      } else {
        if (!_.isNil(nodeChild.data) && !_.isNil(nodeChild.data.menuActions) && !_.isNil(nodeChild.data.menuActions['delete'])) {
          nodeChild.data.menuActions['delete']['method'](nodeChild, serviceModelId);
        }
      }
    }
    callback(node, serviceModelId);
  }


  /**********************************************
   * should undo delete child instance's
   **********************************************/
  undoDeleteAllChild(node, serviceModelId: string, callback): void {
    for (let nodeChild of node.children) {
      if (!_.isNil(nodeChild.data) && !_.isNil(nodeChild.data.menuActions) && !_.isNil(nodeChild.data.menuActions['undoDelete'])) {
        nodeChild.data.menuActions['undoDelete']['method'](nodeChild, serviceModelId);
      }
    }
    callback(node, serviceModelId);
  }

  /**********************************************
   * should return true if can delete
   **********************************************/
  shouldShowDelete(node): boolean {
    const mode = this._store.getState().global.drawingBoardStatus;
    if (!_.isNil(node) && !_.isNil(node.data) && !_.isNil(node.data.action) && !_.isNil(node.data.menuActions['delete'])) {
      if (mode !== DrawingBoardModes.EDIT || node.data.action === ServiceInstanceActions.Create) {
        return false;
      } else if (node.data.action === ServiceInstanceActions.None) {
        return true
      }
      return false;
    }
    return false;
  }

  /**********************************************
   * should return true if can undo delete
   **********************************************/
  shouldShowUndoDelete(node): boolean {
    const mode = this._store.getState().global.drawingBoardStatus;
    if (mode === DrawingBoardModes.EDIT && !_.isNil(node.data.action) && !_.isNil(node.data.menuActions['undoDelete'])) {
      if (node.data.action === ServiceInstanceActions.Create || node.data.action === ServiceInstanceActions.Delete) {
        return false;
      } else if (node.data.action.split('_').pop() === 'Delete') {
        return true
      }
      return false;
    }
    return false;
  }
  /**********************************************
   * should return true if can remove or edit
   * enabled only on edit/design mode and for new instances
   **********************************************/
  shouldShowRemoveAndEdit(node): boolean {
    const mode = this._store.getState().global.drawingBoardStatus;
    if (!_.isNil(node) && !_.isNil(node.data) && !_.isNil(node.data.action) && node.data.action === ServiceInstanceActions.Create &&
      mode !== DrawingBoardModes.VIEW && mode !== DrawingBoardModes.RETRY) {
      return true;
    }
    return false;
  }
  /**********************************************
   * should return true if can duplicate by mode
   **********************************************/
  shouldShowDuplicate(node): boolean {
    const mode = this._store.getState().global.drawingBoardStatus;
    return !mode.includes('RETRY');
  }

  /**********************************************
   * should return true if can audit info
   **********************************************/
  shouldShowAuditInfo(node): boolean {
    return this.isRetryMode() || (!_.isNil(node.data) && !_.isNil(node.data.action) && node.data.action !== ServiceInstanceActions.Create);
  }


  isRetryMode(): boolean {
    const mode = this._store.getState().global.drawingBoardStatus;
    return mode.includes('RETRY');
  }


  /**********************************************
   * should return true if can add node instances
   **********************************************/
  shouldShowAddIcon(): boolean{
    const mode = this._store.getState().global.drawingBoardStatus;
    return mode === DrawingBoardModes.EDIT || mode=== DrawingBoardModes.CREATE;
  }
  /************************************************
   return number of instances with action Delete
   @type: vnfs networks, vngGroups (not vfModule)
   @node : node model from the left tree
   ************************************************/
  getExistingInstancesWithDeleteMode(node, serviceModelId: string, type: string): number {
    let counter = 0;
    const existingInstances = this._store.getState().service.serviceInstance[serviceModelId][type];
    const modelUniqueId = node.data.modelUniqueId;
    if (!_.isNil(existingInstances)) {
      for (let instanceKey in existingInstances) {
        if (!_.isNil(existingInstances[instanceKey].action)) {
          if (existingInstances[instanceKey].modelInfo.modelUniqueId === modelUniqueId && existingInstances[instanceKey].action.split('_').pop() === 'Delete') {
            counter++;
          }
        }
      }
    }
    return counter;
  }


  isServiceOnDeleteMode(serviceId: string): boolean {
    return this._store.getState().service.serviceInstance[serviceId].action === ServiceInstanceActions.Delete;
  }


  openModal(node : any | any[] , serviceModelId : string, cb : Function) : void {
    let type: string = _.isArray(node) ? 'Service' : node.data.typeName;
    let messageBoxData: MessageBoxData = new MessageBoxData(
      "Mark for Delete",
      `You are about to mark for delete this ${type} this will also mark all its children and remove all new instances just added`,
      <any>"warning",
      <any>"md",
      [
        {
          text: "Mark and remove",
          size: "large",
          callback: cb.bind(this, node, serviceModelId),
          closeModal: true
        },
        {text: "Don’t Remove", size: "medium", closeModal: true}
      ]);

    MessageBoxService.openModal.next(messageBoxData);
  }

  someChildHasCreateAction(nodes: any | any[]) : boolean {
    let nodesArr = _.isArray(nodes) ? nodes : [nodes];
    for(const node of nodesArr){
      if(node.action === ServiceInstanceActions.Create) {return true;}
      if(node.children){
        for (let nodeChild of node.children) {
          if (nodeChild.action === ServiceInstanceActions.Create) {
            return true;
          }
          if(nodeChild.children && nodeChild.children.length > 0){
            for(let child of nodeChild.children){
              let hasCreateAction = this.someChildHasCreateAction(child);
              if(hasCreateAction) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  shouldShowDeleteInstanceWithChildrenModal(node : any | any[] , serviceModelId : string, cb : Function) : void {
    if(this.someChildHasCreateAction(node)){
      this.openModal(node , serviceModelId, cb);
    }else {
      cb(node, serviceModelId)
    }
  }


  isFailed(node): boolean {
    return !_.isNil(node.data) ? node.data.isFailed : false;
  }

  /************************************************
   in a case the node is failed e.g. not instantiated correctly
   the function will call to openRetryInstanceAuditInfoModal
   @node : node model from the left tree
   @serviceModelId : serviceModelId
   @instance : instance
   @instanceType: instanceType
   @modelInfoService : the model (vnf, vfmodule, network, vnfgroup)object that call to the function (this)
   ************************************************/
  openAuditInfoModal(node, serviceModelId, instance, instanceType, modelInfoService : ILevelNodeInfo){
    AuditInfoModalComponent.openInstanceAuditInfoModal.next({
      instanceId: serviceModelId,
      type: instanceType,
      model: modelInfoService.getModel(node.data.modelName, instance, this._store.getState().service.serviceHierarchy[serviceModelId]),
      instance
    });
  }


  addGeneralInfoItems(modelInfoSpecificItems: ModelInformationItem[], type: ComponentInfoType, model, instance):ComponentInfoModel {
    let modelInfoItems: ModelInformationItem[] = [
      ModelInformationItem.createInstance("Model version", model ? model.version : null),
      ModelInformationItem.createInstance("Model customization ID", model ? model.customizationUuid : null),
      ModelInformationItem.createInstance("Instance ID", instance ? instance.instanceId : null),
      ModelInformationItem.createInstance("Instance type", instance ? instance.instanceType : null),
      ModelInformationItem.createInstance("In maintenance", instance? instance.inMaint : null),
    ];
    modelInfoItems = modelInfoItems.concat(modelInfoSpecificItems);
    return this.getComponentInfoModelByModelInformationItems(modelInfoItems, type, instance);
  }

  getComponentInfoModelByModelInformationItems(modelInfoItems: ModelInformationItem[], type: ComponentInfoType, instance){
    const modelInfoItemsWithoutEmpty = _.filter(modelInfoItems, function(item){ return !item.values.every(_.isNil)});
    return new ComponentInfoModel(type, modelInfoItemsWithoutEmpty, [], instance != null);
  }
}
