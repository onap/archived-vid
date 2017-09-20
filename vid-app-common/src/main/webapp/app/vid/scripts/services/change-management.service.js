(function () {
    'use strict';

    appDS2.service('changeManagementService', ['$http', '$q', 'COMPONENT', 'VIDCONFIGURATION', changeManagementService]);

    function changeManagementService($http, $q, COMPONENT, VIDCONFIGURATION) {
        this.getWorkflows = function (vnfs) {
            var deferred = $q.defer();

            $http.get(COMPONENT.GET_WORKFLOW.replace("@vnfs", vnfs))
            .success(function (response) {
                deferred.resolve({data: response});
            }).error(function (data, status, headers, config) {
                deferred.reject({message: data, status: status});
            });

            return deferred.promise;
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
    }
})();
