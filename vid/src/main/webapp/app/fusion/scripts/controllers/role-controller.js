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
app.controller('roleController', function ($scope, modalService, $modal, AdminService,$routeParams){
	//$scope.role=${role};
	//console.log($scope.role.roleFunctions);
		
	$( "#dialogRoleFunction" ).hide();
	$( "#dialogChildRole" ).hide();
	
	//$scope.ociavailableRoleFunctions=${availableRoleFunctions};
	
	AdminService.getRole($routeParams.roleId).then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		
  		$scope.role =JSON.parse($scope.data.role);
  		console.log($scope.role);
  		
  		$scope.ociavailableRoleFunctions =JSON.parse($scope.data.availableRoleFunctions);
  		console.log($scope.ociavailableRoleFunctions);
  		$scope.availableRoleFunctions=[];
  		
  		if($scope.ociavailableRoleFunctions)
  			$.each($scope.ociavailableRoleFunctions, function(i, a){ 
  				var availableRoleFunction = a;
  				availableRoleFunction.selected = false;
  			    $.each($scope.role.roleFunctions, function(j, b){ 
  			    	if(a.code === b.code) {
  			    		availableRoleFunction.selected = true;
  			    	}
  			    });
  			    $scope.availableRoleFunctions.push(availableRoleFunction);	    
  		});	
  		

  		$scope.ociavailableRoles=JSON.parse($scope.data.availableRoles);
  		console.log($scope.ociavailableRoles);
  		console.log("testing roles if exist");
  		$scope.availableRoles=[];
  		
  		if($scope.ociavailableRoles)
  			$.each($scope.ociavailableRoles, function(i, a){ 
  				var availableRole = a;
  				availableRole.selected = false;
  				if($scope.role.childRoles){
  			    $.each($scope.role.childRoles, function(j, b){ 
  			    	if(a.id === b.id) {
  			    		availableRole.selected = true;
  			    	}
  			    });
  				};
  			    $scope.availableRoles.push(availableRole);	    
  		});
  			
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});

	$scope.saveRole = function() {
				var exists = false;	
				for(x in $scope.availableRoles){
				  console.log($scope.availableRoles[x].name);
					if($scope.availableRoles[x].name==$scope.role.name){
						modalService.showFailure("Warning",  "Role already exists.");
						exists = true;
						//$modalInstance.close({availableRoleFunctions:message.availableRoleFunctions});
					}
				}
				if(!exists){
					var uuu = "role/saveRole.htm?role_id="+$routeParams.roleId;
					  var postData={role: $scope.role, childRoles: $scope.role.childRoles, roleFunctions : $scope.role.roleFunctions};
				  	  $.ajax({
				  		 type : 'POST',
				  		 url : uuu,
				  		 dataType: 'json',
				  		 contentType: 'application/json',
				  		 data: JSON.stringify(postData),
				  		 success : function(data){
				  			modalService.showSuccess("Success","Update Successful.");
						 },
						 error : function(data){
							 console.log(data);
							 modalService.showFailure("Fail","Error while saving.");
						 }
				  	  });
				}
			  
			};
		
	$scope.addNewRoleFunctionModalPopup = function() {
			var modalInstance = $modal.open({
			    templateUrl: 'role_functions_popup.html',
			    controller: 'rolepopupController',
			    backdrop: 'static',
			    resolve: {
			    	roleId: function () {
				          return $routeParams.roleId;
				        },
			    	role: function () {
			          return $scope.role;
			        },
			        availableRoles: function () {
				          return $scope.ociavailableRoles;
				    },
				    availableRoleFunctions: function () {
				          return $scope.ociavailableRoleFunctions;
				    }
			      }
			  }); 
			modalInstance.result.then(function(response){
	            console.log('response', response);
	            $scope.role=response.role;
	        });
	};
		
	 $scope.addNewChildRoleModalPopup = function() {
			var modalInstance = $modal.open({
			    templateUrl: 'child_roles_popup.html',
			    controller: 'rolepopupController',
			    backdrop: 'static',
			    resolve: {
			    	roleId: function () {
				          return $routeParams.roleId;
				        },
			    	role: function () {
			          return $scope.role;
			        },
			        availableRoles: function () {
				          return $scope.ociavailableRoles;
				    },
				    availableRoleFunctions: function () {
				          return $scope.ociavailableRoleFunctions;
				    }		
			      }
			  }); 
			modalInstance.result.then(function(response){
	            console.log('response', response);
	            $scope.role=response.role;
	        });
		};
		
		

		$scope.removeRoleFunction = function(roleFunction) {
			modalService.popupConfirmWin("Confirm","You are about to remove the role function "+roleFunction.name+" from the role for "+$scope.role.name+". Do you want to continue?",
	    			function(){
						var uuu = "role/removeRoleFunction.htm?role_id=" + $routeParams.roleId;
						  var postData={roleFunction:roleFunction};
					  	  $.ajax({
					  		 type : 'POST',
					  		 url : uuu,
					  		 dataType: 'json',
					  		 contentType: 'application/json',
					  		 data: JSON.stringify(postData),
					  		 success : function(data){
					  			$scope.$apply(function(){
					  				$scope.role=data.role;
					  				$.each($scope.availableRoleFunctions, function(k, c){ 
					  			    	if(c.code === roleFunction.code) {
					  			    		c.selected = false;
					  			    	}
					  			    });
					  			});
					  			
							 },
							 error : function(data){
								 modalService.showFailure("Fail","Error while saving.");
							 }
					  	  });
				
	    	})
		
		};
		
		$scope.removeChildRole = function(childRole) {
			modalService.popupConfirmWin("Confirm","You are about to remove the child role "+childRole.name+" from the role for "+$scope.role.name+". Do you want to continue?",
	    			function(){
					var uuu = "role/removeChildRole.htm?role_id=" + $routeParams.roleId;
					  var postData={childRole:childRole};
				  	  $.ajax({
				  		 type : 'POST',
				  		 url : uuu,
				  		 dataType: 'json',
				  		 contentType: 'application/json',
				  		 data: JSON.stringify(postData),
				  		 success : function(data){
				  			$scope.$apply(function(){
				  				$scope.role=data.role;
				  				$.each($scope.availableRoles, function(k, c){ 
				  			    	if(c.id === childRole.id) {
				  			    		c.selected = false;
				  			    	}
				  			    });
				  			});
				  			
						 },
						 error : function(data){
							 modalService.showFailure("Fail","Error while saving.");
						 }
				  	  });
				
	    	})
			
		};
		
});
