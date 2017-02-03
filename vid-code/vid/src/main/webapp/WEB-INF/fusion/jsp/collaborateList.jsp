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
<div>
<a href="JavaScript:void(0);" style= "color:#00547A" onClick="downloadScreenCaptureExtenstion()" id="install-button">
		Please download the extension for ScreenCapture and refresh page</a>
		</div>
<div ng-controller="collaborateListController" id="collaborate_ctrl">
	<div>
		<h1 class="heading1" style="margin-top:20px;">User List</h1>
		<div style="margin-top:30px">
			<table att-table table-data="tableData" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">

			    <thead  att-table-row type="header">
					<tr>
						<th att-table-header key="id">User ID</th>
			            <th att-table-header key="lastName">Last Name</th>        
			            <th att-table-header key="firstName">First Name</th>    
			            <th att-table-header key="email">Email</th>
			            <th att-table-header key="orgUserId">UserId</th>        
			            <th att-table-header key="online" default-sort="d">Online/Offline</th>         
			        </tr>
			    </thead>
			    <tbody att-table-row type="body" row-repeat="rowData in tableData">
			        <tr>
		            	<td att-table-body ng-bind="rowData['id']"></td>
		            	<td att-table-body ng-bind="rowData['lastName']"></td>
		            	<td att-table-body ng-bind="rowData['firstName']"></td>
		            	<td att-table-body ng-bind="rowData['email']"></td>
		            	<td att-table-body ng-bind="rowData['orgUserId']"></td>
		        		<td att-table-body >
		        		   <tag-badges ng-hide="rowData.online" style-type="color" class="lred" ng-click="rowData.isActive=true;openCollaboration(rowData.chatId)">Offline</tag-badges>
						   <tag-badges ng-show="rowData.online" style-type="color" class="lgreen" ng-click="rowData.isActive=false;openCollaboration(rowData.chatId)">Online</tag-badges>				
		        	   </td>
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


<!-- handling websocket peer broadcast session -->
  <script type="text/javascript" src="app/fusion/scripts/socket/peerBroadcast.js"></script> 
  <script type="text/javascript" src="app/fusion/external/utils/js/browserCheck.js"></script>
  <script>
 
	var initialPageVisit = "${sessionScope.initialPageVisit}";
	var userId     = "${sessionScope.user.orgUserId}";
	socketSetup(initialPageVisit, userId, null, "socketSend");
	
  
  </script>
 
<script>
var popupModalService;
app.controller("collaborateListController", function ($scope,$http,modalService, $modal) { 
	// Table Data
	$scope.tableData=${model.profileList};
	$scope.viewPerPage = 20;
    $scope.scrollViewsPerPage = 2;
    $scope.currentPage = 1;
    $scope.totalPage;
    $scope.searchCategory = "";
    $scope.searchString = "";
    popupModalService = modalService;
    setPopupService(modalService);
 /*    modalService.showSuccess('','Modal Sample') ; */
	for(x in $scope.tableData){
		if($scope.tableData[x].active_yn=='Y')
			$scope.tableData[x].active_yn=true;
		else
			$scope.tableData[x].active_yn=false;
	}
    $scope.openCollaboration = function(chatId){
    	openInNewTab('collaboration?chat_id=' + chatId);
    	
    }
   
    $scope.toggleProfileActive = function(profileId) {
    	if (confirm("You are about to change user's active status. Do you want to continue?")) {
                 $http.get("profile/toggleProfileActive?profile_id="+profileId).success(function(){});
    	}
    };
   
});

function openInNewTab(url) {
	  
	//popupModalService.popupConfirmWin("Confirm","");
	var win = window.open(url, '_blank');
	win.popupService = popupModalService;
	win.focus();
};

function downloadScreenCaptureExtenstion() {
	  
	  var chromeURL = 'https://chrome.google.com/webstore/detail/icgmlogfeajbfdffajhoebcfbibfhaen';
	  var firefoxURL = 'https://addons.mozilla.org/en-US/firefox/addon/screen-capturing-capability';
	  var url;
	  
	  if(isChrome)
		url = chromeURL;
	  else if(isFirefox)
		url = 	firefoxURL;
	  
	  var win = window.open(url);
	  win.focus();
};

</script>


 
  <div id="peerBroadcastSection"> </div>
