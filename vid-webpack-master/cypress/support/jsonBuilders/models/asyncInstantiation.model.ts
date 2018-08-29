/// <reference types="Cypress" />

export class AsyncInstantiationModel{
  id: number;
  created : number;
  modified: number;
  createdId : string;
  modifiedId : string;
  rowNum : string;
  auditUserId : string;
  auditTrail : string;
  jobId : string;
  templateId : string;
  userId : string;
  jobStatus : string;
  statusModifiedDate: number;
  hidden : boolean;
  pause: boolean;
  owningEntityId: string;
  owningEntityName: string;
  project: string;
  aicZoneId: string;
  aicZoneName: string;
  tenantId: string;
  tenantName: string;
  regionId: string;
  regionName: string;
  serviceType: string;
  subscriberName: string;
  serviceInstanceId : number;
  serviceInstanceName: string;
  serviceModelId: string;
  serviceModelName: string;
  serviceModelVersion: string;
  createdBulkDate: number;
  action: string;

  constructor(){
    this.id = 0;
    this.created = 0;
    this.modified= 0;
    this.createdId = '';
    this.modifiedId = '';
    this.rowNum = '';
    this.auditUserId = '';
    this.auditTrail = '';
    this.jobId = '';
    this.templateId = '';
    this.userId = '';
    this.jobStatus = '';
    this.statusModifiedDate= 0 ;
    this.hidden = false;
    this.pause= false;
    this.owningEntityId= '';
    this.owningEntityName= '';
    this.project= '';
    this.aicZoneId= '';
    this.aicZoneName= '';
    this.tenantId= '';
    this.tenantName= '';
    this.regionId= '';
    this.regionName= '';
    this.serviceType= '';
    this.subscriberName= '';
    this.serviceInstanceId = 0;
    this.serviceInstanceName= '';
    this.serviceModelId= '';
    this.serviceModelName= '';
    this.serviceModelVersion= '';
    this.createdBulkDate= 0;
    this.action = '';
  }
}
