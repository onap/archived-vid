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
}
