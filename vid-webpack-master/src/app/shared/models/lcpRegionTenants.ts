export class LcpRegionTenants {
  id: string;
  tenantId: string;
  tenantName: string;
  isPermitted: boolean;

  constructor(serviceJson){
    this.id = serviceJson["cloudRegionID"];
    this.tenantId = serviceJson["tenantID"];
    this.tenantName = serviceJson["tenantName"];
    this.isPermitted = serviceJson["is-permitted"];
  }
}
