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
    appDS2.filter('changeManagementsByStatuses', [changeManagementsByStatuses]);

    function changeManagementsByStatuses () {
        return function(changeManagements, metadata) {
            var result = [];
            if(changeManagements && metadata && metadata.statuses) {
                angular.forEach(changeManagements, function(changeManagement) {
                    var found = changeManagement.requestStatus && metadata.statuses
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
