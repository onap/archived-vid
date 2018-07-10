import {ServiceStatus} from '../../../instantiationStatus/instantiationStatus.component.service';

export class ServiceInfoModel {
  id: number;
  created: Date;
  modified: Date;
  createdId: number;
  modifiedId: number;
  numRow: number;
  uuid: string;
  userId: string;
  jobStatus: string;
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
  serviceInstanceId: string;
  serviceInstanceName: string;
  serviceModelId: string;
  serviceModelName: string;
  serviceModelVersion: string;
  templateId: string;
  auditUserId: string;
  jobId: string;
}

export class ServiceInfoUiModel extends ServiceInfoModel{
  serviceStatus : ServiceStatus;
  serviceIndex : number;
}
