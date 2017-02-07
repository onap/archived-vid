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

<%-- <%@ include file="/WEB-INF/fusion/jsp/include.jsp" %> --%>
      
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="/WEB-INF/fusion/jsp/popup_modal_rolefunction.html" %>
<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>
<div ng-controller="roleFunctionListController" > 	
	<div class="pageTitle">

	        <h1 class="heading1" style="margin-top:20px;">Role Functions</h1>
	        
	        <a ng-click="addNewRoleFunctionModalPopup();" class="icon-add" size="small" ></a>
	 <br><br>
	        
	</div>
	 
	<br>
	Click on the edit icon to update a role function, the plus icon to add additional role functions, or the delete icon to remove them.
	<br>
	<div id="rolesTable" title="Role Functions">
	  <table att-table table-data="availableRoleFunctions" current-page="1">
	  	<thead att-table-row type="header">
	  		<tr>
	  			<th att-table-header width="70%">Name</th>
	  			<th att-table-header width="10%">Code</th>
	  			<th att-table-header width="10%">Edit?</th>
	  			<th att-table-header width="10%">Delete?</th>
	  		</tr>
	  	</thead>
	  	<tbody att-table-row type="body" row-repeat="availableRoleFunction in availableRoleFunctions" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->
		  <tr>
		    <td att-table-body width="70%">{{ availableRoleFunction.name }}</td>
		    <td att-table-body width="10%">{{ availableRoleFunction.code }}</td>
		    <td att-table-body width="10%">
		    <!-- <a ng-click="editRoleFunctionPopup(availableRoleFunction);" >
		    <img src="static/fusion/images/editicon.gif">
		    </a> -->
		    <div ng-click="editRoleFunctionModalPopup(availableRoleFunction);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-edit"></a></div>
		    </td>
		     <td att-table-body width="10%">
		    	<!-- <a ng-click="removeRole(availableRoleFunction);" ><img src="static/fusion/images/deleteicon.gif"></a> -->
		    	<div ng-click="removeRole(availableRoleFunction);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
		     </td>
		  </tr>
		</tbody>
		</table>
	</div>
	
<!-- 	<div align="left" style="marin-bottom: 50px;"> -->
<!-- 		<button type="submit" onClick="window.location='role_function.htm';" att-button -->
<!-- 			btn-type="primary" size="small">Create</button> -->
<!-- 	</div> -->
	
	<div id="dialog" title="Add Role Function">
	   	
	   	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>Name:</label><br>
		<input type="text" class="fn-ebz-text" ng-model="editRoleFunction.name"
			maxlength="30" /> 
		</div>
	   	<br/>
	   	<div class="fn-ebz-container" >
		<label class="fn-ebz-text-label"><sup><b>*</b></sup>Code:</label><br>
		<input type="text" class="fn-ebz-text" ng-model="editRoleFunction.code" ng-disabled="editRoleFunction.code!=null"
			maxlength="30" /> 
		</div>
		<br/>
		<button type="submit" ng-click="saveRoleFunction(editRoleFunction);" att-button
			btn-type="primary" size="small">Save</button>
		
	</div>
	
</div>




<script>
app.controller('roleFunctionListController', function ($scope, modalService, $modal){
	$( "#dialog" ).hide();
	$scope.availableRoleFunctions=${availableRoleFunctions};
	
	
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
				 alert("Error while saving.");
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
</script>
