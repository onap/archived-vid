/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 NOKIA Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

(function () {
    'use strict';

    appDS2.controller("reportModalController", ["$uibModalInstance", "$scope", "$window", "DataService", "ReportService", "errorMsg", reportModalController]);

    function reportModalController($uibModalInstance, $scope, $window, DataService, ReportService, errorMsg) {
        const vm = this;

        const init = function() {
            vm.timestamp =  ReportService.getReportTimeStamp();
            vm.downloadEnable = false;
            $scope.isSpinnerVisible = true;

            ReportService.getReportData({}).then(
                response => {
                    $scope.isSpinnerVisible = false;
                    vm.saveReportData(response);
                }, response => {
                    $scope.isSpinnerVisible = false;
                    vm.printReportFail(response);
                });
        };

        vm.saveReportData = function(response) {
            vm.report =
                "Selected test API: \n" + DataService.getMsoRequestParametersTestApi()
                + "\n\n Data from GUI:\n" + errorMsg
                + "\n\n Collected data from API:\n" + JSON.stringify(response.data,  null, "\t") ;


            const blob = new Blob([ vm.report ], { type : 'text/plain' });
            vm.download = ($window.URL || $window.webkitURL).createObjectURL( blob );
            vm.downloadEnable = true;
        };

        vm.printReportFail = function(response) {
            vm.downloadEnable = false;
            vm.report = errorMsg + "\n\n API error:\n" + JSON.stringify(response.data,  null, "\t") ;
        };

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.ok = function () {
            $uibModalInstance.close(true);
        };

        init();
    }
})();
