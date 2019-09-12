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

"use strict";

var DetailsService = function ($log, DataService, ComponentService, COMPONENT,
                               FIELD, UtilityService) {

    var _this = this;

    var getSummaryList = function () {
        var model = DataService.getModelInfo(COMPONENT.SERVICE);
        switch (_this.componentId) {
            case COMPONENT.NETWORK:
            case COMPONENT.SERVICE:
            case COMPONENT.VNF:
            case COMPONENT.VF_MODULE:
            case COMPONENT.VOLUME_GROUP:
                return [{
                    name: FIELD.NAME.SUBSCRIBER_NAME,
                    value: DataService.getSubscriberName()
                }, {
                    name: FIELD.NAME.SERVICE_INSTANCE_ID,
                    value: DataService.getServiceInstanceId()
                }, {
                    name: FIELD.NAME.SERVICE_TYPE,
                    value: DataService.getServiceType()
                }, {
                    name: FIELD.NAME.MODEL_NAME,
                    value: model.modelName
                }, {
                    name: FIELD.NAME.MODEL_VERSION,
                    value: model.modelVersion
                }];
        }
    };

    var getDetailsList = function () {
        switch (_this.componentId) {
            case COMPONENT.NETWORK:
            case COMPONENT.SERVICE:
            case COMPONENT.VNF:
            case COMPONENT.VF_MODULE:
            case COMPONENT.VOLUME_GROUP:
                var model = DataService.getModelInfo(_this.componentId);
                var metaData = ComponentService.getDisplayNames(ComponentService
                    .getInventoryParameterList(_this.componentId, DataService
                        .getInventoryItem(), false));
                if (model && !UtilityService.isObjectEmpty(model.modelName) && !UtilityService.isObjectEmpty(model.modelVersion)) {
                    metaData = metaData.concat([
                        {
                            id: FIELD.NAME.MODEL_NAME,
                            name: FIELD.NAME.MODEL_NAME,
                            value: model.modelName
                        }, {
                            id: FIELD.NAME.MODEL_VERSION,
                            name: FIELD.NAME.MODEL_VERSION,
                            value: model.modelVersion
                        }
                    ]);
                }
                return metaData;
        }
    };

    var getMsoFilterString = function () {

        var instanceId = "";

        switch (_this.componentId) {
            case COMPONENT.NETWORK:
                instanceId = DataService.getNetworkInstanceId();
                break;
            case COMPONENT.SERVICE:
                instanceId = DataService.getServiceInstanceId();
                break;
            case COMPONENT.VNF:
                instanceId = DataService.getVnfInstanceId();
                break;
            case COMPONENT.VF_MODULE:
                instanceId = DataService.getVfModuleInstanceId();
                break;
            case COMPONENT.VOLUME_GROUP:
                instanceId = DataService.getVolumeGroupInstanceId();
        }

        return "filter=" + _this.componentId + "InstanceId:EQUALS:"
            + instanceId;
    };

    return {
        initializeComponent: function (componentId) {
            _this.componentId = ComponentService.initialize(componentId);
        },
        getComponentDisplayName: ComponentService.getComponentDisplayName,
        getSummaryList: getSummaryList,
        getDetailsList: getDetailsList,
        getMsoFilterString: getMsoFilterString
    };
};

appDS2.factory("DetailsService", ["$log", "DataService", "ComponentService",
    "COMPONENT", "FIELD", "UtilityService", DetailsService]);
