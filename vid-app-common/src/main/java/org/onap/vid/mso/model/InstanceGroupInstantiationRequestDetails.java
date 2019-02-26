/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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

