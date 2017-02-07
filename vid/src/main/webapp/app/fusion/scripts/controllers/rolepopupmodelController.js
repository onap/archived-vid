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
var rolepopupController =  function ($scope, $modalInstance, role, roleId, availableRoles, availableRoleFunctions,AdminService,modalService){
	
	  		$scope.role = role;
	  		console.log($scope.role);
	  		if($scope.role.childRoles==null){
	  			$scope.role.childRoles=[];
	  		}
	  		
	  		$scope.ociavailableRoles=availableRoles;
	  		console.log($scope.ociavailableRoles);
	  		
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
	  			
	  		$scope.ociavailableRoleFunctions = availableRoleFunctions; 
	  		console.log($scope.ociavailableRoleFunctions);
	  		$scope.availableRoleFunctions = []; 
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
	  		//$scope.resetMenu();
		
		$scope.toggleRoleFunction = function(selected,availableRoleFunction) {
			//alert('toggleRole: '+selected);
			 
			if(!selected) {
				//remove role function
				if(role.id==null){
					var index = $scope.role.roleFunctions.indexOf(availableRoleFunction);
				 	if(index>=0)
						$scope.role.roleFunctions.splice(index, 1);
					return;
				}
				var uuu = "role/removeRoleFunction.htm?role_id=" + roleId;
				modalService.popupConfirmWinWithCancel("Confirm","You are about to remove the role function "+availableRoleFunction.name+" from the role for "+$scope.role.name+". Do you want to continue?",
		    			function(){
								var postData={roleFunction:availableRoleFunction};
							  	  $.ajax({
							  		 type : 'POST',
							  		 url : uuu,
							  		 dataType: 'json',
							  		 contentType: 'application/json',
							  		 data: JSON.stringify(postData),
							  		 success : function(data){
							  			$scope.$apply(function(){$scope.role=data.role;}); 
									 },
									 error : function(data){
										 modalService.showFailure("Fail","Error while saving.");
									 }
							  	  });
					
		    	},
		    	function(){
		    		availableRoleFunction.selected=!availableRoleFunction.selected;
		    	})
	
			} else {
				//add role function
				if(role.id==null){
					$scope.role.roleFunctions.push(availableRoleFunction);
					return;
				}
				var uuu = "role/addRoleFunction.htm?role_id=" + roleId;
				
				modalService.popupConfirmWinWithCancel("Confirm","You are about to add the role function "+availableRoleFunction.name+" to the role for "+$scope.role.name+". Do you want to continue?",
		    			function(){
					  var postData={roleFunction:availableRoleFunction};
				  	  $.ajax({
				  		 type : 'POST',
				  		 url : uuu,
				  		 dataType: 'json',
				  		 contentType: 'application/json',
				  		 data: JSON.stringify(postData),
				  		 success : function(data){
				  			$scope.$apply(function(){$scope.role=data.role;}); 
						 },
						 error : function(data){
							 modalService.showFailure("Fail","Error while saving.");
						 }
				  	  });
					
		    	},
		    	function(){
		    		availableRoleFunction.selected=!availableRoleFunction.selected;
		    	})
			}
			
			  
	};
	
	$scope.toggleChildRole = function(selected,availableRole) {
		//alert('toggleRole: '+selected);

		if(!selected) {
			//remove role
			if(role.id==null){
				var index = $scope.role.childRoles.indexOf(availableRole);
			 	if(index>=0)
					$scope.role.childRoles.splice(index, 1);
				return;
			}
			var uuu = "role/removeChildRole.htm?role_id=" + roleId;
			
			modalService.popupConfirmWinWithCancel("Confirm","You are about to remove the child role "+availableRole.name+" from the role for "+$scope.role.name+". Do you want to continue?",
	    			function(){
						var postData={childRole:availableRole};
					  	  $.ajax({
					  		 type : 'POST',
					  		 url : uuu,
					  		 dataType: 'json',
					  		 contentType: 'application/json',
					  		 data: JSON.stringify(postData),
					  		 success : function(data){
					  			 console.log('role',data.role);
					  			 $scope.$apply(function(){$scope.role=data.role;}); 
							 },
							 error : function(data){
								 modalService.showFailure("Fail","Error while saving.");
							 }
					  	  });
				
	    	},
	    	function(){
	    		 availableRole.selected=true;			
	    	})
	    	
		} else {
			//add role
			if(role.id==null){
				$scope.role.childRoles.push(availableRole);
				return;
			}
			var uuu = "role/addChildRole.htm?role_id=" + roleId;
			
			modalService.popupConfirmWinWithCancel("Confirm","You are about to add the child role "+availableRole.name+" to the role for "+$scope.role.name+". Do you want to continue?",
	    			function(){
				  var postData={childRole:availableRole};
			  	  $.ajax({
			  		 type : 'POST',
			  		 url : uuu,
			  		 dataType: 'json',
			  		 contentType: 'application/json',
			  		 data: JSON.stringify(postData),
			  		 success : function(data){
			  			$scope.$apply(function(){$scope.role=data.role;}); 
					 },
					 error : function(data){
						 modalService.showFailure("Fail","Error while saving.");
					 }
			  	});
				
	    	},
	    	function(){
	    		availableRole.selected=false;
	    	})
		}
		
		  
	};
	
	$scope.close = function() {
		console.log('role', $scope.role);
		$modalInstance.close({role:$scope.role});
	};
	
}
