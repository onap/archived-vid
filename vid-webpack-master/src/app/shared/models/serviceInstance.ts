import {VnfInstance} from "./vnfInstance";
import {NetworkInstance} from "./networkInstance";
import {NodeInstance} from "./nodeInstance";
import {VnfGroupInstance} from "./vnfGroupInstance";
import {VnfMember} from "./VnfMember";
import {VrfInstance} from "./vrfInstance";
import {VidNotions} from "./vidNotions";
import {PnfInstance} from "./pnfInstance";

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
  latestAvailableVersion: Number;
  pause: boolean;
  bulkSize: number;
  pnfs: { [pnf_module_model_name: string]: PnfInstance; };
  vnfs: { [vnf_module_model_name: string]: VnfInstance; };
  vrfs: { [vrf_model_name: string]: VrfInstance; };
  vnfGroups : {[vnf_module_model_name: string]: VnfGroupInstance; };
  networks: { [vnf_module_model_name: string]: NetworkInstance; };
  isDirty : boolean;
  isUpgraded : boolean;
  instanceParams: {[key: string]: string}[];
  rollbackOnFailure: boolean;
  subscriberName: string;
  validationCounter: number;
  existingNames:  {[key: string] : any};
  modelInavariantId?: string;
  existingPNFCounterMap : { [pnf_module_model_name: string]: number; };
  existingVNFCounterMap : { [vnf_module_model_name: string]: number; };
  existingVRFCounterMap : { [vrf_module_model_name: string]: number; };
  existingVnfGroupCounterMap : { [vnf_group_module_model_name: string]: number; };
  existingNetworksCounterMap : { [network_module_model_name: string]: number; };
  optionalGroupMembersMap?: { [path: string]: VnfMember[]; };
  statusMessage: string;
  vidNotions?: VidNotions;
  upgradedVFMSonsCounter: number;

  constructor() {
    super();
    this.isDirty = false;
    this.vnfs = {};
    this.pnfs = {};
    this.vrfs = {};
    this.instanceParams = [];
    this.validationCounter = 0;
    this.existingNames = {};
    this.existingPNFCounterMap = {};
    this.existingVNFCounterMap = {};
    this.existingVRFCounterMap = {};
    this.existingVnfGroupCounterMap = {};
    this.existingNetworksCounterMap = {};
    this.optionalGroupMembersMap = {};
    this.networks = {};
    this.vnfGroups = {};
    this.bulkSize = 1;
    this.isUpgraded = false;
    this.upgradedVFMSonsCounter = 0;
  }
}
