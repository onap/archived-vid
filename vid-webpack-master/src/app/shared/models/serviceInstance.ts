import {VnfInstance} from "./vnfInstance";

export class ServiceInstance {
  instanceName: string;
  isUserProvidedNaming: boolean;
  globalSubscriberId: string;
  productFamilyId: string;
  subscriptionServiceType: string;
  lcpCloudRegionId: string;
  tenantId: string;
  tenantName: string;
  aicZoneId: string;
  aicZoneName: string;
  projectName: string;
  owningEntityId: string;
  owningEntityName: string;
  pause: boolean;
  bulkSize: number;
  vnfs: { [vnf_module_model_name: string] : VnfInstance; };
  instanceParams: { [key: string] : string; };
  rollbackOnFailure : boolean;
  subscriberName : string;

  constructor() {
    this.vnfs = {};
    this.instanceParams = {};
  }
}
