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
<%-- <%@ include file="/WEB-INF/fusion/jsp/include.jsp"%> --%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>

<div align="left" ng-controller="cacheAdminController">
	<div class="pageTitle"><h3>Cache Regions</h3></div>
	These are the regions which are currently defined in the cache. 'Items'	and 'Bytes' refer to the elements currently in memory (not spooled).
	You can clear all items for a region by clicking on the Clear icon next	to the desired region below. You can also <a href="#" ng-click="clearAll();">clear all regions</a> which
	empties the entire cache. <br /><br />

	<div style="width: 97%; border-top: 1px solid #c4c4c4; margin-top: 0px; background-color: #fff; padding: 10px 0; color: #646464s">
		<div style="margin: 0 20px 10px 0px; font-size: 80%; float: left; width: 97%;">
			<div class="col-md-4"><b>Cache Name</b></div>
			<div class="col-md-2"><b># of Items</b></div>
			<div class="col-md-2"><b>Bytes</b></div>
			<div class="col-md-2"><b>Status</b></div>
			<div class="col-md-3"><b>Memory Hits</b></div>
			<div class="col-md-3"><b>Aux Hits</b></div>
			<div class="col-md-3"><b>Not Found Misses</b></div>
			<div class="col-md-3"><b>Expired Misses</b></div>
			<div class="col-md-1"><b>Clear?</b></div>
			<div class="col-md-1"><b>Items</b></div>
		</div>
		<div ng-repeat="region in regions">
			<div style="margin: 0 20px 10px 0px; font-size: 80%; float: left; width: 97%;">
				<div class="col-md-4"><a href="#" tooltip="Click to Show Region Details" ng-click="showRegionDetails(region.cacheName);">{{region.cacheName}}</a></div>
				<div class="col-md-2">{{region.size}}</div>
				<div class="col-md-2">{{region.byteCount}}</div>
				<div class="col-md-2">{{region.status}}</div>
				<div class="col-md-3">{{region.hitCountRam}}</div>
				<div class="col-md-3">{{region.hitCountAux}}</div>
				<div class="col-md-3">{{region.missCountNotFound}}</div>
				<div class="col-md-3">{{region.missCountExpired}}</div>
				<div class="col-md-1">
					<div ng-click="clearRegion(region.cacheName);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
				</div>
				<div class="col-md-1"><span class="att--tree__plus col-md-1" ng-class="{minus: bling$index}" ng-click="bling$index = !bling$index" style="display: block;"></span></div>
			</div>
			<div style="margin: 0px 20px 0px 30px; font-size: 80%; float: left; width: 95%; border-top: 1px solid #e4e4e4; padding-top: 10px"
				ng-show="bling$index">
				<span class="att--tree__arrow" style="height: 20px; width: 20px; display: inline-block; float: left"></span>
				<div class="col-md-12"><b>Key</b></div>
				<div class="col-md-2"><b>Eternal?</b></div>
				<div class="col-md-4"><b>Created</b></div>
				<div class="col-md-2"><b>Max Life</b></div>
				<div class="col-md-2"><b>Expires</b></div>
				<div class="col-md-1"><b>Clear?</b></div>
			</div>
			<div ng-repeat="item in region.items">
				<div style="margin: 0px 20px 0px 30px; font-size: 80%; float: left; width: 95%;" ng-show="bling$index">
					<span class="att--tree__arrow" style="height: 20px; width: 20px; display: inline-block; float: left"></span>
					<div class="col-md-12"><a href="#" tooltip="Click to Show Item Details" ng-click="showItemDetails(region.cacheName,item.key);">{{item.key}}</a></div>
					<div class="col-md-2">{{item.eternal}}</div>
					<div class="col-md-4">{{item.createTime}}</div>
					<div class="col-md-2">{{item.maxLifeSeconds}}</div>
					<div class="col-md-2">{{item.expiresInSeconds}}</div>
					<div class="col-md-1">
						<div ng-click="clearItem(region.cacheName,item.key);" style="font-size:20px;"><a href="javascript:void(0)" class="icon-trash"></a></div>
					</div>
				</div>
			</div>
			<div style="clear: both"></div>
		</div>
	</div>
</div>

<script>
app.controller('cacheAdminController', function ($scope,$interval,$http,$modal,modalService){
	$scope.regions=${model};
	 
	$scope.clearAll = function() {
		modalService.popupConfirmWin("Confirm",'You are about to clear all of the items for all cache regions. Do you want to continue?',
    			function(){
			        $http.get("jcs_admin/clearAll").success(function(response){$scope.regions=response;});
					
    	})
	};
	
	$scope.clearRegion = function(cacheName) {
		modalService.popupConfirmWin("Confirm",'You are about to clear all of the items in the cache region "' + cacheName + '". Do you want to continue?',
    			function(){
			       $http.get("jcs_admin/clearRegion?cacheName="+cacheName).success(function(response){$scope.regions=response;});
					
    	})
	};
	
	$scope.clearItem = function(cacheName,key) {
		modalService.popupConfirmWin("Confirm",'You are about to clear this item from the cache region "' + cacheName + '". Do you want to continue?',
    			function(){
			       $http.get("jcs_admin/clearItem?keyName="+key+"&cacheName="+cacheName).success(function(response){$scope.regions=response;});
					
    	})
	};
	
	$scope.showRegionDetails = function(cacheName) {
		$http.get("jcs_admin/showRegionDetails?cacheName="+cacheName).success(function(response){modalService.showSuccess('',response);});
	};

	$scope.showItemDetails = function(cacheName,key) {
			$http.get("jcs_admin/showItemDetails?keyName="+key+"&cacheName="+cacheName).success(function(response){ 
				var message = "CacheName: "+ response.cacheName 
					+"\nkey: "+response.key
					+"\nIS_SPOOL: "+response.attr.IS_SPOOL
					+"\nIS_LATERAL: "+response.attr.IS_LATERAL
					+"\nIS_REMOTE: "+response.attr.IS_REMOTE
					+"\nIS_ETERNAL: "+response.attr.IS_ETERNAL
					+"\nversion: "+response.attr.version
					+"\nmaxLifeSeconds: "+response.attr.maxLifeSeconds
					+"\nmaxIdleTimeSeconds: "+response.attr.maxIdleTimeSeconds
					+"\nsize: "+response.attr.size
					+"\ncreateTime: "+response.attr.createTime
					+"\nlastAccessTime: "+response.attr.lastAccessTime
					+"\nidleTime: "+response.attr.idleTime
					+"\ntimeToLiveSeconds: "+response.attr.timeToLiveSeconds
					+"\nisSpool: "+response.attr.isSpool
					+"\nisLateral: "+response.attr.isLateral
					+"\nisRemote: "+response.attr.isRemote
					+"\nisEternal: "+response.attr.isEternal;
				modalService.showSuccess('',message);});
	};
});
</script>
