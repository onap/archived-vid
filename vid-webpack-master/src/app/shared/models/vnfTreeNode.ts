import {VNFModel} from "./vnfModel";
import {VnfInstance} from "./vnfInstance";
import {VfModuleTreeNode} from "./vfModuleTreeNode";

export class VnfTreeNode {
  modelId: string;
  name: string;
  modelName: string;
  type: string;
  children: VfModuleTreeNode[];

  constructor(instance: VnfInstance, vnfModel: VNFModel){
    this.name = instance.instanceName || vnfModel['properties'].ecomp_generated_naming == 'false' ? vnfModel.modelCustomizationName : '<Automatically Assigned>';
    this.modelId = vnfModel.uuid;
    this.modelName = vnfModel.modelCustomizationName;
    this.type = vnfModel.type;
  }
}
