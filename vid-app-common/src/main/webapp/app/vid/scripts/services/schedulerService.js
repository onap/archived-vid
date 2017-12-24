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

var SchedulerService = function($http, $log, PropertyService, UtilityService, COMPONENT, FIELD) {
    return {
    
		getStatusSchedulerId : function(schedulerInfo,successCallbackFunction) {
			$log.debug("SchedulerService:getSchedulerStatusAndSchedulerId");
			var url =   COMPONENT.POST_CREATE_NEW_VNF_CHANGE+COMPONENT.ASSIGN + Math.random();

			$http.post(url,	schedulerInfo,
					{
				timeout : PropertyService
				.getServerResponseTimeoutMsec()
					}).then(function(response) {
						if (response.data) {
							successCallbackFunction(response);
						} else {
							successCallbackFunction([]);
						}
					})["catch"]
			(UtilityService.runHttpErrorHandler);
		},

		getTimeSotsForSchedulerId:function(schedulerID,successCallbackFunction){
			$log.debug("SchedulerService:getTimeSlotsForSchedulerID");
			var url =   COMPONENT.GET_TIME_SLOTS+COMPONENT.FORWARD_SLASH +schedulerID+COMPONENT.ASSIGN;

			$http.get(url,
					{
				timeout : PropertyService
				.getServerResponseTimeoutMsec()
					}).then(function(response) {
						if (response.data) {
							successCallbackFunction(response);
						} else {
							successCallbackFunction([]);
						}
					})["catch"]

			(UtilityService.runHttpErrorHandler);
		},
		getSubmitForapprovedTimeslots: function(ApprovedTimeSlotsObj,successCallbackFunction) {
			$log.debug("SchedulerService:getSchedulerStatusAndSchedulerId");
			var url =   COMPONENT.SUBMIT_VNF_CHANGE_TIMESLOTS+COMPONENT.ASSIGN + Math.random();

			$http.post(url,	ApprovedTimeSlotsObj,
					{
				timeout : PropertyService
				.getServerResponseTimeoutMsec()
					}).then(function(response) {
						if (response.data) {
							successCallbackFunction(response);
						} else {
							successCallbackFunction([]);
						}
					})["catch"]
			(UtilityService.runHttpErrorHandler);
		},
		getPolicyInfo:function(policyName,successCallbackFunction){
			$log.debug("SchedulerService:getPolicyInfo");
			var url =   COMPONENT.GET_POLICY +COMPONENT.ASSIGN + Math.random();

			$http.post(url,	policyName,
					{
				timeout : PropertyService
				.getServerResponseTimeoutMsec()
					}).then(function(response) {
						if (response.data) {
							successCallbackFunction(response);
						} else {
							successCallbackFunction([]);
						}
					})["catch"]
			(UtilityService.runHttpErrorHandler);
		},

		cancelScheduleRequest: function(schedulerID, successCallbackFunction, errorCallbackFunction) {
            $log.debug("SchedulerService:cancelPendingBySchedulerId");
            var url =   COMPONENT.CANCEL_SCHEDULE_REQUEST + COMPONENT.FORWARD_SLASH + schedulerID + COMPONENT.ASSIGN;

            $http.delete(url,
			{
				timeout : PropertyService
					.getServerResponseTimeoutMsec()
			}).then(function(response) {
                if (response.data) {
                    successCallbackFunction(response);
                } else {
                    successCallbackFunction([]);
                }
            }).catch(function(error) {
            	errorCallbackFunction(error);
			});
		}
    }
}

appDS2.factory("SchedulerService", ["$http", "$log", "PropertyService",
    "UtilityService", "COMPONENT", "FIELD", SchedulerService]);
