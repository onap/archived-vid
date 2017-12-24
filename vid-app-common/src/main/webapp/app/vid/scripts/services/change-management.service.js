(function () {
    'use strict';

    appDS2.service('changeManagementService', ['$http', '$q', 'COMPONENT', 'VIDCONFIGURATION', changeManagementService]);

    function changeManagementService($http, $q, COMPONENT, VIDCONFIGURATION) {
        this.getWorkflows = function (vnfs) {
            var requestVnfs = _.map(vnfs, function (vnf) {
                return {
                    UUID: vnf.id,
                    invariantUUID: vnf["invariant-id"]
                };
            });
            var requestDetails = {vnfsDetails: requestVnfs};
            return $http.post(COMPONENT.GET_WORKFLOW, requestDetails)
            .success(function (response) {
                return {data: response};
            })
                .catch(function (err) {
                    return {data: []};
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
    }
})();
