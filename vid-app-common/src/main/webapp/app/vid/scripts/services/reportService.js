"use strict";

var ReportService = function ( $http ) {

    return {
         getReportData: function() {
            return $http.get("rest/models/errorReport");
        }
    }

};

appDS2.factory("ReportService", ["$http", ReportService]);