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
app.controller("collaborateListController", function ($scope,$http,modalService, $modal,AdminService) { 
	// Table Data
    AdminService.getCollaborateList().then(function(data){
		
		var j = data;
  		$scope.tableData = JSON.parse(j.data);
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});
	
	$scope.viewPerPage = 20;
    $scope.scrollViewsPerPage = 2;
    $scope.currentPage = 1;
    $scope.totalPage;
    $scope.searchCategory = "";
    $scope.searchString = "";
 /*    modalService.showSuccess('','Modal Sample') ; */
	for(x in $scope.tableData){
		if($scope.tableData[x].active_yn=='Y')
			$scope.tableData[x].active_yn=true;
		else
			$scope.tableData[x].active_yn=false;
	}
    $scope.openCollaboration = function(chatId){
    	openInNewTab('collaboration?chat_id=' + chatId);
    }
   
    $scope.toggleProfileActive = function(profileId) {
    	modalService.popupConfirmWin("Confirm","You are about to change user's active status. Do you want to continue?",
    			function(){
    		        $http.get("profile/toggleProfileActive?profile_id="+profileId).success(function(){});
					
    	})
    };
   
});

function openInNewTab(url) {
	  var win = window.open(url, '_blank');
	  win.focus();
};
