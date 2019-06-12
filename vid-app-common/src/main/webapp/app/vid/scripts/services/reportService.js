"use strict";

var ReportService = function ( $http ) {

    return {
         getReportData: function() {
            return $http.get("rest/vid/error-report");
        },

        getReportDataWithId: function(instanceId) {
            return $http.get("rest/vid/error-report/" + instanceId);
        }
    }

};

appDS2.factory("ReportService", ["$http", ReportService]);