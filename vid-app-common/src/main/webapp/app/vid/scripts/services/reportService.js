"use strict";

var ReportService = function ( $http ) {

    return {

        getReportData: function(requestInfo) {
            return $http.post("error-report",requestInfo);
        },

        getReportTimeStamp: function () {
            const today = new Date();
            const se = String(today.getSeconds()).padStart(2, '0');
            const mi = String(today.getMinutes()).padStart(2, '0');
            const hr = String(today.getHours()).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const yyyy = today.getFullYear();

            return hr + '-' + mi + '-' + se + "_" + dd + '-' + mm + '-' + yyyy;
        }

    }

};

appDS2.factory("ReportService", ["$http", ReportService]);