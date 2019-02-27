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

(function () {
    'use strict';

    appDS2.service('changeManagementService', ['$http', '$q', 'COMPONENT', 'VIDCONFIGURATION', changeManagementService]);

    function changeManagementService($http, $q, COMPONENT, VIDCONFIGURATION) {
        this.getWorkflows = function (vnfs) {
            var requestVnfs = _.map(vnfs, function (vnf) {
                return {
                    UUID: vnf["modelVersionId"],
                    invariantUUID: vnf["invariant-id"]
                };
            });
            var requestDetails = {vnfsDetails: requestVnfs};
            return $http.post(COMPONENT.GET_WORKFLOW, requestDetails)
            .success(function (response) {
                return {data: response};
            })
                .catch(function () {
                    return {data: []};
                });
        };

        this.getSOWorkflows = function (vnfNames) {
            return $http.get(COMPONENT.GET_SO_WORKFLOWS, {params: {vnfName: vnfNames}})
            .success(function (response) {
                return {data: response};
            }).catch(function () {
                return {data: []};
            });
        };

        this.getSOWorkflowParameter = function (workflowID){
          return $http.get(COMPONENT.GET_SO_WORKFLOW_PARAMETER.replace('@workflowID', workflowID))
          .success(function (response) {
            return {data: response.parameterDefinitions}
          });
        };

      this.getLocalWorkflowParameter = function (workflowName){
        return $http.get(COMPONENT.GET_LOCAL_WORKFLOW_PARAMETER.replace('@workflowName', encodeURIComponent(workflowName)))
        .success(function (response) {
          return {data: response.parameterDefinitions}
        });
      };

        this.getMSOChangeManagements = function() {
            var deferred = $q.defer();

            $http.get(COMPONENT.GET_MSO_WORKFLOWS)
            .success(function (response) {
                deferred.resolve({data: response});
            })
            .error(function(data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        };

        this.getAllSDCServices = function () {
            var deferred = $q.defer();

            $http.get(COMPONENT.SERVICES_DIST_STATUS_PATH + VIDCONFIGURATION.ASDC_MODEL_STATUS)
            .success(function (response) {
                deferred.resolve({data: response});
            })
            .error(function(data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        };

        this.getSDCService = function(uuid) {
            var deferred = $q.defer();

            $http.get(COMPONENT.SERVICES_PATH + uuid)
            .success(function (response) {
                deferred.resolve({data: response});
            })
            .error(function(data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        };

        this.getSchedulerChangeManagements = function(){
            var deferred = $q.defer();

            $http.get(COMPONENT.GET_SCHEDULER_CHANGE_MANAGEMENTS)
                .success(function (response) {
                    deferred.resolve({data: response});
                })
                .error(function(data, status, headers, config) {
                    deferred.reject({message: data, status: status});
                });

            return deferred.promise;
        };
		
		this.postChangeManagementNow = function (requestData, vnfName) {
			var url = COMPONENT.CHANGE_MANAGEMENT_OPERATION_NO_SCHEDULER.replace('@vnfName', vnfName);
            return $http.post(url, requestData)
            .success(function (response) {
                return {data: response};
            })
                .catch(function (err) {
                    return {data: []};
                });
        };

        this.postChangeManagementScaleOutNow = function (requestData, serviceInstanceId, vnfId) {
            var url = "mso/mso_create_vfmodule_instance/"+serviceInstanceId+"/vnfs/"+vnfId;
            return $http.post(url, requestData)
                .success(function (response) {
                    return {data: response};
                })
                .catch(function (err) {
                    return {data: []};
                });
        };
    }
})();
