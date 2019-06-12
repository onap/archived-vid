"use strict";

var ReportService = function ( $http ) {

    return {
         getReportData: function() {
            return $http.get("error-report");
        },

        getReportDataWithId: function(requestId, instanceId) {
            return $http.get("error-report/" + requestId + "/" + instanceId);
        }
    }

};

appDS2.factory("ReportService", ["$http", ReportService]);