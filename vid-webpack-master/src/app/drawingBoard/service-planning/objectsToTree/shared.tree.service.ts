import {Injectable} from "@angular/core";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../shared/store/reducers";
import {ServiceInstanceActions} from "../../../shared/models/serviceInstanceActions";
import {MessageBoxData} from "../../../shared/components/messageBox/messageBox.data";
import {MessageBoxService} from "../../../shared/components/messageBox/messageBox.service";
import * as _ from "lodash";
import {DrawingBoardModes} from "../drawing-board.modes";
import {AuditInfoModalComponent} from "../../../shared/components/auditInfoModal/auditInfoModal.component";
import {ILevelNodeInfo} from "./models/basic.model.info";
import {ComponentInfoModel, ComponentInfoType} from "../component-info/component-info-model";
import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";
import {undoUpgradeService, upgradeService} from "../../../shared/storeUtil/utils/service/service.actions";
import {VNFMethods} from "../../../shared/storeUtil/utils/vnf/vnf.actions";
import {FeatureFlagsService, Features} from "../../../shared/services/featureFlag/feature-flags.service";
import {Utils} from "../../../shared/utils/utils";
import {Constants} from "../../../shared/utils/constants";
import {NodeInstance} from "../../../shared/models/nodeInstance";

@Injectable()
export class SharedTreeService {
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

  /**
   * Determines a consistent unique ID for a given right-tree
   * node instance.
   */
  modelUniqueId = (nodeInstance: NodeInstance): string => {
    return _.isNil(nodeInstance.modelInfo)
      ? null
      : (nodeInstance.modelInfo.modelCustomizationId || nodeInstance.modelInfo.modelInvariantId);
  };

  modelUniqueNameOrId = (instance): string => {
    if (_.isNil(instance)) {
      return null;
    }

    const innerInstance = _.find(instance) || {};

    return instance.originalName
      || this.modelUniqueId(instance)
      || innerInstance.originalName
      || this.modelUniqueId(innerInstance);
  };

  /**
   * Finds a model inside a full service model
   * @param serviceModelFromHierarchy
   * @param modelTypeName "vnfs" | "networks" | "vfModules" | "collectionResources" | ...
   * @param modelUniqueNameOrId Either an entry name (i.e. "originalName"), modelCustomizationId or modelInvariantId.
   *                      Note that modelInvariantId will work only where model lacks a modelCustomizationId.
   * @param modelName An optional entry name (i.e. "originalName"); will not try to use as id
   */
  modelByIdentifiers = (serviceModelFromHierarchy, modelTypeName: string, modelUniqueNameOrId: string, modelName?: string): any => {
    const logErrorAndReturnUndefined = () =>
      console.info(`modelByIdentifiers: could not find a model matching query`, {
        modelTypeName, modelUniqueNameOrId, modelName, serviceModelFromHierarchy
      });

    if (_.isNil(serviceModelFromHierarchy)) return logErrorAndReturnUndefined();

    const modelsOfType = serviceModelFromHierarchy[modelTypeName];
    if (_.isNil(modelsOfType)) return logErrorAndReturnUndefined();

    const modelIfModelIdentifierIsEntryName = modelsOfType[modelUniqueNameOrId];
    const modelIfModeNameExists = _.isNil(modelName) ? null : modelsOfType[modelName];

    if (!_.isNil(modelIfModelIdentifierIsEntryName)) {
      return modelIfModelIdentifierIsEntryName;
    } else if (!_.isNil(modelIfModeNameExists)) {
      return modelIfModeNameExists;
    } else {
      // try modelUniqueNameOrId as an id
      return _.find(modelsOfType, o => (o.customizationUuid || o.invariantUuid) === modelUniqueNameOrId) || logErrorAndReturnUndefined()
    }
  };

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
  shouldShowDelete(node, serviceModelId): boolean {
    return this.shouldShowButtonGeneric(node, "delete", serviceModelId)
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
   * enabled only on edit/design
   * enabled only if there's a newer version for VNF-M
   **********************************************/
  upgradeBottomUp(node,serviceModelId: string): void {
    this.iterateOverTreeBranchAndRunAction(node, serviceModelId, VNFMethods.UPGRADE);
    this._store.dispatch(upgradeService(serviceModelId));
  }

  private iterateOverTreeBranchAndRunAction(node, serviceModelId: string, actionMethod) {
    while (_.has(node.parent, 'data') && _.has(node.parent.data, 'menuActions')
    && !_.isNil(node.parent.data.menuActions[actionMethod])) {
      node = node.parent;
      node.data.menuActions[actionMethod]['method'](node, serviceModelId);
    }
  }

  /****************************************************
   * should return true if customer can upgrade a VFM *
   ****************************************************/
  shouldShowUpgrade(node, serviceModelId): boolean {
      return (this.isVfMoudleCouldBeUpgraded(node, serviceModelId))
        && this.shouldShowButtonGeneric(node, VNFMethods.UPGRADE, serviceModelId) ;
    }

  isVfMoudleCouldBeUpgraded(node, serviceModelId): boolean{
    return (FeatureFlagsService.getFlagState(Features.FLAG_FLASH_REPLACE_VF_MODULE, this._store) &&
      (this.isThereAnUpdatedLatestVersion(serviceModelId) || this.isVfModuleCustomizationIdNotExistsOnModel(node, serviceModelId)))
  }

  isVfModuleCustomizationIdNotExistsOnModel(vfModuleNode, serviceModelId) {

    // prevent undefined
    if (_.isNil(vfModuleNode.data) || _.isNil(vfModuleNode.data.modelCustomizationId)) {
      return false;
    }

    let vfModulesHierarchyByGivenModelId = this._store.getState().service.serviceHierarchy[serviceModelId].vfModules;
    return  !_.some(vfModulesHierarchyByGivenModelId, vfmodel => vfmodel.customizationUuid === vfModuleNode.data.modelCustomizationId);
  }


  isVfmoduleAlmostPartOfModelOnlyCustomizationUuidDiffer(vfModuleNode, serviceModelId) : boolean {
    /*
    for `true`, should all:
    1. parent vnf found by model-mane
    2. vfmodule found by invariant
    3. vfmodule diff by customization
     */

    if (_.isNil(vfModuleNode.data)) {
      return false;
    }

    const vnfHierarchy = this.getParentVnfHierarchy(vfModuleNode, serviceModelId);
    if (_.isNil(vnfHierarchy)) {
      return false;
    }

    const vfModuleHierarchyByInvariantId =  this.getVfModuleHFromVnfHierarchyByInvariantId(vfModuleNode, vnfHierarchy);
    if(_.isNil(vfModuleHierarchyByInvariantId)){
      return false;
    }

    return vfModuleHierarchyByInvariantId.customizationUuid
      && (vfModuleHierarchyByInvariantId.customizationUuid !== vfModuleNode.data.modelCustomizationId);
  }

  getParentVnfHierarchy(vfModuleNode, serviceModelId) {
    if (vfModuleNode.parent && vfModuleNode.parent.data) {
      return this._store.getState().service.serviceHierarchy[serviceModelId].vnfs[vfModuleNode.parent.data.modelName];
    } else {
      return null;
    }
  }

  getVfModuleHFromVnfHierarchyByInvariantId(vfModuleNode, parentVnfHierarchy) {
    if(vfModuleNode.data.modelInvariantId && parentVnfHierarchy && parentVnfHierarchy.vfModules){
      return _.find(parentVnfHierarchy.vfModules, o => o.invariantUuid === vfModuleNode.data.modelInvariantId);
    }
    return null;
  }


  isThereAnUpdatedLatestVersion(serviceModelId) : boolean{
    let serviceInstance = this.getServiceInstance(serviceModelId);
    return !_.isNil(serviceInstance.latestAvailableVersion) && (Number(serviceInstance.modelInfo.modelVersion) < serviceInstance.latestAvailableVersion);
  }

  private getServiceInstance(serviceModelId): any {
    return this._store.getState().service.serviceInstance[serviceModelId];
  }

  shouldShowButtonGeneric(node, method, serviceModelId) {
    const mode = this._store.getState().global.drawingBoardStatus;
    const isMacro = !(this.getServiceInstance(serviceModelId).isALaCarte);

    if (isMacro) { //if macro action allowed only for service level
      return false;
    }

    if (!_.isNil(node) && !_.isNil(node.data) && !_.isNil(node.data.action) && !_.isNil(node.data.menuActions[method])) {
      if (mode !== DrawingBoardModes.EDIT || node.data.action === ServiceInstanceActions.Create) {
        return false;
      }
      else if (node.data.action === ServiceInstanceActions.None) {
        return true
      }
    }
    return false;
  }

  /**********************************************
   * return boolean according to
   * current defined action of VFModule node
   **********************************************/
  shouldShowUndoUpgrade(node): boolean {
    const mode = this._store.getState().global.drawingBoardStatus;
    if (mode === DrawingBoardModes.EDIT && !_.isNil(node.data.action) && !_.isNil(node.data.menuActions[VNFMethods.UNDO_UPGRADE])) {
      if (node.data.action === ServiceInstanceActions.Upgrade) {
        return false;
      } else if (node.data.action.split('_').pop() === ServiceInstanceActions.Upgrade) {
        return true
      }
      return false;
    }
    return false;
  }
  /**********************************************
   * enabled only on edit/design
   * enabled only if there's a newer version for VNF-M
   **********************************************/
  undoUpgradeBottomUp(node,serviceModelId: string): void {
    this.iterateOverTreeBranchAndRunAction(node, serviceModelId, VNFMethods.UNDO_UPGRADE);
    this._store.dispatch(undoUpgradeService(serviceModelId));
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
    return mode === DrawingBoardModes.EDIT || mode=== DrawingBoardModes.CREATE || mode=== DrawingBoardModes.RECREATE;
  }


  isReachedToMaxInstances(properties, counter, flags): boolean{
    let maxInstances  = Utils.getMaxFirstLevel(properties, flags);
    if(_.isNil(maxInstances)){
      return false;
    }else {
      return !(maxInstances > counter);
    }
  }
  /************************************************
   return number of instances with action Delete
   @type: vnfs networks, vngGroups (not vfModule)
   @node : node model from the left tree
   ************************************************/
  getExistingInstancesWithDeleteMode(node, serviceModelId: string, type: string): number {
    let counter = 0;
    const existingInstances = this.getServiceInstance(serviceModelId)[type];
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
      model: modelInfoService.getModel(
        this.modelByIdentifiers(
          this._store.getState().service.serviceHierarchy[serviceModelId],
          modelInfoService.name,
          this.modelUniqueNameOrId(instance), node.data.modelName
        )
      ),
      instance
    });
  }

  getModelVersionEitherFromInstanceOrFromHierarchy(selectedNodeData, model): string | undefined {
    return this.getNamedFieldFromInstanceOrFromHierarchy(selectedNodeData, "modelVersion", model, "version");
  }

  getModelCustomizationIdEitherFromInstanceOrFromHierarchy(selectedNodeData, model): string | undefined {
    return this.getNamedFieldFromInstanceOrFromHierarchy(selectedNodeData, "modelCustomizationId", model, "customizationUuid");
  }

  getGetModelInvariantIdEitherFromInstanceOrFromHierarchy(selectedNodeData, model): string | undefined {
    return this.getNamedFieldFromInstanceOrFromHierarchy(selectedNodeData, "modelInvariantId", model, "invariantUuid");
  }

  getModelVersionIdEitherFromInstanceOrFromHierarchy(selectedNodeData, model): string | undefined {
    return this.getNamedFieldFromInstanceOrFromHierarchy (selectedNodeData, "modelVersionId", model, "uuid");
  }



  getNamedFieldFromInstanceOrFromHierarchy(selectedNodeData, instanceModelInfoFieldName, model, modelFieldName): string | undefined {
    if (instanceModelInfoFieldName && selectedNodeData && selectedNodeData.instanceModelInfo
      && selectedNodeData.instanceModelInfo[instanceModelInfoFieldName]) {
      return selectedNodeData.instanceModelInfo[instanceModelInfoFieldName];
    } else if (modelFieldName && model && model[modelFieldName]) {
      return model[modelFieldName];
    }
    return undefined;
  }

  addGeneralInfoItems(modelInfoSpecificItems: ModelInformationItem[], type: ComponentInfoType, model, selectedNodeData):ComponentInfoModel {
    let modelInfoItems: ModelInformationItem[] = [
      ModelInformationItem.createInstance("Model version", this.getModelVersionEitherFromInstanceOrFromHierarchy(selectedNodeData, model)),
      ModelInformationItem.createInstance("Model customization ID", this.getModelCustomizationIdEitherFromInstanceOrFromHierarchy(selectedNodeData, model)),
      ModelInformationItem.createInstance("Instance ID", selectedNodeData ? selectedNodeData.instanceId : null),
      ModelInformationItem.createInstance("Instance type", selectedNodeData ? selectedNodeData.instanceType : null),
      ModelInformationItem.createInstance("In maintenance", selectedNodeData? selectedNodeData.inMaint : null),
    ];
    modelInfoItems = modelInfoItems.concat(modelInfoSpecificItems);
    return this.getComponentInfoModelByModelInformationItems(modelInfoItems, type, selectedNodeData);
  }

  getComponentInfoModelByModelInformationItems(modelInfoItems: ModelInformationItem[], type: ComponentInfoType, instance){
    const modelInfoItemsWithoutEmpty = _.filter(modelInfoItems, function(item){ return !item.values.every(_.isNil)});
    return new ComponentInfoModel(type, modelInfoItemsWithoutEmpty, [], instance != null);
  }

  createMaximumToInstantiateModelInformationItem(model): ModelInformationItem {
    return ModelInformationItem.createInstance(
      "Max instances",
      !_.isNil(model.max) ? String(model.max) : Constants.ModelInfo.UNLIMITED_DEFAULT
    );
  }
}
