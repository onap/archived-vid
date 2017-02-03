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
<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>

<div class="pageTitle">
	<h1 class="heading1" style="margin-top:20px;">Roles</h1>
</div>
<br>
Click on a Role to view its details.
     
<div ng-controller="roleListController" > 	
	<div id="dialog" title="Roles">
	  <table att-table table-data="availableRoles" current-page="1">
	  	<thead att-table-row type="header">
	  		<tr>
	  			<th att-table-header width="70%">Name</th>
	  			<th att-table-header width="10%">Priority</th>
	  			<th att-table-header width="10%">Active?</th>
	  			<th att-table-header width="10%">Delete?</th>
	  		</tr>
	  	</thead>
	  	<tbody att-table-row type="body" row-repeat="availableRole in availableRoles" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody-->
<!-- 		  <tr ng-repeat="availableRole in availableRoles track by availableRole.id">		  	 -->
		  <tr>
		    <td width="70%"><a href="role.htm?role_id={{availableRole.id}}">{{ availableRole.name }}</a></td>
		    <td width="10%">{{ availableRole.priority }}</td>
		    <td width="10%">
		    	<div ng-click="toggleRole(availableRole.active,availableRole);">
		    	<input type="checkbox" ng-model="availableRole.active" att-toggle-main>
		    	</div>
		    </td>
		     <td att-table-body width="10%">
		    	<div ng-click="removeRole(availableRole);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
		     </td>
		  </tr>
		</tbody>
		</table>
	</div>
	
	<div align="left" style="marin-bottom: 50px;">
		<button type="submit" onClick="window.location='role.htm';" att-button
			btn-type="primary" size="small">Create</button>
	</div>
	
</div>




<script>
app.controller('roleListController', function ($scope,modalService){
	
	$scope.availableRoles=${availableRoles};
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
</script>
