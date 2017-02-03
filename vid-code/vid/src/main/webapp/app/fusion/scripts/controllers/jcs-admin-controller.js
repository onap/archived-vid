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
app.controller('cacheAdminController', function ($scope,$interval,$http,$modal,modalService,AdminService){
	
    AdminService.getCacheRegions().then(function(data){
		
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.regions =$scope.data;
  		//$scope.resetMenu();
	
	},function(error){
		console.log("failed");
		reloadPageOnce();
	});
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
