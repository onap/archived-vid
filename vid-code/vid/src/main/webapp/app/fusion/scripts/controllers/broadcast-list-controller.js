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
app.controller('broadcastListController', function ($scope, modalService, $modal,AdminService){
	//$scope.broadcastMessage=${broadcastMessage};
	//$scope.broadcastSites=${broadcastSites};
	//console.log($scope.broadcastMessage);
	var messagesMap = {};
	AdminService.getBroadcastList().then(function(data){ 
			
			var j = data;
	  		$scope.data = JSON.parse(j.data);
	  		$scope.messagesList=(($scope.data.messagesList===null) ? [""]:$scope.data.messagesList);
	  		$scope.messageLocations=(($scope.data.messageLocations===null) ? [""]:$scope.data.messageLocations);
	  		console.log("messages: "+$scope.messagesList);
	  		console.log("location: "+$scope.messageLocations);
	  		$.each($scope.messageLocations, function(i, a){ 
	  			//var result = [];
	  		    angular.forEach($scope.messagesList, function(value, key) {
	  		    	if (key+'' === a.value+'') {
	  		    		//var objsJSON = JSON.parse(value);
	  		    		
	  		    		$.each(value, function(i, a){ 
	  		    			var startDateLong = a.startDate;
	  		    			var tempStartDate = new Date(startDateLong);
	  		    			tempStartDate = moment(tempStartDate).format('DD MMM YYYY hh:mmA zz');//03 Jun 2013 04:15PM EDT
	  		    			a.displayStartDate=tempStartDate.toString();
	  		    			
	  		    			var endDateLong = a.endDate;
	  		    			var tempEndDate = new Date(endDateLong);
	  		    			tempEndDate = moment(tempEndDate).format('DD MMM YYYY hh:mmA zz');//03 Jun 2013 04:15PM EDT
	  		    			a.displayEndDate=tempEndDate.toString();
	  		    		});
	  		    		a.messages = value;
	  		        }
	  		    }); 
	  		    console.log(a.messages);
	  		});	
	  		
	  		//$scope.resetMenu();
		
		},function(error){
			console.log("failed");
			reloadPageOnce();
	});
	
	
	$scope.editMessage = function(location) {
		
		editMessage(location.value, location.label);
	};
	
	$scope.toggleActive = function(broadcastMessage) {

		//alert('deleted'+role.name);
		var uuu = "broadcast_list/toggleActive";
		  var postData={broadcastMessage:broadcastMessage};
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			//window.location.reload();  
			 },
			 error : function(data){
				 console.log(data);
				 modalService.showFailure("Fail","Error while toggling: "+ data.responseText);
				 
			 }
	  	  });
	
	
	};
	
	$scope.remove = function(broadcastMessage) {

			//alert('deleted'+role.name);
			  var uuu = "broadcast_list/remove";
			  var postData={broadcastMessage:broadcastMessage};
		  	  $.ajax({
		  		 type : 'POST',
		  		 url : uuu,
		  		 dataType: 'json',
		  		 contentType: 'application/json',
		  		 data: JSON.stringify(postData),
		  		 success : function(data){
		  			window.location.reload();  
				 },
				 error : function(data){
					 console.log(data);
					 modalService.showFailure("Fail","Error while deleting: "+ data.responseText);
				 }
		  	  });
		
		
	};
	
});

function editMessage(messageLocationId, messageLocation, messageId) {
    window.location='admin#/broadcast/'+messageLocationId + '/' + messageLocation + ((messageId != null) ? '/' + messageId : '');
}
