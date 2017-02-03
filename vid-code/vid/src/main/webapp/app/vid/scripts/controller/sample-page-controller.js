/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

app.controller('samplePageController', function($scope, $http,ProfileService,modalService){
	$scope.tableData=[];
	$scope.viewPerPage=20;
	$scope.scrollViewPerPage=2;
	$scope.currentPage=1;
	$scope.totalPage;
	$scope.searchCategory;
	$scope.searchString="";
	$scope.currentPageNum=1;
	ProfileService.getProfilePagination(1,$scope.viewPerPage).then(function(data){
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.tableData =JSON.parse($scope.data.profileList);
  		$scope.totalPages =JSON.parse($scope.data.totalPage);
		for(x in $scope.tableData){
			if($scope.tableData[x].active_yn=='Y')
				$scope.tableData[x].active_yn=true;
			else
				$scope.tableData[x].active_yn=false;
		}
		//$scope.resetMenu();
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});
	
	$scope.$watch('currentPageNum', function(val) {
		
		ProfileService.getProfilePagination(val,$scope.viewPerPage).then(function(data){
			var j = data;
	  		$scope.data = JSON.parse(j.data);
	  		$scope.tableData =JSON.parse($scope.data.profileList);
	  		$scope.totalPages =JSON.parse($scope.data.totalPage);
			for(x in $scope.tableData){
				if($scope.tableData[x].active_yn=='Y')
					$scope.tableData[x].active_yn=true;
				else
					$scope.tableData[x].active_yn=false;
			}
			//$scope.resetMenu();
		},function(error){
			console.log("failed");
		});
	
    });
	
	$scope.editRow = function(profileId){
        window.location = 'userProfile#/profile/' + profileId;
    }
   
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
