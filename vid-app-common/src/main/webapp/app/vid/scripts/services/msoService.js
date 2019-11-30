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

var MsoService = function($http, $log, $q, PropertyService, AaiService, UtilityService, COMPONENT, FIELD, moment) {

    var _this = this;

	/*
	 * Common function to handle both create and delete instance requests
	 */
    var requestInstanceUpdate = function(request, successCallbackFunction) {
        $log.debug("MsoService:requestInstanceUpdate: request:");
        $log.debug(request);
        $http.post( "mso/" + request.url, {
            requestDetails : request.requestDetails
        }, {
            timeout : PropertyService.getServerResponseTimeoutMsec()
        }).then(successCallbackFunction)["catch"]
        (UtilityService.runHttpErrorHandler);
    };

    var checkValidStatus = function(response) {
        if (response.data.status < 200 || response.data.status > 202) {
            throw {
                type : FIELD.ID.MSO_FAILURE
            };
        }
    };

    var addListEntry = function(name, value) {
        var entry = '"' + name + '": ';
        if (value === undefined) {
            return entry + "undefined";
        } else {
            return entry + '"' + value + '"';
        }
    };

    var buildPayloadForServiceActivateDeactivate = function (model, userId) {
        var requestDetails = {
                "modelInfo": {
                    "modelType": "service",
                    "modelInvariantId": model.service.invariantUuid,
                    "modelVersionId": model.service.uuid,
                    "modelName": model.service.name,
                    "modelVersion": model.service.version
                },
                "requestInfo": {
                    "source": "VID",
                    "requestorId": userId
                },
                "requestParameters": {
                    // aicZone was sent from here
                    "userParams": []
                }
        };

        $log.debug("Service Activate/Deactivate payload", requestDetails);

        return requestDetails;

    };

    var buildPayloadForActivateFabricConfiguration = function (model, userId) {
        var requestDetails = {
            "modelInfo": {
                "modelType": "service",
                "modelInvariantId": model.service.invariantUuid,
                "modelVersionId": model.service.uuid,
                "modelName": model.service.name,
                "modelVersion": model.service.version
            },
            "requestInfo": {
                "source": "VID",
                "requestorId": userId
            },
            "requestParameters": {
                "aLaCarte": false
            }
        };

        $log.debug("Service Activate Fabric Configuration payload", requestDetails);

        return requestDetails;

    };

    var activateInstance = function(requestParams) {
        var requestDetails = buildPayloadForServiceActivateDeactivate(requestParams.model, requestParams.userId);

        return sendPostRequest(COMPONENT.MSO_ACTIVATE_INSTANCE.replace('@serviceInstanceId', requestParams.instance.serviceInstanceId),
            requestDetails);
    };

    var deactivateInstance = function(requestParams) {
        var requestDetails = buildPayloadForServiceActivateDeactivate(requestParams.model, requestParams.userId);

        return sendPostRequest(COMPONENT.MSO_DEACTIVATE_INSTANCE.replace('@serviceInstanceId', requestParams.instance.serviceInstanceId),
            requestDetails);
    };

    var sendPostRequestWithBody = function(url, requestBody) {
        var deferred = $q.defer();
        if (url) {
            $http.post(url, requestBody, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            }).success(function (response) {
                deferred.resolve({data: response});
            }).error(function (data, status) {
                deferred.reject({message: data, status: status});
            });
        }

        return deferred.promise;
    };

    var sendPostRequest = function(url, requestDetails) {
        return sendPostRequestWithBody(url, {requestDetails: requestDetails});
    };

    return {
        createInstance : requestInstanceUpdate,
        deleteInstance : requestInstanceUpdate,
        createAndDeleteInstance: function(requestParams)  {
            return sendPostRequest("mso/" + requestParams.url, requestParams.requestDetails);
        },
        getOrchestrationRequest : function(requestId, successCallbackFunction) {
            $log.debug("MsoService:getOrchestrationRequest: requestId: "
                + requestId);
            $http.get(
                "mso/mso_get_orch_req/"
                + requestId + "?r=" + Math.random(),
                {
                    timeout : PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(successCallbackFunction)["catch"]
            (UtilityService.runHttpErrorHandler);
        },
        getOrchestrationRequests : function(filterString,
                                            successCallbackFunction) {
            $log.debug("MsoService:getOrchestrationRequests: filterString: "
                + filterString);
            $http.get(
                "mso/mso_get_orch_reqs/"
                + encodeURIComponent(filterString) + "?r="
                + Math.random(),
                {
                    timeout : PropertyService
                        .getServerResponseTimeoutMsec()
                }).then(successCallbackFunction)["catch"]
            (UtilityService.runHttpErrorHandler);
        },
        getFormattedCommonResponse : function(response) {
            let HttpStatusText = UtilityService.getHttpStatusText(response.data.status);
            if(HttpStatusText === 'Unknown (undefined)'){
                HttpStatusText = 'What do you expect to be written here'
            }
            return UtilityService.getCurrentTime() + " HTTP Status: "
                + HttpStatusText
                + "\n" + angular.toJson(response.data.entity, true);

        },
        checkValidStatus : checkValidStatus,
        getFormattedGetOrchestrationRequestsResponse : function(response) {
            UtilityService.checkUndefined(COMPONENT.ENTITY, response.data.entity);
            UtilityService.checkUndefined(COMPONENT.STATUS, response.data.status);
            checkValidStatus(response);

            var list = response.data.entity.requestList;
            UtilityService.checkUndefined(FIELD.ID.REQUEST_LIST, list);

            var message = "";

            for (var i = 0; i < list.length; i++) {
                var request = list[i].request;
                message += addListEntry(FIELD.ID.REQUEST_ID, request.requestId) + ",\n";
                message += addListEntry(FIELD.ID.REQUEST_TYPE, request.requestType)
                    + ",\n";
                var status = request.requestStatus;
                if (status === undefined) {
                    message += addListEntry(FIELD.ID.REQUEST_STATUS, undefined) + "\n";
                } else {
                    if(status.finishTime) {
                        message += addListEntry(FIELD.ID.TIMESTAMP, moment(new Date(status.finishTime)).format("ddd, DD MMM YYYY HH:mm:ss"))
                            + ",\n";
                    }
                    message += addListEntry(FIELD.ID.REQUEST_STATE, status.requestState)
                        + ",\n";
                    message += addListEntry(FIELD.ID.REQUEST_STATUS,
                            status.statusMessage)
                        + ",\n";
                    message += addListEntry(FIELD.ID.PERCENT_PROGRESS,
                            status.percentProgress)
                        + "\n";
                }
                if (i < (list.length - 1)) {
                    message += "\n";
                }
            }
            return message;
        },
        getFormattedSingleGetOrchestrationRequestResponse : function (response) {
            UtilityService.checkUndefined(COMPONENT.ENTITY, response.data.entity);
            UtilityService.checkUndefined(COMPONENT.STATUS, response.data.status);
            //checkValidStatus(response);

            var message = "";
            if (! (response && response.data && response.data.entity)) {
                return message;
            }
            if ( UtilityService.hasContents (response.data.entity.request) ) {
                var request = response.data.entity.request;
                message += addListEntry(FIELD.ID.REQUEST_ID, request.requestId) + ",\n";
                message += addListEntry(FIELD.ID.REQUEST_TYPE, request.requestType)
                    + ",\n";
                var status = request.requestStatus;
                if (status === undefined) {
                    message += addListEntry(FIELD.ID.REQUEST_STATUS, undefined) + "\n";
                } else {
                    message += addListEntry(FIELD.ID.TIMESTAMP, moment(new Date()).format("ddd, DD MMM YYYY HH:mm:ss"))
                        + ",\n";
                    message += addListEntry(FIELD.ID.REQUEST_STATE, status.requestState)
                        + ",\n";
                    message += addListEntry(FIELD.ID.REQUEST_STATUS,
                            status.statusMessage)
                        + ",\n";
                    message += addListEntry(FIELD.ID.PERCENT_PROGRESS,
                            status.percentProgress)
                        + "\n\n";
                }
            }
            else {
                if (UtilityService.hasContents(response.data.status) && UtilityService.hasContents(response.data.entity)) {
                    message = this.getFormattedCommonResponse(response) + "\n";
                }
            }
            return message;
        },
        getManualTasks : function(requestId) {
            $log.debug("MsoService:getManualTasks: requestId: "
                + requestId);
            return $http.get(
                "mso/mso_get_man_task/" + requestId,
                {
                    timeout: PropertyService
                        .getServerResponseTimeoutMsec()
                })
                .catch(UtilityService.runHttpErrorHandler);
        },
        completeTask: function(taskId, taskToComplete) {
            var promise = new Promise(function (resolve, reject) {
                $log.debug("MsoService:completeTask: taskId: "
                    + taskId);
                AaiService.getLoggedInUserID(function (response) {
                    var attuid = response.data;
                    var source = "VID";
                    var data = {
                        requestDetails: {
                            requestInfo: {
                                source: source,
                                responseValue: taskToComplete,
                                requestorId: attuid
                            }
                        }
                    };

                    return $http.post(
                        "mso/mso_post_man_task/" + taskId, data,
                        {
                            timeout: PropertyService
                                .getServerResponseTimeoutMsec()
                        }).then(function (response) {
                            resolve(response);
                    })
                        .catch(UtilityService.runHttpErrorHandler);
                }, function () {
                    reject();
                });
            });

            return promise;
        },
        showResponseContentError : function(error, showFunction) {
            switch (error.type) {
                case "undefinedObject":
                    showFunction(FIELD.ERROR.SYSTEM_FAILURE, error.message);
                    break;
                case "msoFailure":
                    showFunction(FIELD.ERROR.MSO, "");
                    break;
                default:
                    showFunction(FIELD.ERROR.SYSTEM_FAILURE);
            }
        },
        activateInstance: activateInstance,
        deactivateInstance: deactivateInstance,


        createConfigurationInstance: function(requestParams) {

            const modelInfoOf = function (instance) {
                const modelInfo = {
                    "modelType": "vnf",
                    "modelInvariantId": instance.properties['model-invariant-id'],
                    "modelVersionId": instance.properties['model-version-id'],
                    "modelName": instance.properties['model-name'],
                    "modelVersion": instance.properties['model-version'],
                    "modelCustomizationId": instance.properties['model-customization-id']
                };

                $log.debug("model info from instance", instance);
                $log.debug("model info to model", modelInfo);

                return modelInfo;
            };

            var payload = {
                "requestDetails": {
                    "modelInfo": {
                        "modelType": "configuration",
                        "modelInvariantId": requestParams.configurationModelInfo.modelInvariantId,
                        "modelVersionId": requestParams.configurationModelInfo.modelNameVersionId,
                        "modelName": requestParams.configurationModelInfo.modelName, // "Port Mirroring Configuration"
                        "modelVersion": requestParams.configurationModelInfo.modelVersion,
                        "modelCustomizationId": requestParams.configurationModelInfo.customizationUuid,
                        "modelCustomizationName": requestParams.configurationModelInfo.modelCustomizationName
                    },
                    "cloudConfiguration": {
                        "lcpCloudRegionId": requestParams.portMirroringConfigFields.cloudRegionId,
                        "cloudOwner" : requestParams.portMirroringConfigFields.cloudOwner
                    },
                    "requestInfo": {
                        "instanceName": requestParams.portMirroringConfigFields.instanceName.value,
                        "source": "VID",
                        "requestorId": requestParams.attuuid
                    },
                    "relatedInstanceList": [
                        {
                            "relatedInstance": {
                                "instanceId": requestParams.topServiceInstanceId,
                                "modelInfo": {
                                    "modelType": "service", // relatedTopModelsInfo.modelType
                                    "modelInvariantId": requestParams.relatedTopModelsInfo.modelInvariantId,
                                    "modelVersionId": requestParams.relatedTopModelsInfo.modelNameVersionId,
                                    "modelName": requestParams.relatedTopModelsInfo.modelName,
                                    "modelVersion": requestParams.relatedTopModelsInfo.modelVersion
                                }
                            }
                        },
                        {
                            "relatedInstance": {
                                "instanceId": requestParams.portMirroringConfigFields.sourceInstance.properties['vnf-id'],
                                "instanceDirection": "source",
                                "modelInfo": modelInfoOf(requestParams.portMirroringConfigFields.sourceInstance)
                            }
                        },
                        {
                            "relatedInstance": requestParams.configurationByPolicy ? {
                                "instanceName": requestParams.portMirroringConfigFields.destinationInstance.properties['pnfName'],
                                "instanceDirection": "destination",
                                "modelInfo": {
                                    "modelType": "pnf"
                                }
                            } : {
                                "instanceId": requestParams.portMirroringConfigFields.destinationInstance.properties['vnf-id'],
                                "instanceDirection": "destination",
                                "modelInfo": modelInfoOf(requestParams.portMirroringConfigFields.destinationInstance)
                            }
                        }
                    ],
                    "requestParameters": {
                        "userParams": []
                    }
                }
            };

            $log.debug("payload", payload);

            var deferred = $q.defer();
            $http.post([
                'mso','mso_create_configuration_instance',
                requestParams.topServiceInstanceId,
                'configurations',''
            ].join(COMPONENT.FORWARD_SLASH),
                payload)
                .success(function (response) {
                    deferred.resolve({data : response});
                }).error(function (data, status) {
                deferred.reject({message: data, status: status});
            });
            return deferred.promise;
        },
        deleteConfiguration: function(requestParams, configuration) {

            var requestDetails = {
                "modelInfo": requestParams.configurationModel,
                "cloudConfiguration": {
                    "lcpCloudRegionId": configuration.configData.cloudRegionId
                },
                "requestInfo": {
                    "source": "VID",
                    "requestorId": requestParams.userId
                },
                "requestParameters": {
                    "userParams": []
                }
            };

            var url = "mso/mso_delete_configuration/" + requestParams.serviceInstanceId + "/configurations/" + requestParams.configurationId;
            return sendPostRequest(url, requestDetails);
        },
        toggleConfigurationStatus: function(requestParams, configuration) {

            var requestDetails = {
                "modelInfo": requestParams.configurationModel,
                "cloudConfiguration": {
                    "lcpCloudRegionId": configuration && configuration.configData ? configuration.configData.cloudRegionId : null
                },
                "requestInfo": {
                    "source": "VID",
                    "requestorId": requestParams.userId
                },
                "relatedInstanceList": [{
                    "relatedInstance": {
                        "instanceId": requestParams.serviceInstanceId,
                        "modelInfo": requestParams.serviceModel
                    }
                }],
                "requestParameters": {
                    "userParams": []
                }
            };

            var url;
            switch (requestParams.configStatus) {
                case FIELD.STATUS.AAI_CREATED:
                case FIELD.STATUS.AAI_INACTIVE:
                    url = "mso/mso_activate_configuration/"+requestParams.serviceInstanceId+"/configurations/"+requestParams.configurationId;
                    break;
                case FIELD.STATUS.AAI_ACTIVE:
                    url = "mso/mso_deactivate_configuration/"+requestParams.serviceInstanceId+"/configurations/"+requestParams.configurationId;
                    break;
            }

            return sendPostRequest(url, requestDetails);
        },

        togglePortStatus: function(requestParams, configuration, defaultParams) {

            var requestDetails = {
                "modelInfo": requestParams.configurationModel,
                "cloudConfiguration": {
                    "lcpCloudRegionId": configuration && configuration.configData ? configuration.configData.cloudRegionId : null
                },
                "requestInfo": {
                    "source": "VID",
                    "requestorId": requestParams.userId
                },
                "relatedInstanceList": [
                    {
                        "relatedInstance": {
                            "instanceId": requestParams.serviceInstanceId,
                            "modelInfo": requestParams.serviceModel
                        }
                    },
                    {
                        "relatedInstance": {
                            "instanceId": requestParams.portId,
                            "instanceDirection": "source",
                            "modelInfo": {
                                "modelType": "connectionPoint"
                            }
                        }
                    }
                ]
            };

            var url;
            switch (requestParams.portStatus) {
                case FIELD.STATUS.AAI_ENABLED:
                    url = "mso/mso_disable_port_configuration/"+requestParams.serviceInstanceId+"/configurations/"+requestParams.configurationId;
                    break;
                case FIELD.STATUS.AAI_DISABLED:
                    url = "mso/mso_enable_port_configuration/"+requestParams.serviceInstanceId+"/configurations/"+requestParams.configurationId;
                    break;
            }

            return sendPostRequest(url, requestDetails);
        },

        buildPayloadForAssociateDissociate: function(serviceModelInfo, attuuid, instanceId, pnf) {
            var payload = {
                    "modelInfo": {
                        "modelType": "service",
                        "modelInvariantId": serviceModelInfo.invariantUuid,
                        "modelVersionId": serviceModelInfo.uuid,
                        "modelName": serviceModelInfo.name,
                        "modelVersion": serviceModelInfo.version
                    },
                    "requestInfo": {
                        "source": "VID",
                        "requestorId": attuuid
                    },
                    "relatedInstanceList": [
                        {
                            "relatedInstance": {
                                "instanceName": pnf,
                                "modelInfo": {
                                    "modelType": "pnf"
                                }
                            }
                        }],
                    "requestParameters": {
                        "aLaCarte": true
                    }
            };

            $log.debug("payload", payload);

            return payload;
        },
        associatePnf: function(requestParams) {

            var payload = this.buildPayloadForAssociateDissociate(requestParams.serviceModelInfo, requestParams.attuuid, requestParams.instanceId, requestParams.pnf);
            return sendPostRequest([
                    COMPONENT.MSO, COMPONENT.MSO_CREATE_REALATIONSHIP,
                    requestParams.instanceId,
                    ''
                ].join(COMPONENT.FORWARD_SLASH), payload);
        },
        dissociatePnf: function(requestParams) {

            var payload = this.buildPayloadForAssociateDissociate(requestParams.serviceModelInfo, requestParams.attuuid, requestParams.serviceInstanceId, requestParams.pnf);

            return sendPostRequest([
                COMPONENT.MSO, COMPONENT.MSO_REMOVE_RELATIONSHIP,
                requestParams.serviceInstanceId,
                ''
            ].join(COMPONENT.FORWARD_SLASH), payload);
        },
        activateFabricConfiguration: function(requestParams) {
            var payload = buildPayloadForActivateFabricConfiguration(requestParams.model, requestParams.userId);

            var url = COMPONENT.MSO_ACTIVATE_FABRIC_CONFIGURATION_INSTANCE.replace('@serviceInstanceId', requestParams.serviceInstanceId);
            return sendPostRequest(url, payload);
        },
        deactivateAndCloudDelete : function (requestParams)  {
            var payload = {
                tenantId: requestParams.tenantId,
                lcpCloudRegionId: requestParams.lcpCloudRegionId,
                userId: requestParams.userId
            };

            var url = COMPONENT.MSO_DEACTIVATE_AND_CLOUD_DELETE_INSTANCE.replace('@serviceInstanceId', requestParams.serviceInstanceId)
                        .replace('@vnfInstanceId', requestParams.vnfInstanceId)
                        .replace('@vfModuleInstanceId', requestParams.vfModuleInstanceId);

            return sendPostRequestWithBody(url, payload);
        }
    };
};

appDS2.factory("MsoService", MsoService );
