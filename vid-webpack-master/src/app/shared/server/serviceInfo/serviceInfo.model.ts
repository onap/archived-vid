import {ServiceStatus} from '../../../instantiationStatus/instantiationStatus.component.service';
import {ServiceAction} from "../../models/serviceInstanceActions";

export class ServiceInfoModel {
  id: number;
  created: Date;
  modified: Date;
  createdId: number;
  modifiedId: number;
  numRow: number;
  uuid: string;
  userId: string;
  requestId: string;
  aLaCarte: boolean;
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
  subscriberId: string;
  serviceInstanceId: string;
  serviceInstanceName: string;
  serviceModelId: string;
  serviceModelName: string;
  serviceModelVersion: string;
  templateId: string;
  auditUserId: string;
  jobId: string;
  action: ServiceAction;
  isRetryEnabled: boolean;
}

export class ServiceInfoUiModel extends ServiceInfoModel{
  serviceStatus : ServiceStatus;
  serviceIndex : number;
}
