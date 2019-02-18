import {Injectable} from "@angular/core";
import {ILevelNodeInfo} from "../models/basic.model.info";
import {ObjectToTreeService} from "../objectToTree.service";
import {DefaultDataGeneratorService} from "../../../../shared/services/defaultDataServiceGenerator/default.data.generator.service";
import * as _ from "lodash";
import {ServiceInstanceActions} from "../../../../shared/models/serviceInstanceActions";
import {ErrorMsgService} from "../../../../shared/components/error-msg/error-msg.service";
import {FeatureFlagsService, Features} from "../../../../shared/services/featureFlag/feature-flags.service";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../shared/store/reducers";

@Injectable()
export class ObjectToInstanceTreeService {
  constructor(private _objectToTreeService: ObjectToTreeService, private _errorMsgService: ErrorMsgService, private store: NgRedux<AppState>) {
    this.numberOfFailed = 0;
    this.numberOfElements = 0;

  }

  /** store number of failed ********  ONLY IN RETRY MODE  ******** **/
  numberOfFailed: number = 0;

  /** store number of existing elements **/
  numberOfElements: number = 0;

  /*****************************************************************
   * return array of first level node with there child's
   * @param serviceInstance - The service instance object from store
   * @param serviceHierarchy - The service Hierarchy store
   ****************************************************************/
  convertServiceInstanceToTreeData(serviceInstance, serviceHierarchy): any[] {
    this._errorMsgService.triggerClearError.next();
    let nodes = [];
    this.numberOfFailed = 0;
    this.numberOfElements = 0;
    let _this = this;
    const firstLevelOptions: ILevelNodeInfo[] = _this._objectToTreeService.getFirstLevelOptions();
    for (let option of firstLevelOptions) {
      _.forOwn(serviceInstance[option.name], function (instance, modelName) {
        nodes.push(_this.getNodeInstance(modelName, null, instance, serviceHierarchy, option));
      });
    }
    return this.sortElementsByPosition(nodes);
  }

  /*****************************************************************
   * should increase number of failed
   * @param node - the current node
   ****************************************************************/
  increaseNumberOfFailed(node) {
    if (node.isFailed) {
      this.numberOfFailed++;
      node['errors'] = !_.isNil(node['errors']) ? node['errors'] : {};
      node['errors']["isFailed"] = true;
      this._errorMsgService.triggerShowError.next(this._errorMsgService.getRetryErrorObject(this.numberOfFailed));
    }
  }

  /*****************************************************************
   * should increase number of existing elements
   * @param node - the current node
   ****************************************************************/
  increaseNumberOfExcitingElements() {
    this.numberOfElements++;
  }

  /*****************************************************************
   * return array of first level node with there child's
   * @param modelName
   * @param parentModel
   * @param instance
   * @param serviceHierarchy - The service Hierarchy store
   * @param option
   * @param parentType
   ****************************************************************/
  getNodeInstance(modelName: string, parentModel: any, instance: any, serviceHierarchy, option: ILevelNodeInfo, parentType ?: string) {
    const model = option.getModel(modelName, instance, serviceHierarchy);

    let optionalNodes = option.createInstanceTreeNode(instance, model, parentModel, modelName);
    this.increaseNumberOfFailed(optionalNodes);
    this.increaseNumberOfExcitingElements();
    let nodes: any[] = _.isArray(optionalNodes) ? optionalNodes : [optionalNodes];
    for (let node of nodes) {
      node = this.addingExtraDataToNode(node, modelName, parentModel, instance, serviceHierarchy, option, parentType);
      let children = this.addNextInstanceTreeNode(instance, model, option, node, serviceHierarchy);
      if (!_.isNil(children) && children.length > 0) {
        node.children = this.sortElementsByPosition(children);
      }
      this.updateScalingPolicy(node);
    }
    return nodes.length === 1 ? nodes[0] : nodes;
  }

  addingExtraDataToNode(node, modelName: string, parentModel: any, instance: any, serviceHierarchy, option: ILevelNodeInfo, parentType ?: string) {
    node.trackById = _.isNil(node.trackById) ? DefaultDataGeneratorService.createRandomTrackById() : node['trackById'];
    node.parentType = !_.isNil(parentType) ? parentType : "";
    node.updatePoistionFunction = option.updatePosition;
    node.position = option.getNodePosition(instance, node.dynamicModelName);
    node.onSelectedNode = option.onSelectedNode;
    return node;
  }

  sortElementsByPosition(nodes: any[]): any[] {
    if (!FeatureFlagsService.getFlagState(Features.DRAG_AND_DROP_OPERATION, this.store)) return nodes;
    return nodes.sort((nodeA, nodeB) => {
      return nodeA.position - nodeB.position;
    });
  }

  /*****************************************************************
   * return next level node with there child's
   * @param parentInstance
   * @param parentModel
   * @param levelNodeInfo
   * @param parentNode
   * @param serviceHierarchy - The service Hierarchy store
   ****************************************************************/
  addNextInstanceTreeNode(parentInstance, parentModel, levelNodeInfo: ILevelNodeInfo, parentNode, serviceHierarchy): any[] {
    if (!_.isNil(levelNodeInfo.childName)) {
      parentNode.children = [];
      if (!_.isNil(parentInstance[levelNodeInfo.childName])) {
        let parentType = levelNodeInfo.type;
        let nextLevelNodeInfo = levelNodeInfo.getNextLevelObject.apply(this);
        Object.keys(parentInstance[levelNodeInfo.childName]).map((modelName) => {
          let nextLevelInstance = parentInstance[levelNodeInfo.childName][modelName];
          let nodes: any[] | any = this.getNodeInstance(modelName, parentModel, nextLevelInstance, serviceHierarchy, nextLevelNodeInfo, parentType);
          if (_.isArray(nodes)) {
            parentNode.children = parentNode.children.concat(nodes);
          } else {
            parentNode.children.push(nodes);
          }
        });
        return this.sortElementsByPosition(parentNode.children);
      }
    }
    return parentNode.children;
  }


  /************************************************************************************
   * update instance scaling policy according to instance limit and existing children
   * @param node
   *********************************************************************************/
  updateScalingPolicy(node): void {
    node['errors'] = !_.isNil(node['errors']) ? node['errors'] : {};
    if (!_.isNil(node['limitMembers']) && !_.isNil(node.children)) {
      let effectiveChildren = (node.children).filter(child => [
        ServiceInstanceActions.Create,
        ServiceInstanceActions.None,
        ServiceInstanceActions.Update
      ].includes(child.action));


      if (effectiveChildren.length > node.limitMembers) {
        node['errors']["scalingError"] = true;
        this._errorMsgService.triggerShowError.next(this._errorMsgService.getScalingErrorObject());
      } else {
        delete node['errors']["scalingError"];
      }

    }
  }
}
