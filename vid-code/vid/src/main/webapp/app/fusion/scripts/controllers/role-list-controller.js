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
app.controller('roleListController', function ($scope,AdminService,modalService){
	
	AdminService.getRoles().then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.availableRoles =JSON.parse($scope.data.availableRoles);
  	
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});
	
	//console.log($scope.availableRoles);
		$scope.toggleRole = function(selected,availableRole) {
				//alert('toggleRole: '+selected);
				var toggleType = null;
				if(selected) {
					toggleType = "activate";
				} else {
					toggleType = "inactivate";
				}
				
				modalService.popupConfirmWinWithCancel("Confirm","You are about to "+toggleType+" the test role "+availableRole.name+". Do you want to continue?",
		    			function(){
			                    var uuu = "role_list/toggleRole";
								
								var postData={role:availableRole};
							  	  $.ajax({
							  		 type : 'POST',
							  		 url : uuu,
							  		 dataType: 'json',
							  		 contentType: 'application/json',
							  		 data: JSON.stringify(postData),
							  		 success : function(data){
							  			console.log(data);
							  			$scope.$apply(function(){$scope.availableRoles=data.availableRoles;}); 
							  			console.log($scope.availableRoles);
									 },
									 error : function(data){
										 console.log(data);
										 modalService.showFailure("Fail","Error while saving.");
									 }
							  	  });
					
		    	},
		    	function(){
		    		availableRole.active=!availableRole.active;
		    	})
				
				  
		};

		$scope.removeRole = function(role) {
			
			modalService.popupConfirmWin("Confirm","You are about to delete the role "+role.name+". Do you want to continue?",
	    			function(){
							var uuu = "role_list/removeRole";
							  var postData={role:role};
						  	  $.ajax({
						  		 type : 'POST',
						  		 url : uuu,
						  		 dataType: 'json',
						  		 contentType: 'application/json',
						  		 data: JSON.stringify(postData),
						  		 success : function(data){
						  			$scope.$apply(function(){$scope.availableRoles=data.availableRoles;});  
								 },
								 error : function(data){
									 console.log(data);
									 modalService.showFailure("Fail","Error while deleting: "+ data.responseText);
								 }
						  	  });
				
	    	})
	    	
			
		};
		

});
