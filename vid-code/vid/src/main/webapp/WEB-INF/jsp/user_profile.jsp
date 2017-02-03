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
<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>
<div ng-controller="userProfileSampleController">
	<div>
		<h1 class="heading1" style="margin-top:20px;">Profile Search</h1>
		<div style="margin-top:30px">
			<table att-table table-data="tableData" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">

			    <thead  att-table-row type="header">
					<tr>
						<th att-table-header key="id">User ID</th>
			            <th att-table-header key="last_name">Last Name</th>        
			            <th att-table-header key="first_name">First Name</th>    
			            <th att-table-header key="email">Email</th>
			            <th att-table-header key="org_user_id">OrgUserId</th>        
			            <th att-table-header key="org_manager_userid">Manager OrgUserId</th> 
			        </tr>
			    </thead>
			    <tbody att-table-row type="body" row-repeat="rowData in tableData">
			        <tr>
		            	<td att-table-body >{{rowData.id}}</td>
		            	<td att-table-body >{{rowData.last_name}}</td>
		            	<td att-table-body >{{rowData.first_name}}</td>
		            	<td att-table-body >{{rowData.email}}</td>
		            	<td att-table-body >{{rowData.orgUserId}}</td>
		            	<td att-table-body >{{rowData.org_manager_userid}}</td>
			        </tr>     
			    </tbody>	  
			</table>
		</div>
	</div>	
	<div class="fn-ebz-container">
	          Rows Per Page:
	          <input class="fn-ebz-text" type="text" ng-model="viewPerPage" size="5" style="width: 47px;">
    </div>
	<div class="fn-ebz-container">
	          Current Page:
	          <input class="fn-ebz-text" type="text" ng-model="currentPage" size="5" style="width: 47px;">
    </div>
    <div class="fn-ebz-container">
	          Total Page(s):
	          <input class="fn-ebz-text" type="text" ng-model="totalPage" size="5" readonly="true" style="width: 47px;">
    </div>
</div>
	
<script>
app.controller("userProfileSampleController", function ($scope,$http,modalService, $modal) { 
	// Table Data
	$scope.tableData=${model.customerInfo};
	console.log($scope.tableData);
	$scope.viewPerPage = 20;
    $scope.scrollViewsPerPage = 2;
    $scope.currentPage = 1;
    $scope.totalPage;
    $scope.searchCategory = "";
    $scope.searchString = "";
   // modalService.showSuccess('','Modal Sample') ;
	for(x in $scope.tableData){
		if($scope.tableData[x].active_yn=='Y')
			$scope.tableData[x].active_yn=true;
		else
			$scope.tableData[x].active_yn=false;
	}

});
</script>
