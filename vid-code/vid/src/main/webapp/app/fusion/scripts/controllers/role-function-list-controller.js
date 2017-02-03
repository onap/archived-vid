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
app.controller('roleFunctionListController', function ($scope, AdminService, modalService, $modal){
	$( "#dialog" ).hide();
	
    AdminService.getRoleFunctionList().then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.availableRoleFunctions =JSON.parse($scope.data.availableRoleFunctions);
  		
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});
	
	$scope.editRoleFunction = null;
	var dialog = null;
	$scope.editRoleFunctionPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = availableRoleFunction;
		$( "#dialog" ).dialog({
		      modal: true
	    });
	};
	
	$scope.editRoleFunctionModalPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = availableRoleFunction;
		$scope.availableRoleFunctionsTemp=$scope.availableRoleFunctions;
		$scope.availableRoleFunctions={};
		var modalInstance = $modal.open({
		    templateUrl: 'edit_role_function_popup.html',
		    controller: 'rolefunctionpopupController',
		    resolve: {
		    	message: function () {
		    		var message = {
		    				availableRoleFunction:  $scope.editRoleFunction,
		    				availableRoleFunctions: $scope.availableRoleFunctionsTemp
                           	};
		          return message;
		        }					
		      }
		  }); 
		modalInstance.result.then(function(response){
            console.log('response', response);
            if(response!=null)
            	$scope.availableRoleFunctions=response.availableRoleFunctions;
            else
            	$scope.availableRoleFunctions=$scope.availableRoleFunctionsTemp;
        });
	};
	
	$scope.addNewRoleFunctionModalPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = null;
		$scope.availableRoleFunctionsTemp=$scope.availableRoleFunctions;
		$scope.availableRoleFunctions={};
		var modalInstance = $modal.open({
		    templateUrl: 'edit_role_function_popup.html',
		    controller: 'rolefunctionpopupController',
		    resolve: {
		    	message: function () {
		    		var message = {
		    				availableRoleFunction: $scope.editRoleFunction,
		    				availableRoleFunctions: $scope.availableRoleFunctionsTemp
                           	};
		          return message;
		        }					
		      }
		  }); 
		modalInstance.result.then(function(response){
            console.log('response', response);
            if(response!=null)
            	$scope.availableRoleFunctions=response.availableRoleFunctions;
            else
            	$scope.availableRoleFunctions=$scope.availableRoleFunctionsTemp;
        });
	};
	
	$scope.addNewRoleFunctionPopup = function() {
		$scope.editRoleFunction = null;
		$( "#dialog" ).dialog({
		      modal: true
	    });
	};
	
	$scope.saveRoleFunction = function(availableRoleFunction) {
		  var uuu = "role_function_list/saveRoleFunction.htm";
		  var postData={availableRoleFunction: availableRoleFunction};
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			$scope.$apply(function(){
	  				$scope.availableRoleFunctions=[];$scope.$apply();
	  				$scope.availableRoleFunctions=data.availableRoleFunctions;});  
	  			//alert("Update Successful.") ;
	  			console.log($scope.availableRoleFunctions);
  				
	  			$scope.editRoleFunction = null;
	  			$( "#dialog" ).dialog("close");
			 },
			 error : function(data){
				 modalService.showFailure("Fail","Error while saving.");
			 }
	  	  });
		};
	
		
		$scope.removeRole = function(availableRoleFunction) {
			modalService.popupConfirmWin("Confirm","You are about to delete the role function "+availableRoleFunction.name+". Do you want to continue?",
	    			function(){
							$scope.availableRoleFunctionsTemp=$scope.availableRoleFunctions;
							$scope.availableRoleFunctions={};
						  var uuu = "role_function_list/removeRoleFunction.htm";
						  var postData={availableRoleFunction: availableRoleFunction};
					  	  $.ajax({
					  		 type : 'POST',
					  		 url : uuu,
					  		 dataType: 'json',
					  		 contentType: 'application/json',
					  		 data: JSON.stringify(postData),
					  		 success : function(data){
					  			$scope.$apply(function(){$scope.availableRoleFunctions=data.availableRoleFunctions;});  
							 },
							 error : function(data){
								 console.log(data);
								 $scope.$apply(function(){$scope.availableRoleFunctions=$scope.availableRoleFunctionsTemp;});
								 modalService.showFailure("Fail","Error while deleting: "+ data.responseText);
							 }
					  	  });
						
	    	})
			
		};

});
