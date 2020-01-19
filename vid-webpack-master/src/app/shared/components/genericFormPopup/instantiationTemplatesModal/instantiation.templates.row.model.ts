import * as moment from 'moment';
import * as _ from 'lodash';
import {InstantiationBase} from "../../../models/InstantiationBase";

export class InstantiationTemplatesRowModel extends InstantiationBase{
  readonly userId ?: string;
  readonly createDate ?: string;
  readonly instanceName ?: string;
  readonly instantiationStatus?: string;
  readonly summary?: string;
  readonly region?: string;
  readonly tenant?: string;

  constructor(data) {
    super(data);
    this.userId = !_.isNil(data.userId) ? data.userId : null;
    this.createDate = !_.isNil(data.created) ? moment(data.created).format("YYYY-MM-DD HH:mm:ss") : null;
    this.instanceName = this.getInstanceName(data.serviceInstanceName);
    this.instantiationStatus = !_.isNil(data.jobStatus) ? data.jobStatus : null;
    this.summary = this.convertRequestSummaryFromMapToString(data.requestSummary);
    this.region =  !_.isNil(data.regionName) ? data.regionName : null;
    this.tenant = !_.isNil(data.tenantName) ? data.tenantName : null;
  }

  getInstanceName = (instanceName?: string): string => {
    if (_.isNil(instanceName)) {
      return '<Automatically generated>';
    }
    return instanceName;
  };

  convertRequestSummaryFromMapToString = (requestSummary: Map<string, number>): string => {
    let values: string[] = _.map(requestSummary, (count: number, instanceType: string) => instanceType + ": " + count);
    return _.join(values, ", ");
  }

}

