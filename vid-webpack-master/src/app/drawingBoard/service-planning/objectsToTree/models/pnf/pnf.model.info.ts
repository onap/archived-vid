import {ILevelNodeInfo} from "../basic.model.info";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {PnfInstance} from "app/shared/models/pnfInstance";
import {PNFModel} from "../../../../../shared/models/pnfModel";
import {SharedTreeService} from "../../shared.tree.service";
import {NodeModelResponseInterface} from "../../../../../shared/models/nodeModel";


export class PnfModelInfo implements ILevelNodeInfo{

  constructor(
    private _sharedTreeService: SharedTreeService,
  ){}

  name: string = 'pnfs';
  type: string ='PNF';
  typeName: string = 'PNF';
  childNames: string[];
  componentInfoType = ComponentInfoType.PNF;

  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => null;

  getInfo(model, instance): ModelInformationItem[] {
    return [];
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function; visible: Function; enable: Function } } {
    return {};
  }

  getModel = (instanceModel: any): PNFModel => {
    return new PNFModel(instanceModel);
  };

  getNextLevelObject(): any { return null;  }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    return 0;
  }

  getNodePosition(instance): number {return 0;  }

  getTooltip = (): string => 'PNF';


  getType = (): string => 'pnf';


  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return false;
  }

  isEcompGeneratedNaming(currentModel): boolean {
    return false;
  }

  onClickAdd(node: ITreeNode, serviceModelId: string): void {
  }

  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    return null;
  }

  updateDynamicInputsDataFromModel = (currentModel): any => [];

  updatePosition(that, node, instanceId): void {
  }

}
