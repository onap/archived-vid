/*-
 * ================================================================================
 * eCOMP Portal SDK
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
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
 * ================================================================================
 */
function isRealValue(obj){
	return obj && obj !== "null" && obj!== "undefined";
}
app.factory('LeftMenuService', function ($http,$log, $q) {
	return {
		getLeftMenu: function() {
			return $http.get('get_menu')
			.then(function(response) {
					if (typeof response.data === 'object') {
						return response.data;
					} else {
						return $q.reject(response.data);
					}
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		},
		getAppName: function() {
			return $http.get('get_app_name')
			.then(function(response) {
					if (typeof response.data === 'object') {
						return response.data;
					} else {
						return $q.reject(response.data);
					}
			}, function(response) {
				// something went wrong
				return $q.reject(response.data);
			});
		}
		
	};
});

