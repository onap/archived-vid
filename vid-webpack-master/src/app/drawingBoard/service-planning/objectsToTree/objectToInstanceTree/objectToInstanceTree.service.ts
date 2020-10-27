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
import {SharedTreeService} from "../shared.tree.service";

@Injectable()
export class ObjectToInstanceTreeService {
  constructor(private _objectToTreeService: ObjectToTreeService, private _errorMsgService: ErrorMsgService,
              private store: NgRedux<AppState>, private _sharedTreeService: SharedTreeService) {
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
    const serviceModelId:string = serviceInstance.modelInfo.modelVersionId;
    const firstLevelOptions: ILevelNodeInfo[] = _this._objectToTreeService.getFirstLevelOptions(serviceInstance.isALaCarte);
    for (let option of firstLevelOptions) {
      _.forOwn(serviceInstance[option.name], function (instance, modelName) {
        nodes.push(_this.getNodeInstance(modelName, null, instance, serviceHierarchy, option, serviceModelId));
      });
    }
    return this.sortElementsByPosition(nodes);
  }

  /*****************************************************************
   * should increase number of failed
   * @param node - the current node
   ****************************************************************/
  increaseNumberOfFailed(node) {
    if (node && node.isFailed) {
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
   * @param serviceModelId
   * @param parentType
   ****************************************************************/
  getNodeInstance(modelName: string, parentModel: any, instance: any, serviceHierarchy, option: ILevelNodeInfo, serviceModelId: string, parentType ?: string) {
    const instanceModel = this._sharedTreeService.modelByIdentifiers(
      serviceHierarchy, option.name,
      this._sharedTreeService.modelUniqueNameOrId(instance), modelName
    );
    const model = option.getModel(instanceModel);

    let optionalNodes = option.createInstanceTreeNode(instance, model, parentModel, modelName, serviceModelId);
    this.increaseNumberOfFailed(optionalNodes);
    this.increaseNumberOfExcitingElements();
    let nodes: any[] = _.isArray(optionalNodes) ? optionalNodes : [optionalNodes];
    for (let node of nodes) {
      node = this.addingExtraDataToNode(node, modelName, parentModel, instance, serviceHierarchy, option, parentType);
      let children = this.addNextInstanceTreeNode(instance, model, option, node, serviceHierarchy, serviceModelId);
      if (!_.isNil(children) && children.length > 0) {
        node.children = this.sortElementsByPosition(children);
      }
      this.updateScalingPolicy(node);
    }
    return nodes.length === 1 ? nodes[0] : nodes;
  }

  addingExtraDataToNode(node, modelName: string, parentModel: any, instance: any, serviceHierarchy, option: ILevelNodeInfo, parentType ?: string) {
    if(!_.isNil(node)){
      node.trackById = _.isNil(node.trackById) ? DefaultDataGeneratorService.createRandomTrackById() : node['trackById'];
      node.parentType = !_.isNil(parentType) ? parentType : "";
      node.updatePoistionFunction = option.updatePosition;
      node.position = option.getNodePosition(instance, node.dynamicModelName);
      node.modelTypeName = option.name;
      node.getModel = option.getModel.bind(option);
      node.getInfo = !_.isNil(option.getInfo) ? option.getInfo.bind(option) : ()=>{};
      node.componentInfoType = option.componentInfoType;
    }

    return node;
  }

  sortElementsByPosition(nodes: any[]): any[] {
    if (!FeatureFlagsService.getFlagState(Features.FLAG_1911_INSTANTIATION_ORDER_IN_ASYNC_ALACARTE, this.store)) return nodes;
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
   * @param serviceModelId
   ****************************************************************/
  addNextInstanceTreeNode(parentInstance, parentModel, levelNodeInfo: ILevelNodeInfo, parentNode, serviceHierarchy, serviceModelId: string): any[] {
    if (!_.isNil(levelNodeInfo.childNames)&& levelNodeInfo.childNames.length > 0) {
      const that = this;
      parentNode.children = [];
      levelNodeInfo.childNames.forEach(function (childName)  {
        if (!_.isNil(parentInstance[childName])) {
          let parentType = levelNodeInfo.type;
          let nextLevelNodeInfo = levelNodeInfo.getNextLevelObject.apply(that, [childName]);
          Object.keys(parentInstance[childName]).map((modelName) => {
            let nextLevelInstance = parentInstance[childName][modelName];
            let nodes: any[] | any = that.getNodeInstance(modelName, parentModel, nextLevelInstance, serviceHierarchy, nextLevelNodeInfo, serviceModelId, parentType);
            if (_.isArray(nodes)) {
              parentNode.children = parentNode.children.concat(nodes);
            } else {
              parentNode.children.push(nodes);
            }
          });
        }
      });
      return this.sortElementsByPosition(parentNode.children);
    }
    return !_.isNil(parentNode) ? parentNode.children : null;
  }


  /************************************************************************************
   * update instance scaling policy according to instance limit and existing children
   * @param node
   *********************************************************************************/
  updateScalingPolicy(node): void {
    if(_.isNil(node)) return node;
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
