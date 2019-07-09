import {NetworkInstance} from "./networkInstance";
import {Level1Model} from "./nodeModel";
import {FirstLevelTreeNode} from "./firstLevelTreeNode";


export class NetworkTreeNode extends FirstLevelTreeNode {
  routeTargetId: string;
  routeTargetRole: string;

  constructor(instance: NetworkInstance, networkModel: Level1Model, networkStoreKey: string) {
    super(instance, networkModel, networkStoreKey);
    this.routeTargetId = instance &&  instance.routeTarget ? instance.routeTarget.globalRouteTarget : null;
    this.routeTargetRole = instance &&  instance.routeTarget ? instance.routeTarget.routeTargetRole : null;
  }
}
