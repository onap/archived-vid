import {Injectable} from "@angular/core";
import {ILevelNodeInfo} from "../models/basic.model.info";
import {ObjectToTreeService} from "../objectToTree.service";
import * as _ from "lodash";

@Injectable()
export class ObjectToModelTreeService {
  constructor(private _objectToTreeService: ObjectToTreeService) {
  }

  /***********************************************************
   * return array of first level node with there child's
   * @param serviceModel - The serviceHierarchy object
   ************************************************************/
  convertServiceHierarchyModelToTreeNodes(serviceModel): any[] {
    let _this = this;
    const firstLevelOptions: ILevelNodeInfo[] = _this._objectToTreeService.getFirstLevelOptions();
    let nodes = [];
    for (let option of firstLevelOptions) {
      _.forOwn(serviceModel[option.name], function (item, key) {
        nodes.push(_this.addFirstLevelModel(serviceModel.service.uuid, key, item, item.type, serviceModel, option));
      });
    }
    console.log('nodes', nodes);
    return nodes;
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
  private addFirstLevelModel(serviceId: string, name, currentModel, type, parentModel, levelNodeInfo: ILevelNodeInfo) {
    let node = ObjectToModelTreeService.convertItemToTreeNode(serviceId, name, currentModel, type, null, levelNodeInfo);
    node.children = this.addNextLevelNodes(serviceId, currentModel, parentModel, levelNodeInfo, node);
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
  addNextLevelNodes(serviceId: string, currentModel, parentModel, levelNodeInfo: ILevelNodeInfo, parentNode): any[] {
    if (!_.isNil(levelNodeInfo.childName)) {
      if (!_.isNil(currentModel[levelNodeInfo.childName])) {
        let nextLevelNodeInfo = levelNodeInfo.getNextLevelObject.apply(this);
        parentNode.children = Object.keys(currentModel[levelNodeInfo.childName]).map((key) =>
          ObjectToModelTreeService.convertItemToTreeNode(serviceId, key, currentModel[levelNodeInfo.childName][key], levelNodeInfo.childName, currentModel, nextLevelNodeInfo));

      }
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
  static convertItemToTreeNode(serviceId: string, name: string, currentModel: any, valueType: string, parentModel: string, levelNodeInfo: ILevelNodeInfo) {
    let node = {
      id: currentModel.customizationUuid || currentModel.uuid,
      modelCustomizationId : currentModel.customizationUuid,
      modelVersionId:  currentModel.uuid,
      modelUniqueId : currentModel.customizationUuid || currentModel.uuid,
      name: name,
      tooltip: levelNodeInfo.getTooltip(),
      type: levelNodeInfo.getType(),
      count: currentModel.count || 0,
      max: currentModel.max || 1,
      children: [],
      disabled: false,
      dynamicInputs: levelNodeInfo.updateDynamicInputsDataFromModel(currentModel),
      isEcompGeneratedNaming: levelNodeInfo.isEcompGeneratedNaming(currentModel, parentModel)
    };

    node['onAddClick'] = (node, serviceId) => levelNodeInfo.onClickAdd(node, serviceId);
    node['getNodeCount'] = (node, serviceId) => levelNodeInfo.getNodeCount(node, serviceId);
    node['getMenuAction'] = (node, serviceId) => levelNodeInfo.getMenuAction(node, serviceId);
    node['showNodeIcons'] = (node, serviceId) => levelNodeInfo.showNodeIcons(node, serviceId);
    node['typeName'] = levelNodeInfo['typeName'];
    return node;
  }
}
