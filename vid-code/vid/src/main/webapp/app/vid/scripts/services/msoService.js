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

var MsoService = function($http, $log, PropertyService, UtilityService) {

	var _this = this;

	/*
	 * Common function to handle both create and delete instance requests
	 */
	var requestInstanceUpdate = function(request, successCallbackFunction) {
		$log.debug("MsoService:requestInstanceUpdate: request:");
		$log.debug(request);
		$http.post(PropertyService.getMsoBaseUrl() + "/" + request.url, {
			requestDetails : request.requestDetails
		}, {
			timeout : PropertyService.getServerResponseTimeoutMsec()
		}).then(successCallbackFunction)["catch"]
				(UtilityService.runHttpErrorHandler);
	}

	var checkValidStatus = function(response) {
		if (response.data.status < 200 || response.data.status > 202) {
			throw {
				type : "msoFailure"
			}
		}
	}

	var addListEntry = function(name, value) {
		var entry = '"' + name + '": ';
		if (value === undefined) {
			return entry + "undefined";
		} else {
			return entry + '"' + value + '"';
		}
	}

	return {
		createInstance : requestInstanceUpdate,
		deleteInstance : requestInstanceUpdate,
		getOrchestrationRequest : function(requestId, successCallbackFunction) {
			$log.debug("MsoService:getOrchestrationRequest: requestId: "
					+ requestId);
			$http.get(
					PropertyService.getMsoBaseUrl() + "/mso_get_orch_req/"
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
					PropertyService.getMsoBaseUrl() + "/mso_get_orch_reqs/"
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
					+ "\n\n";
		},
		checkValidStatus : checkValidStatus,
		getFormattedGetOrchestrationRequestsResponse : function(response) {
			UtilityService.checkUndefined("entity", response.data.entity);
			UtilityService.checkUndefined("status", response.data.status);
			checkValidStatus(response);

			var list = response.data.entity.requestList
			UtilityService.checkUndefined("requestList", list);

			var message = "";

			for (var i = 0; i < list.length; i++) {
				var request = list[i].request;
				message += addListEntry("requestId", request.requestId) + ",\n";
				message += addListEntry("requestType", request.requestType)
						+ ",\n";
				var status = request.requestStatus;
				if (status === undefined) {
					message += addListEntry("requestStatus", undefined) + "\n";
				} else {
					message += addListEntry("timestamp", status.timestamp)
							+ ",\n";
					message += addListEntry("requestState", status.requestState)
							+ ",\n";
					message += addListEntry("statusMessage",
							status.statusMessage)
							+ ",\n";
					message += addListEntry("percentProgress",
							status.percentProgress)
							+ "\n";
				}
				if (i < (list.length - 1)) {
					message += "\n";
				}
			}
			return message;
		},
		showResponseContentError : function(error, showFunction) {
			switch (error.type) {
			case "undefinedObject":
				showFunction("System failure", error.message);
				break;
			case "msoFailure":
				showFunction("MSO failure", "see log below for details")
				break;
			default:
				showFunction("System failure");
			}
		}
	}
}

app.factory("MsoService", [ "$http", "$log", "PropertyService",
		"UtilityService", MsoService ]);
