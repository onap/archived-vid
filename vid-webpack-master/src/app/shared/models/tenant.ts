export class Tenant {
  id: string;
  name: string;
  isPermitted: boolean;

  constructor(serviceJson){
    this.id = serviceJson["tenantID"];
    this.name = serviceJson["tenantName"];
    this.isPermitted = serviceJson["is-permitted"];
  }
}
