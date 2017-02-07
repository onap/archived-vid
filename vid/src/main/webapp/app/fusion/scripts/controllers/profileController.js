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
app.config(function($routeProvider) {
	$routeProvider
	.when('/profile/:profileId', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/profile_detail.html',
		controller: 'profileController'
	})
	.when('/post_search', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/post_search.html',
		controller: 'postSearchCtrl'
	})
	.when('/self_profile', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/self_profile.html',
		controller: 'selfProfileController'
	})
	.otherwise({
		templateUrl: 'app/fusion/scripts/view-models/profile-page/profile_search.html',
		controller : "profileSearchCtrl"
	});
});
