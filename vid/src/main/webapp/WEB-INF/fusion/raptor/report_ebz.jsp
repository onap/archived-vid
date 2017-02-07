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
<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/ebz_header/header.css">
<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/ebz_header/portal_ebz_header.css">
<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/sandbox/styles/style.css" >


<script src= "app/fusion/external/ebz/angular_js/angular.js"></script> 
<script src= "app/fusion/external/ebz/angular_js/angular-route.min.js"></script>
<script src= "app/fusion/external/ebz/angular_js/angular-sanitize.js"></script>
<script src= "app/fusion/external/ebz/angular_js/angular-cookies.js"></script>
<script src="app/fusion/external/ebz/angular_js/angular-touch.js"></script>
<script src="app/fusion/external/ebz/angular_js/angular-animate.js"></script>

<script src= "app/fusion/external/ebz/angular_js/gestures.js"></script>
<script src="static/js/jquery-1.10.2.js"></script>
<script src="app/fusion/scripts/modalService.js"></script>
<script src="static/js/jquery.mask.min.js" type="text/javascript"></script>
<script src="static/js/jquery-ui.js" type="text/javascript"></script>
<script src="app/fusion/external/ebz/sandbox/att-abs-tpls.js" type="text/javascript"></script>
<script src="static/fusion/js/att_angular_gridster/ui-gridster-tpls.js"></script>
<script src="static/fusion/js/att_angular_gridster/angular-gridster.js"></script>
<script src= "app/fusion/external/ebz/angular_js/checklist-model.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/lodash.js/0.10.0/lodash.min.js"></script>
<script src="app/fusion/external/angular-ui/ui-bootstrap-tpls-1.1.2.min.js"></script>
<script src="app/fusion/scripts/services/userInfoService.js"></script>
<script src="app/fusion/scripts/services/leftMenuService.js"></script>


<script src="static/fusion/raptor/ebz/dynamicform.js"></script>
<script src="static/fusion/raptor/ebz/multiselect.js"></script>
<script src="static/fusion/raptor/ebz/report_search.js"></script>
<script src="static/fusion/raptor/ebz/report_run.js"></script>
<script src="static/fusion/raptor/ebz/quick_links.js"></script>

<script src="static/fusion/raptor/uigrid/vfs_fonts.js"></script>
<script src="static/fusion/raptor/uigrid/ui-grid.js"></script>

<script src="static/fusion/raptor/ebz/report_chart_wizard.js"></script>


<script src="app/fusion/scripts/controllers/modelpopupController.js"></script>

<script src="static/fusion/raptor/ebz/date_time_picker.js"></script>
<script src="static/fusion/raptor/ebz/moment.js"></script>
<link rel="stylesheet" href="static/fusion/raptor/ebz/date_time_picker.css"/>


<link rel="stylesheet" href="static/fusion/raptor/uigrid/ui-grid.css" type="text/css">

<div ng-controller="reportMainController">
	<div ng-if='isViewRendering || isDataLoading' style="font-size:50px;color:#2ca02c">Loading...</div>
	<div ng-view style="min-height: 400px;"></div>
</div>
<div ng-include src="app/fusion/scripts/view-models/profile-page/popup_modal.html"></div>

<style>
#accBar .att-accordion__heading span{
	font-weight:bold;
	position:relative;
	margin:-10px;
}
#accTimeChart .att-accordion__heading span{
	font-weight:bold;
	position:relative;
	margin:-10px;
}
#accFlexTimeChart .att-accordion__heading span{
	font-weight:bold;
	position:relative;
	margin:-10px;
}
#accCommonOptions .att-accordion__heading span{
	font-weight:bold;
	position:relative;
	margin:-10px;
}

#additionalOptions .att-accordion__heading span{
	font-weight:bold;
	position:relative;
	margin:-10px;
}



</style>
<script>
angular.module('abs').requires.push('quantum', 'ngAnimate', 'ngTouch', 'ngRoute', 'ui.grid',
		'ui.grid.pagination','ui.grid.resizeColumns', 
		'ui.grid.pinning');
app.config(['$routeProvider',
                  function($routeProvider) {
                    $routeProvider.
                      when('/report_search', {
                	templateUrl: 'static/fusion/raptor/ebz/report_search.html',
                	controller: 'reportSearchController'
                      }).
                      when('/report_run/:reportUrlParams*', {
                      	templateUrl: 'static/fusion/raptor/ebz/report_run.html',
                      	controller: 'reportRunController'
                      }).
                      when('/report_chart_wizard/:reportId', {
                        templateUrl: 'static/fusion/raptor/ebz/report_chart_wizard.html',
                        controller: 'ChartController'
                      }).
                      otherwise({
                	redirectTo: '/report_search'
                      });
                }]);

app.factory('redirectInterceptor',['$q','$location','$window','$rootScope', function($q,$location,$window,$rootScope){
    return  {
    	'request':function(config){
    		$rootScope.isDataLoading = true;
    		return config;
        },
    	'response':function(response,config){
    		if(typeof response.data === 'string' && response.data=="session has timed out for user") {
                $window.location.href = 'login.htm';
                return $q.reject(response);
            }else{
        		//console.log('Inside response else ');
            	$rootScope.isDataLoading = false;
                return response;
            }
        }
    }
}]);

app.config(['$httpProvider',function($httpProvider) {
    $httpProvider.interceptors.push('redirectInterceptor');
}]);

app.controller("reportMainController", [ '$scope', '$rootScope', '$parse', 'Grid', function($scope,$rootScope,$parse,Grid) {
	$rootScope.isViewRendering = true;
	$rootScope.$on('$routeChangeStart', function() {
		$rootScope.isViewRendering = true;
		});
	
	Grid.prototype.getCellValue = function getCellValue(row, col){
		 if(col.field.indexOf('==')>-1){
			 var customField = col.field.split('==');
			 var obj = row.entity.filter(function(d){if(d.columnId==customField[0]) return true; });
			 if(obj.length>0){
			   return obj[0].searchresultField[customField[1]];
			 }
		 }
		 if ( typeof(row.entity[ '$$' + col.uid ]) !== 'undefined' ) {
		   return row.entity[ '$$' + col.uid].rendered;
		 } else if (this.options.flatEntityAccess && typeof(col.field) !== 'undefined' ){
		   return row.entity[col.field];
		 } else {
		   if (!col.cellValueGetterCache) {
		     col.cellValueGetterCache = $parse(row.getEntityQualifiedColField(col));
		   }
		   return col.cellValueGetterCache(row);
		 }
	};

}]);
</script>
