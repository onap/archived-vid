import {SelectOption, SelectOptionInterface} from "../../../shared/models/selectOption";

export class ServicePopupDataModel {
  subscribers: SelectOptionInterface[];
  serviceTypes: SelectOptionInterface[];
  aicZones: SelectOptionInterface[];
  lcpRegions: SelectOptionInterface[];
  productFamilies: SelectOptionInterface[];
  lcpRegionsTenantsMap: object;
  tenants: SelectOptionInterface[];
  projects: SelectOptionInterface[];
  owningEntities: SelectOptionInterface[];
  globalCustomerId: string;
  rollbackOnFailure: SelectOptionInterface[];


  constructor(){
    this.subscribers = null;
    this.serviceTypes = null;
    this.aicZones = null;
    this.lcpRegions = null;
    this.lcpRegionsTenantsMap = {};
    this.tenants = null;
    this.productFamilies = null;
    this.projects = null;
    this.owningEntities = null;
    this.rollbackOnFailure = [
      new SelectOption({id: 'true', name: 'Rollback'}),
      new SelectOption({id: 'false', name: 'Don\'t Rollback'})
    ];
  }
}
