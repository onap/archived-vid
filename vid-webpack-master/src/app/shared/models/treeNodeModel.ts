import {ChildNodeInstance} from "./nodeInstance";
import {NodeModel} from "./nodeModel";
import {ServiceNodeTypes} from "./ServiceNodeTypes";
import * as _ from 'lodash';
import {ServiceInstanceActions} from "./serviceInstanceActions";

export enum TreeLevel {
  Level_0 , Level_1, Level_2

}
interface TreeNodeInstanceInterface {
  treeLevel: TreeLevel;
  getModel(): NodeModel;
  convertToTreeNode(node: any): any;
  type: ServiceNodeTypes;
}
export class TreeNodeModel {
  type: String;
  modelId: string;
  modelInvariantId?: string;
  modelCustomizationId?: string;
  modelUniqueId?: string;
  id: string;
  name: string;
  modelName: string;
  missingData: boolean;
  isEcompGeneratedNaming: boolean;
  orchStatus?:string;
  provStatus?:string;
  inMaint?:boolean;
  action : string ;
  instanceId?: string;
  instanceType?: string;
  instanceName?: string;

  constructor(instance: ChildNodeInstance, nodeModel: NodeModel){
    if (instance.modelInfo) {
      this.modelInvariantId = instance.modelInfo.modelInvariantId;
      this.modelCustomizationId = instance.modelInfo.modelCustomizationId;
      this.modelId = instance.modelInfo.modelVersionId;
      this.modelUniqueId = this.modelCustomizationId || this.modelId;
    }
    this.missingData = false;
    this.id = instance.trackById;
    this.action = !_.isNil(instance.action) ? instance.action : ServiceInstanceActions.Create;

    if(!_.isNil(instance.orchStatus)){
      this.orchStatus= instance.orchStatus;
    }

    if(!_.isNil(instance.provStatus)){
      this.provStatus= instance.provStatus;
    }

    if(!_.isNil(instance.inMaint)){
      this.inMaint= instance.inMaint;
    }

    if(!_.isNil(instance.instanceId)){
      this.instanceId= instance.instanceId;
    }
    if(!_.isNil(instance.instanceType)){
      this.instanceType= instance.instanceType;
    }
    if(!_.isNil(instance.instanceName)){
      this.instanceName= instance.instanceName;
    }




  }
}
