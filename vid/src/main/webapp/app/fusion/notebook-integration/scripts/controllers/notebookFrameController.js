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
app.controller('notebookFrameController', function ($scope,$location,$window,$http,$routeParams) {
	
	//alert($location.search(1, $scope.additionalqueryParams));
	//var nid = $routeParams.nid;
	//var qprms = $routeParams.qprms;
	//var value = $routeParams.value;
	//console.log('check id ');
	var nid = $window.location.search.substr($window.location.search.indexOf("=")+1);
	console.log('nid',nid);
	//console.log('qprms',qprms);
	
	if ($window.location.search.substr($window.location.search.indexOf("=")+1)) {
		$scope.queryParams = $window.location.search;
		
		if ($window.location.search.indexOf("&")!=-1) {
			$scope.notebookparam = $window.location.search.substring($window.location.search.indexOf("?")+1,$window.location.search.indexOf("&"));
			$scope.additionalqueryParams = JSON.parse('{"' + decodeURI($scope.queryParams.substr($scope.queryParams.indexOf("&")+1).replace(/&/g, "\",\"").replace(/=/g,"\":\"")) + '"}');
			console.log('Additional parameters present');
		}
		else {
			$scope.notebookparam = $window.location.search.substr($window.location.search.indexOf("?")+1);
			console.log('Additional parameters absent');
		}
		console.log('add parameters',$scope.additionalqueryParams);
	//	$scope.notebookid = $scope.notebookparam.substring(0,$scope.notebookparam.indexOf("="));
		$scope.notebookvalue = $scope.notebookparam.substr($scope.notebookparam.indexOf("=")+1);
		//$scope.postData = $window.location.search.substr($window.location.search.indexOf("=")+1);
		console.log('Notebook value present ',$scope.notebookvalue);
	}
	else {
		$scope.notebookvalue = '833c0a69ec1433fbb2f8752af733cf0e';
		console.log('Notebook value absent ',$scope.notebookvalue);
	}
	
	
	
	$http({method:'POST', url:'rNotebookFE/authCr', data: $scope.notebookvalue, params:{'qparams' : $scope.additionalqueryParams}}).success(function(data, status) {
        console.log('Data received', data);
        console.log('Status ', status);
        document.getElementById('itestframe').src = data;
   
	})
	
	
	
});
