export interface NodeModelResponseInterface {
  name: string;
  version: string;
  description: string;
  category: string;
  uuid: string;
  invariantUuid: string;
}

export class NodeModel {
  name: string;
  version: string;
  description: string;
  category: string;
  uuid: string;
  invariantUuid: string;

  constructor(serviceJson?: NodeModelResponseInterface) {
    if (serviceJson) {
      this.name = serviceJson.name;
      this.version = serviceJson.version;
      this.description = serviceJson.description;
      this.category = serviceJson.category;
      this.uuid = serviceJson.uuid;
      this.invariantUuid = serviceJson.invariantUuid;
    }
  }

}
