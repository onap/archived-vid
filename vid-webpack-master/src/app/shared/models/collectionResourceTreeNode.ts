import {TreeNodeModel} from "./treeNodeModel";
import {Level1Model} from "./nodeModel";
import {CollectionResourceInstance} from "./collectionResourceInstance";

export class CollectionResourceTreeNode extends TreeNodeModel {
  collectionResourceStoreKey : string;
  typeName: string;
  menuActions: { [p: string]: { method: Function; visible: Function; enable: Function } };
  isFailed: boolean;
  statusMessage?: string;

  constructor(instance: CollectionResourceInstance, collectionResourceModel: Level1Model, collectionResourceStoreKey : string){
    super(instance, collectionResourceModel);
    this.name = instance.instanceName? instance.instanceName: !collectionResourceModel.isEcompGeneratedNaming ? collectionResourceModel.modelCustomizationName : '&lt;Automatically Assigned&gt;';
    this.modelName = collectionResourceModel.modelCustomizationName;
    this.type = collectionResourceModel.type;
    this.isEcompGeneratedNaming = collectionResourceModel.isEcompGeneratedNaming;
    this.collectionResourceStoreKey = collectionResourceStoreKey;
  }
}

