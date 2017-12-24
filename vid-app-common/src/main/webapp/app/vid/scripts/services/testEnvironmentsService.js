(function () {
    'use strict';

    appDS2.service('TestEnvironmentsService', ['$q', '$http', '$log', 'COMPONENT', 'UtilityService', testEnvironmentsService]);

    function testEnvironmentsService($q, $http, $log, COMPONENT, UtilityService) {
        this.loadAAIestEnvironments = function(type) {
            var deferred = $q.defer();
            $http.get(COMPONENT.AAI_GET_TEST_ENVIRONMENTS + type)
                .success(function (response) {
                    if(response.httpCode == 200) {
                        deferred.resolve({operationalEnvironment: response.t.operationalEnvironment});
                    }
                    else {
                        deferred.reject({message: response.errorMessage, status: response.httpCode});
                    }
                })
                .error(function(data, status, headers, config) {
                    deferred.reject({message: data, status: status});
                });

            return deferred.promise;
        };

        this.createApplicationEnv = function(request) {
            var deferred = $q.defer();

            $http.post(COMPONENT.OPERATIONAL_ENVIRONMENT_CREATE, JSON.stringify(request.requestDetails))
                .success(function (response) {
                    deferred.resolve({data: response});
                }).error(function (data, status) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        }

        this.deactivateApplicationEnv = function(request) {
            var deferred = $q.defer();

            $http.post(COMPONENT.OPERATIONAL_ENVIRONMENT_DEACTIVATE + request.envId, JSON.stringify({}))
                .success(function (response) {
                    deferred.resolve({data: response});
                }).error(function (data, status) {
                    deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        };

        this.activateApplicationEnv = function(request) {
            var deferred = $q.defer();

            $http.post(COMPONENT.OPERATIONAL_ENVIRONMENT_ACTIVATE + request.envId, JSON.stringify({
                    "relatedInstanceId": request.relatedInstanceId
                    , "relatedInstanceName": request.relatedInstanceName
                    , "workloadContext": request.workloadContext
                    , "manifest": request.manifest
                }))
                .success(function (response) {
                    deferred.resolve({data: response});
                }).error(function (response, status) {
                    UtilityService.runHttpErrorHandler({data:response, status:status});
                });
            return deferred.promise;
        };

        this.getRequestStatus = function(requestId, successCallback) {
            $http.get(COMPONENT.OPERATIONAL_ENVIRONMENT_STATUS + requestId)
                .success(function(response) {
                    successCallback({data: response});
                } )
                .catch(UtilityService.runHttpErrorHandler);
        };
    }


})();

