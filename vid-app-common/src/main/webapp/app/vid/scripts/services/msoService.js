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

var MsoService = function($http, $log, PropertyService, AaiService, UtilityService, COMPONENT, FIELD, $q) {

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
    }

    var checkValidStatus = function(response) {
        if (response.data.status < 200 || response.data.status > 202) {
            throw {
                type : FIELD.ID.MSO_FAILURE
            }
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

    var activateInstance = function(instance, model) {
        var deferred = $q.defer();

        AaiService.getLoggedInUserID(function (response) {
            var userID = response.data;

            AaiService.getAicZoneForPNF(instance.globalCustomerId, model.service.serviceType, instance.serviceInstanceId, function (aicZone) {

                var requestDetails = {
                    "modelInstanceId": serviceInstanceId,
                    "requestDetails": {
                        "modelInfo": {
                            "modelType": "service",
                            "modelInvariantId": model.service.invariantUuid,
                            "modelVersionId": model.service.uuid,
                            "modelName": model.service.name,
                            "modelVersion": model.service.version
                        },
                        "requestInfo": {
                            "source": "VID",
                            "requestorId": userID
                        },
                        "requestParameters": {
                            "userParams": {
                                "name": "aic_zone",
                                "value": aicZone
                            }
                        }
                    }
                };

                console.log("requestDetails", requestDetails);

                $http.post(COMPONENT.MSO_ACTIVATE_INSTANCE.replace('@serviceInstanceId', requestDetails.modelInstanceId),
                    requestDetails.requestDetails)
                    .success(function (response) {
                        deferred.resolve({data: response});
                    })
                    .error(function(data, status, headers, config) {
                        deferred.reject({message: data, status: status});
                    });
            });
        });

        return deferred.promise;
    };

    return {
        createInstance : requestInstanceUpdate,
        deleteInstance : requestInstanceUpdate,
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
            return UtilityService.getCurrentTime() + " HTTP Status: "
                + UtilityService.getHttpStatusText(response.data.status)
                + "\n" + angular.toJson(response.data.entity, true)

        },
        checkValidStatus : checkValidStatus,
        getFormattedGetOrchestrationRequestsResponse : function(response) {
            UtilityService.checkUndefined(COMPONENT.ENTITY, response.data.entity);
            UtilityService.checkUndefined(COMPONENT.STATUS, response.data.status);
            checkValidStatus(response);

            var list = response.data.entity.requestList
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
                    message += addListEntry(FIELD.ID.TIMESTAMP, status.timestamp)
                        + ",\n";
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
            checkValidStatus(response);

            var message = "";
            if ( UtilityService.hasContents (response.data.entity.request) ) {
                var request = response.data.entity.request;
                message += addListEntry(FIELD.ID.REQUEST_ID, request.requestId) + ",\n";
                message += addListEntry(FIELD.ID.REQUEST_TYPE, request.requestType)
                    + ",\n";
                var status = request.requestStatus;
                if (status === undefined) {
                    message += addListEntry(FIELD.ID.REQUEST_STATUS, undefined) + "\n";
                } else {
                    message += addListEntry(FIELD.ID.TIMESTAMP, status.timestamp)
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
                    })
                    .catch(UtilityService.runHttpErrorHandler);
            });
        },
        showResponseContentError : function(error, showFunction) {
            switch (error.type) {
                case "undefinedObject":
                    showFunction(FIELD.ERROR.SYSTEM_FAILURE, error.message);
                    break;
                case "msoFailure":
                    showFunction(FIELD.ERROR.MSO, "")
                    break;
                default:
                    showFunction(FIELD.ERROR.SYSTEM_FAILURE);
            }
        },
        activateInstance: activateInstance
    }
};

appDS2.factory("MsoService", [ "$http", "$log", "PropertyService",
    "AaiService", "UtilityService", "COMPONENT", "FIELD", "$q", MsoService ]);
