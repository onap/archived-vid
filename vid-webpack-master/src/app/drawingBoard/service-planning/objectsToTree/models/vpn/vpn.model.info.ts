import {ILevelNodeInfo} from "../basic.model.info";
import {ComponentInfoType} from "../../../component-info/component-info-model";
import {ITreeNode} from "angular-tree-component/dist/defs/api";
import {AvailableNodeIcons} from "../../../available-models-tree/available-models-tree.service";
import {ModelInformationItem} from "../../../../../shared/components/model-information/model-information.component";
import * as _ from "lodash";
import {NgRedux} from "@angular-redux/store";
import {AppState} from "../../../../../shared/store/reducers";
import {SharedTreeService} from "../../shared.tree.service";
import {NetworkInstance} from "../../../../../shared/models/networkInstance";
import {Level1Model} from "../../../../../shared/models/nodeModel";
import {VpnTreeNode} from "../../../../../shared/models/vpnTreeNode";

export class VpnModelInfo implements ILevelNodeInfo {
  constructor(private _store: NgRedux<AppState>,
              private _sharedTreeService: SharedTreeService) {
  }

  name: string = 'vpns';
  type: string = 'VPN';
  typeName: string = 'VPN';

  childNames: string[] = [];
  componentInfoType = ComponentInfoType.VPN;

  isEcompGeneratedNaming(currentModel): boolean {
    return false;
  }

  updateDynamicInputsDataFromModel = (currentModel): any => [];

  getModel = (instanceModel: any): any => {
    return new Level1Model();
  };


  createInstanceTreeNode = (instance: any, model: any, parentModel: any, storeKey: string, serviceModelId: string): any => {
    let node = new VpnTreeNode(instance, model, storeKey);
    node.missingData = this.hasMissingData(instance, node, model.isEcompGeneratedNaming);
    node = this._sharedTreeService.addingStatusProperty(node);
    node.typeName = this.typeName;
    node.menuActions = this.getMenuAction(<any>node, serviceModelId);
    node.isFailed = _.isNil(instance.isFailed) ? false : instance.isFailed;
    node.statusMessage = !_.isNil(instance.statusMessage) ? instance.statusMessage : "";
    node = this._sharedTreeService.addingStatusProperty(node);
    return node;
  };


  getNextLevelObject = (): any => {
    return null;
  };

  getTooltip = (): string => 'VPN';

  getType = (): string => 'VPN';

  hasMissingData(instance, dynamicInputs: any, isEcompGeneratedNaming: boolean): boolean {
    return false;
  }

  onClickAdd(node, serviceModelId: string): void {

  }

  getNodeCount(node: ITreeNode, serviceModelId: string): number {
    return 0;
  }

  showNodeIcons(node: ITreeNode, serviceModelId: string): AvailableNodeIcons {
    return null;
  }

  getMenuAction(node: ITreeNode, serviceModelId: string): { [methodName: string]: { method: Function, visible: Function, enable: Function } } {
    return {};
  }

  updatePosition(that, node, instanceId): void {
  }

  getNodePosition(instance): number {
    return 0;
  }

  getInfo(model, instance): ModelInformationItem[] {
    const modelInformation = [];
    const instanceInfo = !_.isEmpty(instance) ? [
      ModelInformationItem.createInstance("Region", instance.region),
      ModelInformationItem.createInstance("Route target id", !_.isNull(instance.routeTargetId) ? instance.routeTargetId : null),
      ModelInformationItem.createInstance("Route target role", !_.isNull(instance.routeTargetRole) ? instance.routeTargetRole : null),
      ModelInformationItem.createInstance("Customet VPN ID", !_.isNull(instance.customerVPNId) ? instance.customerVPNId : null)] : [];
    const result = [modelInformation, instanceInfo];
    return _.uniq(_.flatten(result));
  }
}

