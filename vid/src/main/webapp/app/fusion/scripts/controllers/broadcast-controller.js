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
app.controller('broadcastController', function ($scope, modalService, $modal,AdminService,$routeParams){
	//$scope.broadcastMessage=${broadcastMessage};
	//$scope.broadcastSites=${broadcastSites};
	//console.log($scope.broadcastMessage);	
	$scope.broadcastMessage=[];
	$scope.broadcastSites=[];
	AdminService.getBroadcast($routeParams.messageLocationId, $routeParams.messageLocation, $routeParams.messageId).then(function(data){
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.broadcastMessage=JSON.parse($scope.data.broadcastMessage);
  		$scope.broadcastSites=JSON.parse($scope.data.broadcastSites);
  		console.log($scope.broadcastMessage);
  		console.log($scope.broadcastMessage.id);
  		console.log($scope.broadcastSites);
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
    });
	
	$scope.save = function() {
		  var uuu = "broadcast/save";
		  var postData={broadcastMessage: $scope.broadcastMessage};
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			window.location.href = "admin#/broadcast_list";
			 },
			 error : function(data){
				 modalService.showFailure("Fail","Error while saving.");
			 }
	  	  });
	};
	
	$scope.close = function() {
	window.location.href = "admin#/broadcast_list";
};	
	
});

$(function() {
    $( "#startDatepicker" ).datepicker();
    $( "#endDatepicker" ).datepicker();
    
    $( "#startDatepicker" ).change(function() {
    	var tempStartDate = moment($( "#startDatepicker" ).val()).format('YYYY-MM-DD hh:mm:ss.S');
    	$( "#startDateHidden" ).val(tempStartDate.toString());
    	  //alert( $( "#startDateHidden" ).val() );
    });
    $( "#endDatepicker" ).change(function() {
    	var tempEndDate = moment($( "#endDatepicker" ).val()).format('YYYY-MM-DD hh:mm:ss.S');
    	$( "#endDateHidden" ).val(tempEndDate.toString());
    	  //alert( $( "#endDateHidden" ).val() );
  	});
});
