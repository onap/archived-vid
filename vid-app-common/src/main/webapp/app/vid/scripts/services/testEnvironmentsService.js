(function () {
    'use strict';

    appDS2.service('TestEnvironmentsService', ['$q', '$http', '$log', 'COMPONENT', testEnvironmentsService]);

    function testEnvironmentsService($q, $http, $log, COMPONENT) {
        this.loadMSOTestEnvironments = function(type) {
            var deferred = $q.defer();
            var params = {
                operationalEnvironmentType: type
            };
            $http.get(COMPONENT.AAI_GET_TEST_ENVIRONMENTS, params)
                .success(function (response) {
                    deferred.resolve({operationalEnvironment: response.t.operationalEnvironment});
                })
                .error(function(data, status, headers, config) {
                    deferred.reject({message: data, status: status});
                });

            return deferred.promise;
        };

        this.getEnvironmentsTypesList = function () {
            return [
                "VNF"
            ];
        };

        this.getWorkloadContextList = function () {
            return [
                "one",
                "two",
                "three"
            ];
        };

        this.deactivateApplicationEnv = function(request) {
            var deferred = $q.defer();

            $http.post(COMPONENT.OPERATIONAL_ENVIRONMENT_DEACTIVATE + request.envId, JSON.stringify({}))
                .success(function (response) {
                    deferred.resolve(response);
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
                    deferred.resolve(response);
                }).error(function (data, status) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
        };

        this.getRequestStatus = function(requestId, successCallback) {
            $http.get(COMPONENT.OPERATIONAL_ENVIRONMENT_STATUS + requestId)
                .success(successCallback)
                .error(UtilityService.runHttpErrorHandler);
        };
    }


})();

