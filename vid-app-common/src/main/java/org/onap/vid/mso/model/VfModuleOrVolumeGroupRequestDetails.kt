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
package org.onap.vid.mso.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import java.util.*

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

class VfModuleOrVolumeGroupRequestDetails(
        modelInfo: ModelInfo,
        cloudConfiguration: CloudConfiguration?,
        requestInfo: RequestInfo,
        relatedInstanceList: List<RelatedInstance>?,
        requestParameters: RequestParametersVfModuleOrVolumeGroup?)
    : BaseResourceInstantiationRequestDetails(modelInfo, cloudConfiguration, requestInfo, relatedInstanceList, requestParameters)

open class RequestParametersVfModuleOrVolumeGroup internal constructor(
        userParams: List<UserParamTypes>,
        val isUsePreload: Boolean,
        testApi: String?
) : BaseResourceInstantiationRequestDetails.RequestParameters(userParams, testApi)

class RequestParametersVfModuleOrVolumeGroupInstantiation(
        userParams: List<UserParamTypes>,
        usePreload: Boolean?,
        testApi: String?
) : RequestParametersVfModuleOrVolumeGroup(userParams, usePreload.orFalse(), testApi)

class RequestParametersVfModuleUpgrade(
        userParams: List<UserParamTypes>,
        usePreload: Boolean?,
        testApi: String?,
        @get:JsonInclude(NON_NULL) val retainAssignments: Boolean?,
        @get:JsonInclude(NON_NULL) val rebuildVolumeGroups: Boolean?
) : RequestParametersVfModuleOrVolumeGroup(userParams, usePreload.orFalse(), testApi)

class UserParamMap<K, V> : HashMap<K, V>(), UserParamTypes, MutableMap<K, V>

private fun Boolean?.orFalse(): Boolean = this ?: false
