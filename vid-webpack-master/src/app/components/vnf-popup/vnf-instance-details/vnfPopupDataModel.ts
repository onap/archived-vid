import {Project} from "../../../shared/models/project";
import {LcpRegion} from "../../../shared/models/lcpRegion";
import {Tenant} from "../../../shared/models/tenant";
import {ProductFamily} from "../../../shared/models/productFamily";
import {SelectOption, SelectOptionInterface} from "../../../shared/models/selectOption";

export class VNFPopupDataModel {
  productFamilies: ProductFamily[];
  lcpRegions: LcpRegion[];
  lcpRegionsTenantsMap: any;
  tenants: Tenant[];
  projects: Project[];
  lineOfBusinesses: SelectOption[];
  platforms: SelectOptionInterface[];
  globalCustomerId: string;


  constructor(){
      this.lcpRegions = [];
      this.lcpRegionsTenantsMap = {};
      this.tenants = [];
      this.productFamilies = [];
      this.lineOfBusinesses = [];
      this.platforms = [];
  }
}
