import { RequestInfo, RequestDetails, ModelInfo, CloudConfiguration, LineOfBusiness, Platform, RelatedInstanceList, VfcModel } from "./models/requestDetails.model";
import * as _ from "lodash";

function extractModelInfoFromNodeTemplate(node: any, type: string) {
  let modelInfo: ModelInfo = <ModelInfo>{};
  if (node["customizationUuid"] !== undefined) {
    modelInfo.modelCustomizationId = node["customizationUuid"];
  }
  if (node["modelCustomizationName"] !== undefined) {
    modelInfo.modelCustomizationName = node["modelCustomizationName"];
  }
  modelInfo.modelVersionId = node["uuid"];
  modelInfo.modelName = node["name"];
  modelInfo.modelInvariantId = node["invariantUuid"];
  modelInfo.modelType = type;
  modelInfo.modelVersion = node["version"];
  return modelInfo;
}

function extractRequestInfo(userInputs: any, userId: string): RequestInfo {
  let requestInfo: RequestInfo = <RequestInfo>{};
  if (userInputs["instanceName"] !== undefined) requestInfo.instanceName = userInputs["instanceName"];
  requestInfo.productFamilyId = userInputs["productFamily"];
  requestInfo.source = "VID";
  requestInfo.suppressRollback = userInputs["rollback"] === "false";
  requestInfo.requestorId = userId;
  return requestInfo;
}

function extractPlatform(userInputs: any): Platform {
  let platform: Platform = <Platform>{};
  platform.platformName = userInputs["platformName"];
  return platform;
}

function extractVfcGroupModelAccordingToUuid(vnfModel: any, vfcUuid: string) {
  return _.find(vnfModel.vfcInstanceGroups, { uuid: vfcUuid });
}

function extractLineOfBusiness(userInputs: any) {
  let lob: LineOfBusiness = <LineOfBusiness>{};
  lob.lineOfBusinessName = userInputs["lineOfBusiness"];
  return lob;
}

function extractCloudConfiguration(userInputs: any) {
  let cloudConfig: CloudConfiguration = <CloudConfiguration>{};
  cloudConfig.lcpCloudRegionId = userInputs["lcpRegion"];
  cloudConfig.tenantId = userInputs["tenantId"];
  return cloudConfig;
}

function extractModelInfoFromVfcNode(vfcModel: VfcModel): ModelInfo {
  let modelinfo: ModelInfo = <ModelInfo>{};
  modelinfo.modelName = vfcModel.name;
  modelinfo.modelType = "networkCollection";
  modelinfo.modelVersion = vfcModel.version;
  modelinfo.modelVersionId = vfcModel.uuid;
  modelinfo.modelInvariantId = vfcModel.invariantUuid;
  return modelinfo;
}

export function createRequest(userId: string, userInputs: any, service: any, serviceInstanceId: string, networkInstanceGroups: any, vnfCustomizationName: string, vnfCustomizationId: string) {
  let request: RequestDetails = <RequestDetails>{};
  request.requestInfo = extractRequestInfo(userInputs, userId);
  request.lineOfBusiness = extractLineOfBusiness(userInputs);
  request.cloudConfiguration = extractCloudConfiguration(userInputs);
  request.platform = extractPlatform(userInputs);
  request.modelInfo = extractModelInfoFromNodeTemplate(service.vnfs[vnfCustomizationName], "vnf");
  request.requestParameters = { userParams: [], testApi: sessionStorage.getItem("msoRequestParametersTestApiValue")};
  request.relatedInstanceList = [];
  let serviceRelatedInstance: RelatedInstanceList = {
    relatedInstance: {
      instanceId: serviceInstanceId,
      modelInfo: extractModelInfoFromNodeTemplate(service.service, "service")
    }
  };
  request.relatedInstanceList.push(serviceRelatedInstance);
  _.forOwn(networkInstanceGroups, function(group) {
    let modelUuid = group["instance-group"]["model-version-id"];
    let vfcModel = extractVfcGroupModelAccordingToUuid(service.vnfs[vnfCustomizationName], modelUuid);
    let networkInstanceGroup: RelatedInstanceList = {
      relatedInstance: {
        instanceId: group["instance-group"].id,
        modelInfo: extractModelInfoFromVfcNode(vfcModel)
      }
    };
    request.relatedInstanceList.push(networkInstanceGroup);
  });

  return request;
}
