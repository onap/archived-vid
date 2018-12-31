package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/* Based on this model:

//{
//  "requestDetails": {
//    "modelInfo": {
//      "modelType": "instanceGroup",
//      "modelVersionId": "ddcbbf3d-f2c1-4ca0-8852-76a807285efc"
//    },
//    "requestInfo": {
//      "source": "VID",
//      "requestorId": "az2016"
//    },
//    "relatedInstanceList": [
//      {
//        "relatedInstance": {
//          "instanceId": "{the relate-to/parent serviceInstanceId}",
//          "modelInfo": {
//            "modelType": "service",
//	     "modelVersionId": "b3b7e7d3-ecb9-4a91-8f6d-e60d236e8e77",
//          }
//        }
//      }
//    ],
//    "requestParameters": {
//      "userParams": []
//    }
//  }
//}

 */

public class InstanceGroupInstantiationRequestDetails extends BaseResourceInstantiationRequestDetails {

    public InstanceGroupInstantiationRequestDetails(@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                                    @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                                    @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
                                                    @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters)
    {
        super(modelInfo, null, requestInfo, null, null, relatedInstanceList, requestParameters);
    }
}

