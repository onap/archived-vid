package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Based on this model:

{
  "requestDetails": {
      "modelInfo": {
          “modelType”: “vfModule”,
          “modelInvariantId”: “ff5256d2-5a33-55df-13ab-12abad84e7ff”,
          “modelVersionId”: “fe6478e5-ea33-3346-ac12-ab121484a3fe”,
          “modelCustomizationId”: “856f9806-b01a-11e6-80f5-76304dec7eb7”,
          “modelName”: “vSAMP12..base..module-0”,
          "modelVersion": "1"
      },
      “cloudConfiguration”: {
          “lcpCloudRegionId”: “mdt1”,
          “tenantId”: “88a6ca3ee0394ade9403f075db23167e”
      },
      "requestInfo": {
          “instanceName”: “MSOTEST103a-vSAMP12_base_module-0”,
          “source”: “VID”,
          “suppressRollback”: true,
          “requestorId”: “az2016”
      },
      "relatedInstanceList": [
         {
      // This related instance captures the volumeGroup to attach
            “relatedInstance”: {
               “instanceId”: “17ef4658-bd1f-4ef0-9ca0-ea76e2bf122c”,
               “instanceName”: “MSOTESTVOL103a-vSAMP12_base_module-0_vol”,
               “modelInfo”: {
                  “modelType”: “volumeGroup”
               }
            }
         },
         {
            “relatedInstance”: {
               “instanceId”: “{serviceInstanceId}”,
               “modelInfo”: {
                  “modelType”: “service”,
                  “modelInvariantId”: “ff3514e3-5a33-55df-13ab-12abad84e7ff”,
                  “modelVersionId”: “fe6985cd-ea33-3346-ac12-ab121484a3fe”,
                  “modelName”: “{parent service model name}”,
                  "modelVersion": "1.0"
               }
            }
         },
         {
            “relatedInstance”: {
               “instanceId”: “{vnfInstanceId}”,
               "modelInfo": {
                  “modelType”: “vnf”,
                  “modelInvariantId”: “ff5256d1-5a33-55df-13ab-12abad84e7ff”,
                  “modelVersionId”: “fe6478e4-ea33-3346-ac12-ab121484a3fe”,
                  “modelName”: “vSAMP12”,
                  "modelVersion": "1.0",
		     “modelCustomizationName”: “vSAMP12 1”,
		     “modelCustomizationId”: “a7f1d08e-b02d-11e6-80f5-76304dec7eb7”
               }
            }
         }
      ],
      “requestParameters”: {
          “usePreload”: true,
          “userParams”: []
      }
  }
}


 */

public class VfModuleInstantiationRequestDetails extends BaseResourceInstantiationRequestDetails {

    public VfModuleInstantiationRequestDetails(
            @JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
            @JsonProperty(value = "cloudConfiguration", required = true) CloudConfiguration cloudConfiguration,
            @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
            @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
            @JsonProperty(value = "requestParameters", required = true) RequestParametersVfModule requestParameters)
    {
        super(modelInfo, cloudConfiguration, requestInfo, relatedInstanceList, requestParameters);
    }

    public static class RequestParametersVfModule extends BaseResourceInstantiationRequestDetails.RequestParameters {
        private final boolean usePreload;

        public RequestParametersVfModule(List<? extends UserParamTypes> userParams, boolean usePreload) {
            super(userParams);
            this.usePreload = usePreload;
        }

        public boolean isUsePreload() {
            return usePreload;
        }
    }

    public static class UserParamMap<K,V> extends HashMap<K,V> implements UserParamTypes, Map<K,V>  {

        public UserParamMap() {
            super();
        }
    }
}

