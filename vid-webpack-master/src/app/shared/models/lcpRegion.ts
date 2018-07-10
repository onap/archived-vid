export class LcpRegion {
  id: string;
  name: string;
  isPermitted: boolean;

  constructor(serviceJson){
    this.id = serviceJson["cloudRegionID"];
    this.name = serviceJson["cloudRegionID"];
    this.isPermitted = serviceJson["is-permitted"];
  }
}
