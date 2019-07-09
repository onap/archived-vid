import {ChildNodeInstance} from "./nodeInstance";
import {DefaultDataGeneratorService} from "../services/defaultDataServiceGenerator/default.data.generator.service";

export class Level1Instance extends ChildNodeInstance{
  uuid : string;
  isEcompGeneratedNaming: boolean;
  productFamilyId: string;
  lcpCloudRegionId: string;
  legacyRegion: string;
  tenantId: string;
  platformName: string;
  lineOfBusiness: string;
  rollbackOnFailure: string;
  originalName: string;
  region: string;
  routeTargets: any[];
  customerId: string;

  constructor() {
    super();
    this.rollbackOnFailure = 'true';
    this.originalName = null;
    this.isMissingData= false;
    this.trackById = DefaultDataGeneratorService.createRandomTrackById();
    this.inMaint= false;
  }
}
