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
app.controller('AdminMenuEditController', function ($scope, AdminService, modalService, $modal, $route){
	$( "#dialog" ).hide();
	
/*    AdminService.getRoleFunctionList().then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.availableRoleFunctions =JSON.parse($scope.data.availableRoleFunctions);
  		
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});*/
	$scope.init = function () {
		$scope.numberOfRecordstoShow=20;
		 AdminService.getFnMenuItems().then(function(data){ 
				var j = data;
		  		$scope.data =JSON.parse(j.data);
		  		$scope.fnMenuItems =($scope.data.fnMenuItems);  	
		  	
			},function(error){
				console.log("failed");
				//reloadPageOnce();
			}); 
	}
	$scope.init();
    $scope.mapActiveStatus = function(status){
    	if(status)
    		status = "Y";
    		else
    			status = "N";
    	return status;
    	
    };
    

    $scope.addNewFnMenuItemModalPopup = function(availableFnMenuItem) {
		$scope.editFnMenuItem = null;
		var modalInstance = $modal.open({
		    templateUrl: 'fn_menu_add_popup.html',
		    controller: 'fn_menu_popupController',
		    resolve: {
		    	message: function () {
		    		var message = {
		    				availableFnMenuItem: $scope.editFnMenuItem
                           	};
		          return message;
		        }					
		      }
		  }); 
		modalInstance.result.then(function(response){
            console.log('response', response);
            $scope.availableFnMenuItems=response.availableFnMenuItems; 
            $route.reload();
        });
	};
	
	$scope.removeMenuItem = function(fnMenuItem) { 
		modalService.popupConfirmWin("Confirm","You are about to delete the menu item "+fnMenuItem.label+". Do you want to continue?",
    			function(){
					  var uuu = "admin_fn_menu/removeMenuItem.htm";
					  var postData={fnMenuItem: fnMenuItem};
				  	  $.ajax({
				  		 type : 'POST',
				  		 url : uuu,
				  		 dataType: 'json',
				  		 contentType: 'application/json',
				  		 data: JSON.stringify(postData),
				  		 success : function(data){
				  			$scope.$apply(function(){$scope.fnMenuItem=data.fnMenuItem;}); 
				  			$route.reload();
						 },
						 error : function(data){
							 console.log(data);
							 modalService.showFailure("Fail","Error while deleting: "+ data.responseText);
						 }
				  	  });
					
    	})
		
	};	
	
	$scope.editRoleFunction = null;
	var dialog = null;
	$scope.editRoleFunctionPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = availableRoleFunction;
		$( "#dialog" ).dialog({
		      modal: true
	    });
	};
	
    $scope.editMenuItemModalPopup = function(availableFnMenuItem) {
		$scope.editFnMenuItem = availableFnMenuItem;
		var modalInstance = $modal.open({
		    templateUrl: 'fn_menu_add_popup.html',
		    controller: 'fn_menu_popupController',
		    resolve: {
		    	message: function () {
		    		var message = {
		    				availableFnMenuItem: $scope.editFnMenuItem
                           	};
		          return message;
		        }					
		      }
		  }); 
		modalInstance.result.then(function(response){
            $scope.availableFnMenuItems=response.availableFnMenuItems;
            $route.reload();
        });
	};	
	
	$scope.editRoleFunctionModalPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = availableRoleFunction;
		var modalInstance = $modal.open({
		    templateUrl: 'edit_role_function_popup.html',
		    controller: 'rolefunctionpopupController',
		    resolve: {
		    	message: function () {
		    		var message = {
		    				availableRoleFunction: $scope.editRoleFunction
                           	};
		          return message;
		        }					
		      }
		  }); 
		modalInstance.result.then(function(response){
            console.log('response', response);
            $scope.availableRoleFunctions=response.availableRoleFunctions;
        });
	};
	
	$scope.addNewRoleFunctionModalPopup = function(availableRoleFunction) {
		$scope.editRoleFunction = null;
		var modalInstance = $modal.open({
		    templateUrl: 'edit_role_function_popup.html',
		    controller: 'rolefunctionpopupController',
		    resolve: {
		    	message: function () {
		    		var message = {
		    				availableRoleFunction: $scope.editRoleFunction
                           	};
		          return message;
		        }					
		      }
		  }); 
		modalInstance.result.then(function(response){
            console.log('response', response);
            $scope.availableRoleFunctions=response.availableRoleFunctions;
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
								 modalService.showFailure("Fail","Error while deleting: "+ data.responseText);
							 }
					  	  });
						
	    	})
			
		};

});
