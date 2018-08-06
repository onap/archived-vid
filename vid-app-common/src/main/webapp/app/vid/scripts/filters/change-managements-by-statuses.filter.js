(function () {
    'use strict';
    appDS2.filter('changeManagementsByStatuses', [changeManagementsByStatuses]);

    function changeManagementsByStatuses () {
        return function(changeManagements, metadata) {
            var result = [];
            if(changeManagements && metadata && metadata.statuses) {
                angular.forEach(changeManagements, function(changeManagement) {
                    var found = metadata.statuses
                        .map(function(c) { return c.toLowerCase(); })
                        .indexOf(changeManagement.requestStatus.requestState.toLowerCase()) !== -1;

                    if (metadata.notContains && !found) {
                        result.push(changeManagement);
                    }
                    if (! metadata.notContains && found) {
                        result.push(changeManagement);
                    }
                });
            }

            return result;
        }
    }
})();
