import {VfModuleMap} from "./vfModulesMap";

export class VnfInstance {
  instanceName: string;
  isUserProvidedNaming: boolean;
  productFamilyId: string;
  lcpCloudRegionId: string;
  legacyRegion: string;
  tenantId: string;
  platformName: string;
  lineOfBusiness: string;
  rollbackOnFailure: string;
  vfModules: { [vf_module_model_name: string] : VfModuleMap; };

  constructor() {
    this.rollbackOnFailure = 'true';
    this.vfModules = {};
  }
}
