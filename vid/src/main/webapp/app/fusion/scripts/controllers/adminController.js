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
	
	
	.when('/role_function_list', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/role_function_list.html',
		controller: 'roleFunctionListController'
	})
	
	.when('/admin_menu_edit', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/admin_menu_edit.html',
		controller: 'AdminMenuEditController'
	})
	.when('/role/:roleId', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/role.html',
		controller: 'roleController'
	})
	.when('/jcs_admin', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/jcs_admin.html',
		controller: 'cacheAdminController'
	})
	.when('/broadcast_list', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/broadcast_list.html',
		controller: 'broadcastListController'
	})
	.when('/broadcast/:messageLocationId/:messageLocation/:messageId', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/broadcast.html',
		controller: 'broadcastController'
	})
	.when('/broadcast/:messageLocationId/:messageLocation', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/broadcast.html',
		controller: 'broadcastController'
	})
	.when('/collaborate_list', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/collaborate_list.html',
		controller: 'collaborateListController'
	})
	.when('/usage_list', {
		templateUrl: 'app/fusion/scripts/view-models/profile-page/usage_list.html',
		controller: 'usageListController'
	})
	.otherwise({
		templateUrl: 'app/fusion/scripts/view-models/profile-page/role_list.html',
		controller : "roleListController"
	});
});
