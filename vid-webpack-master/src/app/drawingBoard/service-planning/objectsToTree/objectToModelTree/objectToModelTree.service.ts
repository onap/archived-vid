import {Injectable} from "@angular/core";
import {ILevelNodeInfo} from "../models/basic.model.info";
import {ObjectToTreeService} from "../objectToTree.service";
import * as _ from "lodash";
import {IModelTreeNodeModel} from "../../../objectsToTree/objectToModelTree/modelTreeNode.model";
import {FeatureFlagsService} from "../../../../shared/services/featureFlag/feature-flags.service";

@Injectable()
export class ObjectToModelTreeService {
  numberOfPlusButton: number;

  constructor(private _objectToTreeService: ObjectToTreeService, private _featureFlagsService: FeatureFlagsService) {
  }

  /***********************************************************
   * return array of first level node with there child's
   * @param serviceModel - The serviceHierarchy object
   ************************************************************/
  convertServiceHierarchyModelToTreeNodes(serviceModel): any[] {
    let _this = this;
    let isALaCarte = serviceModel.service.vidNotions.instantiationType === "ALaCarte";
    const firstLevelOptions: ILevelNodeInfo[] = _this._objectToTreeService.getFirstLevelOptions(isALaCarte);
    let nodes = [];
    let flags = this._featureFlagsService.getAllFlags();
    for (let option of firstLevelOptions) {
      _.forOwn(serviceModel[option.name], function (item, key) {
        nodes.push(_this.addFirstLevelModel(serviceModel.service.uuid, key, item, item.type, serviceModel, option, flags));
      });
    }

    this.calculateNumberOfNodesWithPlusIcon(serviceModel, nodes);
    return nodes;
  }

  calculateNumberOfNodesWithPlusIcon(serviceModel, nodes): void {
    this.numberOfPlusButton = nodes.reduce((sum, node) => {
      let showNodeIconResult = node.showNodeIcons({data: node}, serviceModel.service.uuid);
      return (!_.isNil(showNodeIconResult) && showNodeIconResult.addIcon && !showNodeIconResult.vIcon) ? sum + 1 : sum;
    }, 0);
  }


  /***********************************************************
   * return node with all his child's
   * @param name - Model name
   * @param serviceId - service id
   * @param currentModel - current Model object
   * @param type - current Model type
   * @param parentModel - current parent Model object
   * @param levelNodeInfo - current level node information
   ************************************************************/
  private addFirstLevelModel(serviceId: string, name, currentModel, type, parentModel, levelNodeInfo: ILevelNodeInfo, flags?: { [key: string]: boolean }) {
    let node = ObjectToModelTreeService.convertItemToTreeNode(serviceId, name, currentModel, type, null, levelNodeInfo, flags);
    node.children = this.addNextLevelNodes(serviceId, currentModel, parentModel, levelNodeInfo, node, flags);
    return node;
  }

  /***********************************************************
   * return all the child's of node (is child exist)
   *        else return empty array.
   * @param currentModel - current Model object
   * @param parentModel - current parent Model object
   * @param levelNodeInfo - current level node information
   * @param parentNode - parent node.
   ************************************************************/
  addNextLevelNodes(serviceId: string, currentModel, parentModel, levelNodeInfo: ILevelNodeInfo, parentNode, flags?: { [key: string]: boolean }): any[] {
    if (!_.isNil(levelNodeInfo.childNames) && levelNodeInfo.childNames.length > 0) {
      levelNodeInfo.childNames.forEach(function (childName) {
        if (!_.isNil(currentModel[childName])) {
          let nextLevelNodeInfo = levelNodeInfo.getNextLevelObject.apply(this, [childName]);
          parentNode.children = Object.keys(currentModel[childName]).map((key) =>
            ObjectToModelTreeService.convertItemToTreeNode(serviceId, key, currentModel[childName][key], childName, currentModel, nextLevelNodeInfo, flags));
        }
      })
    }
    return parentNode.children;
  }


  /***********************************************************
   * return node with his properties
   * @param serviceId
   * @param name - model name
   * @param currentModel - current model from serviceHierarchy
   * @param valueType - model type
   * @param parentModel - current parent model
   * @param levelNodeInfo - current levelNodeInfo object
   ************************************************************/
  static convertItemToTreeNode(serviceId: string, name: string, currentModel: any, valueType: string, parentModel: string, levelNodeInfo: ILevelNodeInfo, flags?: { [key: string]: boolean }) {
    const type: string = levelNodeInfo.getType();
    let node: IModelTreeNodeModel = {
      id: currentModel.customizationUuid || currentModel.uuid,
      modelCustomizationId: currentModel.customizationUuid,
      modelVersionId: currentModel.uuid,
      modelUniqueId: currentModel.customizationUuid || currentModel.uuid,
      name: name,
      tooltip: levelNodeInfo.getTooltip(),
      type,
      count: currentModel.count || 0,
      max: ObjectToModelTreeService.getMax(currentModel, type, flags),
      children: [],
      disabled: false,
      dynamicInputs: levelNodeInfo.updateDynamicInputsDataFromModel(currentModel),
      isEcompGeneratedNaming: levelNodeInfo.isEcompGeneratedNaming(currentModel, parentModel)
    };

    node = this.addExtraFunctionality(node, serviceId, name, currentModel, valueType, parentModel, levelNodeInfo);
    return node;
  }

  static getMax(currentModel, type, flags: { [key: string]: boolean }) {
    if (flags && !!flags['FLAG_2002_UNLIMITED_MAX'] && (type === 'VF' || type === 'Network' || type === 'VFmodule')) {
      return !_.isNil(currentModel.max) ? currentModel.max : null;
    } else {
      return currentModel.max || 1
    }
  }


  static addExtraFunctionality(node, serviceId: string, name: string, currentModel: any, valueType: string, parentModel: string, levelNodeInfo: ILevelNodeInfo) {
    node.onAddClick = (node, serviceId) => levelNodeInfo.onClickAdd(node, serviceId);
    node.getNodeCount = (node, serviceId) => levelNodeInfo.getNodeCount(node, serviceId);
    node.getMenuAction = (node, serviceId) => levelNodeInfo.getMenuAction(node, serviceId);
    node.showNodeIcons = (node, serviceId) => levelNodeInfo.showNodeIcons(node, serviceId);
    node.getModel = levelNodeInfo.getModel.bind(levelNodeInfo);
    node.getInfo = !_.isNil(levelNodeInfo.getInfo) ? levelNodeInfo.getInfo.bind(levelNodeInfo) : () => {};
    node.componentInfoType = levelNodeInfo.componentInfoType;
    node.typeName = levelNodeInfo.typeName;
    node.modelTypeName = levelNodeInfo.name;
    return node;
  }
}
