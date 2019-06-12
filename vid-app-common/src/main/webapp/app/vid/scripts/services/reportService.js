"use strict";

var ReportService = function ( $http, $log, PropertyService ) {

    return {

         getReportData: function() {
            return $http.get("/vid/error-report", {}, {
                timeout: PropertyService.getServerResponseTimeoutMsec()
            });
        }

    }

};

appDS2.factory("ReportService", ["$http", "$log", "PropertyService", ReportService]);