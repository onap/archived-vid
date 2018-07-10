export class AicZone {
  id: string;
  name: string;

  constructor(serviceJson){
    this.id = serviceJson["zone-id"];
    this.name = serviceJson["zone-name"];
  }
}
