import {ILevelNodeInfo} from "../basic.model.info";
import {SharedTreeService} from "../../shared.tree.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import * as _ from "lodash";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {DynamicInputsService} from "../../dynamicInputs.service";
import {
  deleteActionRelatedVnfMemberInstance,
  removeRelatedVnfMemberInstance,
  undoDeleteActionRelatedVnfMemberInstance
} from "../../../../../shared/storeUtil/utils/relatedVnfMember/relatedVnfMember.actions";
import {VnfInstance} from "../../../../../shared/models/vnfInstance";
import {VNFModel} from "../../../../../shared/models/vnfModel";
import {VnfTreeNode} from "../../../../../shared/models/vnfTreeNode";
import {InputType} from "../../../../../shared/models/inputTypes";

export class RelatedVnfMemberInfoModel implements ILevelNodeInfo {
  constructor(private _sharedTreeService: SharedTreeService,
              private _dynamicInputsService : DynamicInputsService,
              private _store: NgRedux<AppState>) {
  }

  name: string = 'vnfs';
  type: string = 'relatedVnfMember';
  typeName: string = 'VNF';

  /***********************************************************
   * return if user should provide instance name or not.
   * @param currentModel - current Model object
   ************************************************************/
  isUserProvidedNaming = (currentModel): boolean => {
    const ecompGeneratedNaming = currentModel.properties.ecomp_generated_naming;
    return ecompGeneratedNaming !== undefined && ecompGeneratedNaming === "false";
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
  getModel = (vnfModelId: string, instance: VnfInstance, serviceHierarchy): VNFModel => {
    const originalModelName = instance.originalName ? instance.originalName : vnfModelId;
    return new VNFModel(serviceHierarchy[this.name][originalModelName]);
  };


  /***********************************************************
   * return vnf instance tree node
   * @param instance - vnf instance
   * @param model - vnf model
   * @param parentModel
   * @param storeKey - store key if exist
   ************************************************************/
  createInstanceTreeNode = (instance: VnfInstance, model: VNFModel, parentModel, storeKey: string): VnfTreeNode => {
    let node = new VnfTreeNode(instance, model, storeKey);
    node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
    node.typeName = this.typeName;
    node.menuActions = this.getMenuAction(<any>node, model.uuid);
    node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage: "";
    node = this._sharedTreeService.addingStatusProperty(node);
    return node;
  };

  /***********************************************************
   * return next level object (null because is last level)
   ************************************************************/
  getNextLevelObject(): any {
    return null;
  }

  /***********************************************************
   * return if instance has missing data
   * @param instance - vnf instance
   * @param dynamicInputs
   * @param isUserProvidedNaming
   ************************************************************/
  hasMissingData(instance, dynamicInputs: any, isUserProvidedNaming: boolean): boolean {
    return this._sharedTreeService.hasMissingData(instance, dynamicInputs, isUserProvidedNaming, [InputType.LCP_REGION, InputType.TENANT, InputType.PLATFORM]);
  }

  getTooltip = (): string => 'VF';

  getType = (): string => 'VF';

  /***********************************************************
   * doesn't have option from DrawingBoard model tree
   ************************************************************/
  onClickAdd(node, serviceModelId: string): void {}

  /***********************************************************
   * doesn't have option from DrawingBoard model tree
   ************************************************************/
  getNodeCount(node: ITreeNode, serviceModelId: string): number {return 0;}

  /***********************************************************
   * doesn't have option from DrawingBoard model tree
   ************************************************************/
  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    return new AvailableNodeIcons(false, false);
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function }} {
    return <any>{
      remove: {
        method: (node, serviceModelId) => {
          this._store.dispatch(removeRelatedVnfMemberInstance(node.parent.data.vnfGroupStoreKey, node.data.vnfStoreKey, serviceModelId));
        },
        visible: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node),
        enable: (node) => this._sharedTreeService.shouldShowRemoveAndEdit(node)
      },
      delete : {
        method : (node, serviceModelId) => {
          this._store.dispatch(deleteActionRelatedVnfMemberInstance(node.parent.data.vnfGroupStoreKey, node.data.vnfStoreKey, serviceModelId));
        },
        visible: (node) => this._sharedTreeService.shouldShowDelete(node),
        enable: (node) => this._sharedTreeService.shouldShowDelete(node)
      },
      undoDelete : {
        method : (node, serviceModelId) => {
          this._store.dispatch(undoDeleteActionRelatedVnfMemberInstance(node.parent.data.vnfGroupStoreKey , node.data.vnfStoreKey, serviceModelId));

        },
        visible: (node) => this._sharedTreeService.shouldShowUndoDelete(node),
        enable: (node, serviceModelId) => this._sharedTreeService.shouldShowUndoDelete(node) && this._sharedTreeService.shouldShowDelete(node.parent) && !this._sharedTreeService.isServiceOnDeleteMode(serviceModelId)
      }
    }
  }

  isEcompGeneratedNaming = (currentModel): boolean => {
    const ecompGeneratedNaming = currentModel.properties.ecomp_generated_naming;
    return ecompGeneratedNaming === "true";
  };


  updatePosition(that , node, instanceId): void {
    // TODO
  }

  getNodePosition(instance): number {
    return null;
  }

  onSelectedNode(node: ITreeNode): void {
  }
}
