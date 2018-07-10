import {LcpRegion} from "./lcpRegion";
import {Tenant} from "./tenant";

export class LcpRegionsAndTenants {
  lcpRegionList: LcpRegion[];
  lcpRegionsTenantsMap: { [lcpRegion: string] : Tenant[]; };

  constructor(lcpRegionList: LcpRegion[] = [], lcpRegionsTenantsMap: any = {}) {
    this.lcpRegionList = lcpRegionList;
    this.lcpRegionsTenantsMap = lcpRegionsTenantsMap;
  }
}
