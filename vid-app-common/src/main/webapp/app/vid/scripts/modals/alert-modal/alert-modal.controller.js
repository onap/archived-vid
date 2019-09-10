/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

    appDS2.controller("alertModalController", ["$uibModalInstance", "jobInfo",
        "$log", alertModalController]);

    function alertModalController($uibModalInstance, jobInfo,  $log) {
        var vm = this;
        var init = function() {
            if (jobInfo) {
                vm.content = jobInfo.message;
                vm.mode = jobInfo.status;
                if (vm.mode == "failed") {
                    vm.Header = "Failed";
                } else if (vm.mode == "confirm") {
                    vm.Header = "Confirm";
                } else {
                    vm.Header = "Success";
                }
            }
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
