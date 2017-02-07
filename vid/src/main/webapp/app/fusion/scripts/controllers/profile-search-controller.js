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
app.controller('profileSearchCtrl', function($scope, $http,ProfileService,modalService){

    $scope.showInput = true;
    $scope.totalPages1 = 5;
    $scope.viewPerPage1 = 8;
    $scope.currentPage1 = 1;

	$scope.$watch('viewPerPage1', function(val) {
		ProfileService.getProfilePagination($scope.currentPage1, val).then(function(data){
    		var j = data;
      		$scope.data = JSON.parse(j.data);
      		$scope.tableData =JSON.parse($scope.data.profileList);
      		$scope.totalPages1 =JSON.parse($scope.data.totalPage);
      		for(x in $scope.tableData){
				if($scope.tableData[x].active_yn=='Y')
					$scope.tableData[x].active_yn=true;
				else
					$scope.tableData[x].active_yn=false;
			}
    	},function(error){
    		console.log("failed");
    		reloadPageOnce();
    	});
		
	});
	    
	$scope.customHandler = function(num) {
	    	$scope.currentPage1 = num;	    	
	    	ProfileService.getProfilePagination($scope.currentPage1,$scope.viewPerPage1).then(function(data){
	    		var j = data;
	      		$scope.data = JSON.parse(j.data);
	      		$scope.tableData =JSON.parse($scope.data.profileList);
	      		$scope.totalPages1 =JSON.parse($scope.data.totalPage);
	      		for(x in $scope.tableData){
					if($scope.tableData[x].active_yn=='Y')
						$scope.tableData[x].active_yn=true;
					else
						$scope.tableData[x].active_yn=false;
				}
	    	},function(error){
	    		console.log("failed");
	    		reloadPageOnce();
	    	});

	    };

	$scope.editRow = function(profileId){
        window.location = 'userProfile#/profile/' + profileId;
    };
   
	$scope.toggleProfileActive = function(rowData) {
    	modalService.popupConfirmWinWithCancel("Confirm","You are about to change user's active status. Do you want to continue?",
    			function(){ 
    		        $http.get("profile/toggleProfileActive?profile_id="+rowData.id).success(function(){});
    	},
    	function(){
    		rowData.active=!rowData.active;
    	})
    };

});
