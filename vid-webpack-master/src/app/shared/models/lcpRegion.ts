export class LcpRegion {
  id: string;
  name: string;
  isPermitted: boolean;
  cloudOwner: string;

  constructor(id: string, name: string, isPermitted: boolean, cloudOwner: string) {
    this.id = id;
    this.name = name;
    this.isPermitted = isPermitted;
    this.cloudOwner = cloudOwner;
  }
}
