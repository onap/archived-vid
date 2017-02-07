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

<div ng-controller="usageListController" > 	
	<div class="pageTitle">
		<h3>
          Usage
		</h3>
    </div>
    <br/>
		The following shows all users currently logged into the application. Click the icon to expel a user from the application.
    
	<div title="Current User Sessions">
	  <table att-table table-data="users" current-page="1">
	  	
	  	<thead att-table-row type="header">
	  		<tr>
	  			<th att-table-header sortable="false" width="10%" align="left">Current User Sessions</th>
	  			<th att-table-header sortable="false" width="10%" align="center"></th>
	  			<th att-table-header sortable="false" width="10%" align="center"></th>
	  			<th att-table-header sortable="false" width="10%" align="center"></th>
	  			<th att-table-header sortable="false" width="10%" align="center"></th>
	  			<th att-table-header sortable="false" width="10%" align="center"></th>
	  		</tr>
	  		<tr>
	  			<th att-table-header sortable="false" width="10%" align="center">User Id</th>
	  			<th att-table-header sortable="false" width="10%" align="center">User Name</th>
	  			<th att-table-header sortable="false" width="10%" align="center">Email</th>
	  			<th att-table-header sortable="false" width="10%" align="center">Last Access Time (minutes)</th>
	  			<th att-table-header sortable="false" width="10%" align="center">Time Remaining (minutes)</th>
	  			<th att-table-header sortable="false" width="10%" align="center">Expel?</th>
	  		</tr>
	  	</thead>
	  	<tbody att-table-row type="body" row-repeat="user in users" style="max-height: 980px;" ><!-- background colors will alternate not properly with multiple tbody--> 
		  <tr>
		    <td att-table-body width="10%">{{user.id}}</td>
		    <td att-table-body width="10%">{{user.lastName}}</td>
		    <td att-table-body width="10%">{{user.email}}</td>
		    <td att-table-body width="10%">{{user.lastAccess}}</td>
		    <td att-table-body width="10%">{{user.remaining}}</td>
		    <td att-table-body width="10%"><div ng-hide="user.delete=='yes'">Current Session</div>
		    	<div ng-click="removeSession(user.sessionId);" ng-hide="user.delete=='no'" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
		    </td>
		  </tr>
		</tbody>
		</table>
	</div>
</div>




<script>
app.controller('usageListController', function ($scope,$interval,$http,$modal,modalService){
	
	$scope.users=${model};console.log($scope.users);	
	$scope.removeSession = function(sessionId) {
		modalService.popupConfirmWin("Confirm","You are about to expel this user from the application. All of their unsaved data will be lost. Do you want to continue?",
    			function(){
			          $http.get("usage_list/removeSession?deleteSessionId="+sessionId).success(function(response){$scope.users=response;});
    	});
	}
});
</script>
