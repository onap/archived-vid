package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/* Based on this model:


// {
//   "requestDetails": {
//       "modelInfo": {
//          “modelType”: “vnf”,
//          “modelInvariantId”: “ff5256d1-5a33-55df-13ab-12abad84e7ff”,
//          “modelVersionId”: “fe042c22-ba82-43c6-b2f6-8f1fc4164091”,
//          “modelName”: “vSAMP12”,
//          "modelVersion": "1.0",
//          “modelCustomizationName”: “vSAMP12 1”,
//          “modelCustomizationId”: “a7f1d08e-b02d-11e6-80f5-76304dec7eb7”
//       },
//       “cloudConfiguration”: {
//           “lcpCloudRegionId”: “mdt1”,
//           “tenantId”: “88a6ca3ee0394ade9403f075db23167e”
//       },
//       "requestInfo": {
//           “instanceName”: “MSOTEST103a”,
//           “productFamilyId”: “a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb”,
//           “source”: “VID”,
//           “suppressRollback”: false,
//           “requestorId”: “az2016”
//       },
//      "platform": {
//        "platformName": "{some platformName}"
//      },
//      "lineOfBusiness": {
//        "lineOfBusinessName": "{some string}"
//      },
//       "relatedInstanceList": [
//          {
//             “relatedInstance”: {
//                “instanceId”: “{serviceInstanceId}”,
//                “modelInfo”: {
//                   “modelType”: “service”,
//                   “modelInvariantId”: “ff3514e3-5a33-55df-13ab-12abad84e7ff”,
//                   “modelVersionId”: “fe6985cd-ea33-3346-ac12-ab121484a3fe”,
//                   “modelName”: “{parent service model name}”,
//                   "modelVersion": "1.0"
//                }
//             }
//          },
//          {
//             “relatedInstance”: {
//                “instanceId”: “{instanceGroupId}”,
//                “modelInfo”: {
//                   “modelType”: “networkCollection”,
//                   “modelInvariantId”: “9ea660dc-155f-44d3-b45c-cc7648b4f31c”,
//                   “modelVersionId”: “bb07aad1-ce2d-40c1-85cb-5392f76bb1ef”,
//                   “modelName”: “{network collection model name}”,
//                   "modelVersion": "1.0"
//                }
//             }
//          }

//       ],
//       “requestParameters”: {
//             “userParams”: []
//       }
//   }
// }

 */

public class VnfInstantiationRequestDetails extends BaseResourceInstantiationRequestDetails {

    public VnfInstantiationRequestDetails(
            @JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
            @JsonProperty(value = "cloudConfiguration", required = true) CloudConfiguration cloudConfiguration,
            @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
            @JsonProperty(value = "platform", required = true) Platform platform,
            @JsonProperty(value = "lineOfBusiness", required = true) LineOfBusiness lineOfBusiness,
            @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
            @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters)
    {
        super(modelInfo, cloudConfiguration, requestInfo, platform, lineOfBusiness, relatedInstanceList, requestParameters);
    }
}

