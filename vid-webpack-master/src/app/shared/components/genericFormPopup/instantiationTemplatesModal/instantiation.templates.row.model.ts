import * as moment from 'moment';
import * as _ from 'lodash';

export class InstantiationTemplatesRowModel {
  readonly jobId: string;
  readonly userId ?: string;
  readonly createDate ?: string;
  readonly instanceName ?: string;
  readonly instantiationStatus?: string;
  readonly summary?: string;
  readonly region?: string;
  readonly tenant?: string;
  readonly aicZone?: string;
  readonly serviceModelId: string;

  constructor(data) {
    this.jobId = data.jobId;
    this.serviceModelId = data.serviceModelId;
    this.userId = !_.isNil(data.created) ? data.userId : null;
    this.createDate = !_.isNil(data.created) ? moment(data.created).format("YYYY-MM-DD HH:mm:ss") : null;
    this.instanceName = this.getInstanceName(data.serviceInstanceName);
    this.instantiationStatus = !_.isNil(data.jobStatus) ? data.jobStatus : null;
    this.summary = null;
    this.region = this.getRegion(data.regionId, data.owningEntityName);
    this.tenant = !_.isNil(data.tenantName) ? data.tenantName : null;
    this.aicZone = !_.isNil(data.aicZoneName) ? data.aicZoneName : null;

  }


  /**************************************************************************************************
   return the LCP region and in brackets the cloud owner removing the “att-“ with capital letters.
   **************************************************************************************************/
  getCloudOwner = (owningEntityName: string): string => {
    const splitByAtt: string[] = owningEntityName.split('att-');
    let owning: string = splitByAtt[splitByAtt.length - 1];
    return owning.toUpperCase();
  };

  getRegion = (regionId: string, owningEntityName: string): string => {
    const convertOwning = !_.isNil(owningEntityName) ? `(${this.getCloudOwner(owningEntityName)})` : '';
    return `${regionId} ${convertOwning}`.trim();
  };


  getInstanceName = (instanceName?: string): string => {
    if (_.isNil(instanceName)) {
      return '<Automatically generated>';
    }
    return instanceName;
  }
}

