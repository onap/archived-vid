<%--
  ================================================================================
  eCOMP Portal SDK
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>
<%@ include file="/WEB-INF/fusion/jsp/popup_modal_rolefunction.html" %>
<%@ include file="/WEB-INF/fusion/jsp/popup_modal_role.html" %>
<style type="text/css">
    .bc-style{
    	margin: 20px;
    	align: left;
    }
</style>
<div class="bc-style">
    <ul class="breadcrumb" >
        <li><a href="welcome">Home</a></li>
        <li><a href="role_list">Roles</a></li>
        <li class="active">Role</li>
    </ul>
</div>
<div class="pageTitle">
<h3>
	<c:choose>
		<c:when test="${!empty param.role_id}">
			<h1 class="heading1" style="margin-top:20px;">Role Edit</h1>
		</c:when>
		<c:otherwise>
			<h1 class="heading1" style="margin-top:20px;">Role Create</h1>
		</c:otherwise>
	</c:choose>
	</h3>
</div>

<div ng-controller="roleController" >

	<br>
     Please edit the role details below:&nbsp;<br>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>Name:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="role.name"
			maxlength="30" /> 
	</div>

	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label">Priority:</label><BR>
		<input type="text" class="fn-ebz-text" ng-model="role.priority"
			maxlength="30" /> 
	</div>
	
	<div align="left" >
		<button type="submit" ng-click="saveRole();" att-button
			btn-type="primary" size="small">Save</button>
	</div>
	
	<br>
	<div class="pageTitle">
		<label>Role Functions</label>
		<a ng-click="addNewRoleFunctionModalPopup();" class="icon-add" size="small"></a>
	</div>

	<table table-data="role.roleFunctions" att-table >
	     <thead att-table-row type="header">
  		<tr>
  			<th att-table-header sortable="false" align="left"  width="90%">Name</th>
  			<th att-table-header sortable="false"  width="10%">Remove?</th>
  		</tr>
  		 </thead>
  		 <tbody att-table-row type="body" row-repeat="roleFunction in role.roleFunctions" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->
		  <tr>
		    <td att-table-body width="90%" >{{ roleFunction.name }}</td>
		    <td att-table-body width="10%">
		    	<div ng-click="removeRoleFunction(roleFunction);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
		     </td>
		  </tr>
		 </tbody>
	</table>
	<a href="role_function_list.htm">Manage Role Functions</a><br><br>
	
	<div class="pageTitle">
		<label>Child Roles</label>
		<a ng-click="addNewChildRoleModalPopup();" class="icon-add" size="small"></a>
	</div>
	
	<table table-data="role.childRoles" att-table >
	     <thead att-table-row type="header">
  		<tr>
  			<th att-table-header sortable="false" align="left"  width="90%">Name</th>
  			<th att-table-header sortable="false"  width="10%">Remove?</th>
  		</tr>
  		 </thead>
  		 <tbody att-table-row type="body" row-repeat="role in role.childRoles" style="max-height: 980px;" >
		  <tr>
		    <td att-table-body width="90%" >{{ role.name }}</td>
		    <td att-table-body width="10%">
		    	<div ng-click="removeChildRole(role);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
		     </td>
		  </tr>
		 </tbody>
	</table>
	
	<div id="dialogChildRole" title="Select Child Roles">
	  <table table-data="availableRoles" att-table>
	  	<thead att-table-row type="header">
	  		<tr>
	  			<th att-table-header sortable="false" width="10%"> </th>
	  			<th att-table-header sortable="false" width="90%">Role</th>
	  		</tr>
	  		</thead>
	  		<tbody att-table-row type="body" row-repeat="availableRole in availableRoles" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->
		  	<tr>
		  	<td att-table-body width="10%">
		  		<div ng-click="toggleChildRole(availableRole.selected,availableRole);">
		    	<input type="checkbox" ng-model="availableRole.selected" att-toggle-main>
		    	</div>
		    </td>
		    <td att-table-body width="90%">{{ availableRole.name }}</td>
		    
		  </tr>
		  </tbody>
		</table>
	</div>
</div>



<script>
app.controller('roleController', function ($scope, modalService, $modal){
	$scope.role=${role};
	console.log($scope.role.roleFunctions);
		
	$( "#dialogRoleFunction" ).hide();
	$( "#dialogChildRole" ).hide();
	
	$scope.ociavailableRoleFunctions=${availableRoleFunctions};
	
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
		;

		$scope.saveRole = function() {
			  var uuu = "role/saveRole.htm?role_id=${param.role_id}";
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
			};
		
		$scope.addNewRoleFunctionModalPopup = function() {
			var modalInstance = $modal.open({
			    templateUrl: 'role_functions_popup.html',
			    controller: 'rolepopupController',
			    backdrop: 'static',
			    resolve: {
			    	role: function () {
			          return $scope.role;
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
			    	role: function () {
			          return $scope.role;
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
				          var uuu = "role/removeRoleFunction.htm?role_id=${param.role_id}";
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
				      var uuu = "role/removeChildRole.htm?role_id=${param.role_id}";
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
</script>
