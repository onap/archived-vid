import {TreeNodeModel} from "./treeNodeModel";
import {Level1Model} from "./nodeModel";
import {Level1Instance} from "./level1Instance";


export class VpnTreeNode extends TreeNodeModel {
  storeKey: string;
  typeName: string;
  menuActions: { [p: string]: { method: Function; visible: Function; enable: Function } };
  isFailed: boolean;
  statusMessage?: string;
  region: string;
  routeTargetId: string;
  routeTargetRole: string;
  customerVPNId: string;

  constructor(instance: Level1Instance, vpnModel: Level1Model, storeKey: string) {
    super(instance, vpnModel);
    this.name = instance.instanceName ? instance.instanceName : !vpnModel.isEcompGeneratedNaming ? vpnModel.modelCustomizationName : '&lt;Automatically Assigned&gt;';
    this.modelName = vpnModel.modelCustomizationName;
    this.type = vpnModel.type;
    this.isEcompGeneratedNaming = vpnModel.isEcompGeneratedNaming;
    this.storeKey = storeKey;
    this.region = instance.region;
    this.routeTargetId = instance.routeTargets && instance.routeTargets.length ? instance.routeTargets[0].globalRouteTarget : null;
    this.routeTargetRole = instance.routeTargets && instance.routeTargets.length ? instance.routeTargets[0].routeTargetRole : null;
    this.customerVPNId = instance.customerId;
  }
}
