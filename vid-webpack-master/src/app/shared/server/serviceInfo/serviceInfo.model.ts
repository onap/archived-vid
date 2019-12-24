import {ServiceStatus} from '../../../instantiationStatus/instantiationStatus.component.service';
import {ServiceAction} from "../../models/serviceInstanceActions";
import {InstantiationBase} from "../../models/InstantiationBase";

export class ServiceInfoModel extends InstantiationBase{
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
  serviceModelName: string;
  serviceModelVersion: string;
  templateId: string;
  auditUserId: string;
  action: ServiceAction;
  isRetryEnabled: boolean;
  requestSummary:string;
}

export class ServiceInfoUiModel extends ServiceInfoModel{
  serviceStatus : ServiceStatus;
  serviceIndex : number;
}
