import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../available-models-tree/available-models-tree.service";
import {ComponentInfoType} from "../../component-info/component-info-model";
import {ModelInformationItem} from "../../../../shared/components/model-information/model-information.component";

export interface ILevelNodeInfo {
  /** Name of the key inside the serviceHierarchy object **/
  name: string;

  /** Type of the object**/
  type: string;

  /** Child name inside the of the serviceHierarchy object (if exist) **/
  childNames?: string[];

  /** model type name **/
  typeName?: string;

  componentInfoType? :ComponentInfoType;

  /** is instance failed **/
  isFailed?: boolean;

  /** number of failed **/
  numberOfFailed?: number;

  /***********************************************************
   * return node position
   * @param instance - instance
   ************************************************************/
  getNodePosition(instance, deepNodeName? : string) : number;

  /***********************************************************
   * return if user should provide instance name or not
   * @param currentModel - The model of current object
   * @param parentModel - The parent model of current object.
   ************************************************************/
  isEcompGeneratedNaming(currentModel, parentModel?): boolean;


  /***********************
   * update node position
   ***********************/
  updatePosition(node, that, instanceId, parentStoreKey?) : void;

  /***********************************************************
   * return a NodeModel object instance
   * @param instanceModel - The model of the instance (usually extracted from
   *        serviceHierarchy store)
   ************************************************************/
  getModel(instanceModel: any): any;

  /***********************************************************
   * return dynamic inputs of current model
   * @param currentModel - The model of current object
   ************************************************************/
  updateDynamicInputsDataFromModel(currentModel): any;

  /***********************************************************
   * return tree node instance
   * @param instance - The model of current object
   * @param model - The model of current object
   * @param parentModel
   * @param storeKey - instance storeKey if exist (for duplicate)
   * @param serviceModelId
   ************************************************************/
  createInstanceTreeNode(instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any

  /***********************************************************
   * return if instance has some missing data
   * @param instance - The instance of current object
   * @param dynamicInputs
   * @param isEcompGeneratedNaming - boolean
   ************************************************************/
  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean;

  /************************
   * return object tooltip
   ************************/
  getTooltip(): string;

  /************************
   * return object type
   ************************/
  getType(): string;

  /************************************
   * return child model option object
   ***********************************/
  getNextLevelObject(type?:string): any;

  /***********************************************************
   * open popup
   * @param node - current ITrees node
   * @param serviceModelId - service id
   ************************************************************/
  onClickAdd(node: ITreeNode, serviceModelId: string): void;

  /***********************************************************
   * get number of existing node instance
   * @param node - current ITrees node
   * @param serviceModelId - service id
   ************************************************************/
  getNodeCount(node: ITreeNode, serviceModelId: string): number;

  /***********************************************************
   * should show node icon
   * @param node - current ITrees node
   * @param serviceModelId - service id
   ************************************************************/
  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons;

  /***********************************************************
   * should return list of actions and there methods
   * @param node - current ITrees node
   * @param serviceModelId
   ************************************************************/
  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } };

   /*******************************************************************************************
   * should generate array of node information
   * @param model - node model
   * @param node - current ITrees node
   *****************************************************************************************/
  getInfo?(model, instance): ModelInformationItem[];
}
