import {ILevelNodeInfo} from "../basic.model.info";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {Level1Model} from "../../../../../shared/models/nodeModel";
import {NcfTreeNode} from "../../../../../shared/models/ncfTreeNode";
import {Level1Instance} from "../../../../../shared/models/level1Instance";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {
  deleteActionNcfInstance,
  undoDeleteActionNcfInstance
} from "../../../../../shared/storeUtil/utils/ncf/ncf.actions";
import * as _ from 'lodash';
import {NcfModelInterface} from "../../../../../shared/models/ncfModel";

export class NcfModelInfo implements ILevelNodeInfo {
  constructor(private _store: NgRedux<AppState>){
  }

  name: string = 'ncfs';
  type: string = 'NCF';
  typeName: string = 'NCF';

  childNames: string[] = [];
  componentInfoType = ComponentInfoType.NCF;

  isEcompGeneratedNaming(currentModel): boolean {
    return false;
  }

  updateDynamicInputsDataFromModel = (currentModel): any => [];

  getModel = (instanceModel: any): any => {
    return new Level1Model();
  };


  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => {
    let modelVersion: string = null;
    if (parentModel.networksCollection && instance.originalName) {
      const ncfRealModel: NcfModelInterface = parentModel.networksCollection[instance.originalName];
      if (ncfRealModel) {
        modelVersion = ncfRealModel.version;
      }
    }

    let node = new NcfTreeNode(instance, model, storeKey, modelVersion);
    node.menuActions = this.getMenuAction(<any>node, serviceModelId);
    node.typeName = this.typeName;
    return node;
  };


  getNextLevelObject = (): any => {
    return null;
  };

  getTooltip = (): string => 'NCF';

  getType = (): string => 'NCF';

  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return false;
  }

  onClickAdd(node, serviceModelId: string): void {

  }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    return 0;
  }

  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    return null;
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return <any>{
      delete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(deleteActionNcfInstance(node.data.parent.collectionResourceStoreKey, node.data.storeKey, serviceModelId));
        },
        visible: (node) => false,
        enable: (node) => false
      },
      undoDelete: {
        method: (node, serviceModelId) => {
          this._store.dispatch(undoDeleteActionNcfInstance(node.data.parent.collectionResourceStoreKey, node.data.storeKey, serviceModelId));
        },
        visible: (node) => false,
        enable: (node) => false
      }

    };
  }

  updatePosition(that, node, instanceId): void {
  }

  getNodePosition(instance): number {
    return 0;
  }

  getInfo(model, instance: NcfTreeNode): ModelInformationItem[] {

    if (_.isNil(instance)) {
      return [];
    }

    return [
      ModelInformationItem.createInstance('Role', instance.instanceGroupRole),
      ModelInformationItem.createInstance('Collection function', instance.instanceGroupFunction),
      ModelInformationItem.createInstance('Number of networks', instance.numberOfNetworks),
    ];
  }
}

