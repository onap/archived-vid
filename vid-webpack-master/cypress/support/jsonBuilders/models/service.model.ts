/// <reference types="Cypress" />

export class ServiceModel{
  service: {
    uuid: string;
    invariantUuid: string;
    name: string;
    version: string;
    toscaModelURL: string;
    category: string;
    serviceType: string;
    serviceRole: string;
    description: string;
    serviceEcompNaming : string;
    instantiationType: string;
    inputs: Object;
  };
  vnfs: Object;
  networks: Object;
  collectionResource: Object;
  configurations: Object;
  serviceProxies: Object;
  vfModules: Object;
  volumeGroups: Object;
  pnfs:Object;
  vnfGroups: Object;

  constructor(){
    this.service = <any>{};
    this.vnfs = {};
    this.networks= {};
    this.collectionResource = {};
    this.configurations = {};
    this.serviceProxies = {};
    this.vfModules = {};
    this.volumeGroups = {};
    this.pnfs = {};
    this.vnfGroups = {};
  }

}
