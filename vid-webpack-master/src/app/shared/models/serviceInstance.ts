import {VnfInstance} from "./vnfInstance";
import {NetworkInstance} from "./networkInstance";
import {NodeInstance} from "./nodeInstance";
import {VnfGroupInstance} from "./vnfGroupInstance";
import {VnfMember} from "./VnfMember";

export class ServiceInstance extends NodeInstance{
  isEcompGeneratedNaming: boolean;
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
  vnfs: { [vnf_module_model_name: string]: VnfInstance; };
  vnfGroups : {[vnf_module_model_name: string]: VnfGroupInstance; };
  networks: { [vnf_module_model_name: string]: NetworkInstance; };
  isDirty : boolean;
  instanceParams: {[key: string]: string}[];
  rollbackOnFailure: boolean;
  subscriberName: string;
  validationCounter: number;
  existingNames:  {[key: string] : any};
  modelInavariantId?: string;
  existingVNFCounterMap : { [vnf_module_model_name: string]: number; };
  existingVnfGroupCounterMap : { [vnf_group_module_model_name: string]: number; };
  existingNetworksCounterMap : { [network_module_model_name: string]: number; };
  optionalGroupMembersMap?: { [path: string]: VnfMember[]; };
  isFailed: boolean;
  statusMessage: string;

  constructor() {
    super();
    this.isDirty = false;
    this.vnfs = {};
    this.instanceParams = [];
    this.validationCounter = 0;
    this.existingNames = {};
    this.existingVNFCounterMap = {};
    this.existingVnfGroupCounterMap = {};
    this.existingNetworksCounterMap = {};
    this.optionalGroupMembersMap = {};
    this.networks = {};
    this.vnfGroups = {};
    this.bulkSize = 1;
  }

}
