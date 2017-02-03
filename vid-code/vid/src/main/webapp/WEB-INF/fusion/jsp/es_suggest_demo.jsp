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
<!-- <link rel="stylesheet" type="text/css" href="app/fusion/elasticsearch/styles/styles.css" > -->
<script>
app.controller('elasticSearchController',['$scope','$http',function ($scope,$http) { 
	$scope.submenuId = "";
	$scope.isNew="";
 	$scope.viewPerPage = 20;
    $scope.currentPage = 2;
    $scope.totalPage;	
    $scope.searchCategory = "";
    $scope.searchString = "";
 	$scope.tableData=[];
 	$scope.searchText='';
 	$scope.flagCount=0;
 	function escapeRegExp(str) {
 		return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|\&]/g, " ");
 	}
 	$scope.test = function(){
 		$scope.searchText=$scope.searchText;
 		var sendData ={
 			"data" : escapeRegExp($scope.searchText),
 			"size" : "20",
 			"fuzzy" : "true",
 			"resultname" : "custsuggest"
		};
		
 		if($scope.flagCount<3 || $scope.searchText.length==0){
 			$scope.flagCount++;
 			$http.get('es_suggest/' + encodeURIComponent(JSON.stringify(sendData))).
 				then(function(response){
 					var data = JSON.parse(response.data.result);
 					$scope.tableData = data.custsuggest[0].options;
 				})
 				.catch(function(data,status){
					console.log("Error: " + status + " : " + data); 					
 				})
 				.finally(function() {
 					$scope.flagCount--;
 				});
 		}
 	}
 	$scope.searchText='';
}]);
</script>

<div style="margin-left:20px;" ng-app="eSearchApp">
 	<div style="margin-top:20px; margin-bottom:20px;"><span class="heading1">Elastic Search - </span><span class="heading2">Corporate Location Data System</span></div>
	<div class="demoElas" ng-controller="elasticSearchController">
		<div class="form-field" align="center">
           <input type="text" placeholder="What are you looking for?" class="search__large-iconform" ng-model="searchText" ng-change="test()">
           <i class="icon-search">&nbsp;</i>
        </div>
        
        <table att-table table-data="tableData" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">
		     <thead att-table-row type="header">
		        <tr>
		         	<th att-table-header>Customer Name</th>  
		            <th att-table-header>Phone</th>  
		            <th att-table-header>Street</th>  
		            <th att-table-header>City</th>  
		            <th att-table-header>State</th>  
		            <th att-table-header>ZIP</th>
		            <th att-table-header>CLLI</th>  
		        </tr>
			 </thead>
		    <tbody att-table-row type="body" row-repeat="options in tableData">
		        <tr>
		     	  	<td att-table-body >{{options.text}}</td>
					<td att-table-body >{{options.payload.tn}}</td>
					<td att-table-body >{{options.payload.addr}}</td>
					<td att-table-body >{{options.payload.city}}</td>
					<td att-table-body >{{options.payload.st}}</td>
					<td att-table-body >{{options.payload.zip}}</td>	
					<td att-table-body >{{options.payload.clli}}</td>			
				</tr>     
		    </tbody>
		</table>
	</div>
</div>
