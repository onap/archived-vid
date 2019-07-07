export class VPNModalRow {
  instanceId: string;
  instanceName: string;
  platformName: string;
  instanceType: string;
  region: string;
  customerId: string;
  modelInfo : {
    modelCustomizationId: string;
    modelInvariantId: string;
    modelVersionId:  string;
  };
  routeTargets: {
    "globalRouteTarget" : string,
    "routeTargetRole" : string
  }[];
}
