import {AuditStatus} from "../../server/serviceInfo/AuditStatus.model";

export class AuditInformationItem {
  showVidStatus : boolean;
  showMoreAuditInfoLink: boolean;
  vidInfoData: AuditStatus[] = [];
  msoInfoData: AuditStatus[] = [];
  isLoading: boolean;
  isAlaCarte: boolean;
  isALaCarteFlagOn: boolean;
  jobId: string;
  serviceModelId: string;

  constructor(showVidStatus : boolean,
              showMoreAuditInfoLink: boolean,
              vidInfoData: AuditStatus[],
              msoInfoData: AuditStatus[],
              isLoading: boolean,
              isAlaCarte: boolean,
              isALaCarteFlagOn: boolean,
              jobId: string,
              serviceModelId: string) {
    this.showVidStatus = showVidStatus;
    this.showMoreAuditInfoLink = showMoreAuditInfoLink;
    this.vidInfoData = vidInfoData;
    this.msoInfoData = msoInfoData;
    this.isLoading = isLoading;
    this.isAlaCarte = isAlaCarte;
    this.isALaCarteFlagOn = isALaCarteFlagOn;
    this.jobId = jobId;
    this.serviceModelId = serviceModelId;
  }
}
