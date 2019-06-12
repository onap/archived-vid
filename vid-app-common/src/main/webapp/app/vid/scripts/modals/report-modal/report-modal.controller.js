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

    appDS2.controller("reportModalController", ["$uibModalInstance", "$scope", "ReportService", "requestId", "errorMsg", reportModalController]);

    function reportModalController($uibModalInstance, $scope, ReportService, requestId, errorMsg) {
        const vm = this;

        const init = function() {
            var today = new Date();
            var dd = String(today.getDate()).padStart(2, '0');
            var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
            var yyyy = today.getFullYear();

            vm.timestamp =  dd + '/' + mm + '/' + yyyy;

            vm.downloadEnable = false;

            if(requestId !== undefined && requestId != null) {
                ReportService.getReportDataWithId(requestId).then(
                    data => {
                        saveReportData(data);
                    }, data => {
                        printReportFail(data);
                    }
                    )
            } else {
                ReportService.getReportData().then(
                    data => {
                        saveReportData(data);
                    }, data => {
                        printReportFail(data);
                    }
                    );
            }

            const blob = new Blob([ vm.report ], { type : 'text/plain' });
            vm.download = (window.URL || window.webkitURL).createObjectURL( blob );

            console.log('window initialized');
        };

        const saveReportData = function(data) {
            vm.report = errorMsg + "\n\n Collected data from API:\n" + data.data.report ;

            const blob = new Blob([ vm.report ], { type : 'text/plain' });
            vm.download = (window.URL || window.webkitURL).createObjectURL( blob );
            vm.downloadEnable = true;
        };

        const  printReportFail = function(data) {
            vm.report = errorMsg + "\n\n API error:\n" +data.data.report ;
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
