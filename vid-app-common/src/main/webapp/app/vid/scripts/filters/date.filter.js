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
    appDS2.filter('vidDate', ['$filter', vidDate]);

    function vidDate($filter) {
        var suffixes = ["th", "st", "nd", "rd"];
        return function(input, format) {
            if(input) {
                var dtfilter = $filter('date')(input, format);
                var day = parseInt($filter('date')(input, 'dd'));
                var relevantDigits = (day < 30) ? day % 20 : day % 30;
                var suffix = (relevantDigits <= 3) ? suffixes[relevantDigits] : suffixes[0];
                return dtfilter.replace('oo', suffix);
            }
            return input;
        };
    }
})();
