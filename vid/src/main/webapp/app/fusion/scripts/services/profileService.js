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

app.factory('ProfileService', function ($http, $q) {
	return {
		getProfile: function() {
			return $http.get('get_user')
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
		
		getProfilePagination: function(pageNum,viewPerPage) {
			return $http.get('get_user_pagination?pageNum=' + pageNum + '&viewPerPage=' + viewPerPage)
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
		
		getPostProfile: function() {
			return $http.get('post_search_sample')
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

		getProfileDetail: function(profileId) {
			return $http.get('get_profile?profile_id='+profileId)
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
		
		getSelfProfileDetail: function() {
			return $http.get('get_self_profile')
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
