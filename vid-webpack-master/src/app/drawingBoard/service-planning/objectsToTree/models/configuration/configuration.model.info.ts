import {ILevelNodeInfo} from "../basic.model.info";
import {DynamicInputsService} from "../../dynamicInputs.service";
import * as _ from 'lodash';
import {SharedTreeService} from "../../shared.tree.service";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";

export class ConfigurationModelInfo implements ILevelNodeInfo{
  constructor(private _dynamicInputsService : DynamicInputsService,
              private _sharedTreeService : SharedTreeService){}
  name: string = 'configurations';
  type : string = 'Configuration';
  typeName : string = 'C';
  componentInfoType = ComponentInfoType.CONFIGURATION;

  isEcompGeneratedNaming = () : boolean => true;

  updateDynamicInputsDataFromModel = () : any => [];

  getNextLevelObject = (): any => null;

  getTooltip = () : string => 'Configuration';

  getType = () : string => "Configuration";

  /***********************************************************
   * @param instanceModel - The model of the instance (usually extracted from serviceHierarchy store)
   ************************************************************/
  getModel = (instanceModel: any) : any =>{
    if (!_.isNil(instanceModel)) {
      return instanceModel;
    }
    return {};
  };

  createInstanceTreeNode(instance: any, model: any, storeKey: string, serviceModelId: string): any {return null;}

  childNames: string[];

  /***********************************************************
   * return if instance has missing data
   * @param instance - vnf instance
   * @param dynamicInputs
   * @param isEcompGeneratedNaming
   ************************************************************/
  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return this._sharedTreeService.hasMissingData(instance, dynamicInputs, isEcompGeneratedNaming, []);
  }

  onClickAdd(node: ITreeNode, serviceModelId: string): void {
  }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    return 0;
  }

  /***********************************************************
   * should show node icon
   * @param node - current ITrees node
   * @param serviceModelId - service id
   ************************************************************/
  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    return null;
  }

  getMenuAction(node: ITreeNode, serviceModelId : string){
    return {

    }
  }

  updatePosition(that , node, instanceId): void {
    // TODO
  }

  getNodePosition(instance): number {
    return null;
  }

  onSelectedNode(node: ITreeNode): void {
  }

  getInfo(model, instance): ModelInformationItem[] {
    return [];
  }

}
