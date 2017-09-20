(function () {
    'use strict';
    appDS2.filter('changeManagementsByStatuses', [changeManagementsByStatuses]);

    function changeManagementsByStatuses () {
        return function(changeManagements, metadata) {
            var result = [];
            if(changeManagements && metadata && metadata.statuses) {
                angular.forEach(changeManagements, function(changeManagement) {
                    angular.forEach(metadata.statuses, function(status) {
                        if(changeManagement.requestStatus.requestState === status) {
                            result.push(changeManagement);
                            return;
                        }
                    });
                });
            }

            return result;
        }
    }
})();
