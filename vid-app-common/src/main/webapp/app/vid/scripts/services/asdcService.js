/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

"use strict";

var AsdcService = function ($http, $log, PropertyService, UtilityService, VIDCONFIGURATION) {
    return {
        getModel: function (modelId, successCallbackFunction) {
            $log.debug("AsdcService:getModel: modelId: " + modelId);
            $http.get(
                "asdc/getModel/" + modelId
                + "?r=" + Math.random(),
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(successCallbackFunction)["catch"]
            (UtilityService.runHttpErrorHandler);
        },

        isMacro: function (serviceModel) {
            switch (serviceModel.service.instantiationType) {
                case 'Macro':
                case 'Both':
                    return true;
                case 'A-La-Carte':
                    return false;
                case 'ClientConfig':
                    console.debug("Looking for " + serviceModel.service.invariantUuid + " by Client Config");
                    return UtilityService.arrayContains(VIDCONFIGURATION.MACRO_SERVICES, serviceModel.service.invariantUuid);
                default:
                    console.debug("Unexpected serviceModel.service.instantiationType: " + serviceModel.service.instantiationType);
                    return true;
            }
        }
    }
}

appDS2.factory("AsdcService", ["$http", "$log", "PropertyService",
    "UtilityService", "VIDCONFIGURATION", AsdcService]);
