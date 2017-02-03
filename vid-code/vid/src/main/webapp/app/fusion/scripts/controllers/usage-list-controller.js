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
app.controller('usageListController', function ($scope,$interval,$http,$modal,modalService,AdminService){
	
    AdminService.getUsageList().then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.users =$scope.data;
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});
	
	$scope.removeSession = function(sessionId) {
		modalService.popupConfirmWin("Confirm","You are about to expel this user from the application. All of their unsaved data will be lost. Do you want to continue?",
    			function(){
			          $http.get("usage_list/removeSession?deleteSessionId="+sessionId).success(function(response){$scope.users=response;});
    	})
		
	}
});
