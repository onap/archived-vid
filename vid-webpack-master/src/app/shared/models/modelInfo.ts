
export class ModelInfo {
  modelInvariantId?: string;
  modelVersionId?: string;
  modelName: string;
  modelVersion: string;
  modelCustomizationId?: string;
  modelCustomizationName?: string;
  uuid? : string;
  modelUniqueId?: String;



  constructor(instanceModel) {
    this.modelInvariantId = instanceModel.invariantUuid;
    this.modelVersionId = instanceModel.uuid;
    this.modelName = instanceModel.name;
    this.modelVersion = instanceModel.version;
    this.modelCustomizationId = instanceModel.customizationUuid;
    this.modelCustomizationName = instanceModel.modelCustomizationName;
    this.uuid = instanceModel.uuid;
    this.modelUniqueId = this.modelCustomizationId||this.uuid;
  }
}

