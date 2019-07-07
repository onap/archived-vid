import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {ModelInformationItem} from "../../../shared/components/model-information/model-information.component";
import {ComponentInfoType} from "../../service-planning/component-info/component-info-model";

export interface IModelTreeNodeModel {
  id?: string;
  modelCustomizationId? : string;
  modelVersionId?:  string;
  modelUniqueId? : string;
  name?: string;
  tooltip?: string;
  type?: string;
  count?: number;
  max?: number;
  children?: any[];
  disabled?: boolean;
  dynamicInputs?: any;
  isEcompGeneratedNaming?: boolean;
  typeName? : string;
  componentInfoType?: ComponentInfoType;


  onAddClick?(node, serviceId);
  getNodeCount?(node, serviceId);
  getMenuAction?(node, serviceId);
  showNodeIcons?(node, serviceId);
  getModel?(modelId: string, instance: any, serviceHierarchy)
  getInfo?(model, instance): ModelInformationItem[];

}
