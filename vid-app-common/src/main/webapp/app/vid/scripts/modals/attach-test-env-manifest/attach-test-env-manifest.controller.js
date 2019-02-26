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

    appDS2.controller("attachTestEnvManifestController", ["$uibModalInstance", "$uibModal", "$log", "$scope",
        attachTestEnvManifestController]);

    function attachTestEnvManifestController($uibModalInstance, $uibModal, $log, $scope) {
        var vm = this;

        var init = function () {
            vm.manifest = "";
            vm.error="";
        };

        vm.close = function () {
            $uibModalInstance.close();
        };

        vm.submit = function () {
            $uibModalInstance.close(vm.manifest);
        };

        vm.isSubmitDisabled = function () {
            return !(vm.manifest);
        };



        /*
        Must be $scope because we bind to the onchange of the html (cannot attached to vm variable).
        We use scope because angular doesn't support ng-change on input file
        https://github.com/angular/angular.js/issues/1375
        https://stackoverflow.com/questions/17922557/angularjs-how-to-check-for-changes-in-file-input-fields
         */
        $scope.selectAttachmentManifest = function (fileInput) {
            if (fileInput && fileInput.id) {
                vm.manifest = "";
                vm.error="";
                var file = fileInput.files[0];
                vm.filename=file.name;
                var fileReader = new FileReader();
                fileReader.onload = function (load) {
                    try {
                        var lines = load.target.result;
                        vm.manifest = JSON.parse(lines);
                    } catch (error) {
                        $log.error(error);
                        vm.error = "file: " + vm.filename + " is not a valid JSON"
                    }
                    $scope.$apply();
                };
                fileReader.readAsText(file);
            }
        };

        init();
    }
})();
