import {ILevelNodeInfo} from "../basic.model.info";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {CollectionResourceTreeNode} from "../../../../../shared/models/collectionResourceTreeNode";
import {CollectionResourceInstance} from "../../../../../shared/models/collectionResourceInstance";
import {CollectionResourceModel} from "../../../../../shared/models/collectionResourceModel";
import * as _ from 'lodash';
import {NcfModelInfo} from "../ncf/ncf.model.info";

import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {deleteActionCrInstance, undoDeleteActionCrInstance} from "../../../../../shared/storeUtil/utils/cr/cr.actions";
import {SharedTreeService} from "../../shared.tree.service";

export class CollectionResourceModelInfo implements ILevelNodeInfo{
  constructor(private _store: NgRedux<AppState>,private  _sharedTreeService: SharedTreeService){
  }
  name: string = 'collectionResources';
  type : string = 'collection Resource';
  typeName : string = 'CR';
  childNames: string[] = ['ncfs'];
  componentInfoType = ComponentInfoType.COLLECTION_RESOURCE;


  isEcompGeneratedNaming(currentModel): boolean {return false; }

  updateDynamicInputsDataFromModel = (currentModel): any => [];

  getModel = (instanceModel): CollectionResourceModel => {
    return new CollectionResourceModel(instanceModel);
  };

  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => {
      let node = new CollectionResourceTreeNode(instance, model, storeKey);
      node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
      node.typeName = this.typeName;
      node.menuActions = this.getMenuAction(<any>node, serviceModelId);
      node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
      node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
      return node;
  };

  getNextLevelObject = (): any => {
    return new NcfModelInfo(this._store);
  }

  getTooltip = (): string => 'Collection Resource';

  getType = (): string => 'collectionResource';

  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {return false; }

  onClickAdd(node, serviceModelId: string): void { }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {return 0;}

  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons { return null;}

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return <any>{
      delete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(deleteActionCrInstance(node.data.collectionResourceStoreKey, serviceModelId));
        }
        else {
          this._sharedTreeService.removeDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
            this._store.dispatch(deleteActionCrInstance(node.data.collectionResourceStoreKey, serviceModelId));
          })
        }},
        visible: (node) => false,
        enable: (node) => false
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          if ((!_.isNil(node.data.children) && node.data.children.length === 0) || _.isNil(node.data.children)) {
            this._store.dispatch(undoDeleteActionCrInstance(node.data.collectionResourceStoreKey, serviceModelId));
          }
          else {
            this._sharedTreeService.undoDeleteAllChild(node, serviceModelId, (node, serviceModelId) => {
              this._store.dispatch(undoDeleteActionCrInstance(node.data.collectionResourceStoreKey, serviceModelId));
            })
          }

        },
        visible: (node) => (node) => false,
        enable: (node) => (node) => false
      }

    };
  }

  updatePosition(that , node, instanceId): void {}

  getNodePosition(instance): number { return 0; }

  getInfo(model, instance): ModelInformationItem[] {return [];}
}
