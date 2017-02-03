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
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="static/fusion/sample/css/scribble.css" />

<link rel="stylesheet" type="text/css" href="static/fusion/sample/css/welcome.css" />

<script src="static/js/jquery-1.10.2.js"></script>
<script src="app/fusion/external/angular-ui/ui-bootstrap-tpls-1.1.2.min.js"></script>

<!-- <script src="static/fusion/js/jquery.resize.js"></script> -->
<link rel="stylesheet" href="static/fusion/css/att_angular_gridster/ui-gridster.css"/>
<link rel="stylesheet" href="static/fusion/css/att_angular_gridster/sandbox-gridster.css"/>
<script src="static/fusion/js/att_angular_gridster/ui-gridster-tpls.js"></script>
<script src="static/fusion/js/att_angular_gridster/angular-gridster.js"></script>
<script src="app/fusion/external/ebz/sandbox/att-abs-tpls.js" type="text/javascript"></script>

<!--for line Chart and  Area Chart-->
<script src="static/fusion/d3/js/d3.v3.min.js"></script>
<script src="static/fusion/d3/js/nv.d3.min.js"></script> 
<script src="static/fusion/d3/js/models/axis.min.js"></script> 

<!-- Style for line Chart and area chart  -->
<link rel="stylesheet" type="text/css" href="static/fusion/d3/css/nv.d3.css">


<script type="text/javascript" src="static/fusion/sample/js/FusionCharts.js"></script>			<!--  Charts -->
<script type="text/javascript" src="static/fusion/sample/js/charts.js"></script>				<!--  Charts -->
<script type="text/javascript" src="static/fusion/sample/js/scribble.js"></script>				<!-- Scribble -->

<!--  Data for Line and Area Charts -->
<script>
historicalBarChart=[{"type":"line","key":"AP_CPU","yAxis":"1","values":[{"x":1388552400000,"y":2.13},{"x":1388552400000,"y":5.0},{"x":1388552400000,"y":2.36},{"x":1388552400000,"y":10.0},{"x":1391230800000,"y":3.15},{"x":1391230800000,"y":2.88},{"x":1391230800000,"y":3.0},{"x":1391230800000,"y":4.0},{"x":1393650000000,"y":8.0},{"x":1393650000000,"y":3.93},{"x":1393650000000,"y":4.27},{"x":1393650000000,"y":4.0},{"x":1396324800000,"y":4.25},{"x":1396324800000,"y":5.35},{"x":1396324800000,"y":5.92},{"x":1396324800000,"y":12.0},{"x":1398916800000,"y":5.55},{"x":1398916800000,"y":4.89},{"x":1398916800000,"y":5.01},{"x":1398916800000,"y":3.27},{"x":1401595200000,"y":6.27},{"x":1401595200000,"y":9.17},{"x":1401595200000,"y":9.31},{"x":1401595200000,"y":6.07},{"x":1404187200000,"y":8.37},{"x":1404187200000,"y":8.11},{"x":1404187200000,"y":8.84},{"x":1404187200000,"y":8.93},{"x":1406865600000,"y":11.79},{"x":1406865600000,"y":12.22},{"x":1406865600000,"y":12.6},{"x":1406865600000,"y":11.61},{"x":1409544000000,"y":15.27},{"x":1409544000000,"y":19.09},{"x":1409544000000,"y":16.09},{"x":1409544000000,"y":18.66},{"x":1412136000000,"y":18.4},{"x":1412136000000,"y":22.05},{"x":1412136000000,"y":21.66},{"x":1412136000000,"y":19.04},{"x":1414814400000,"y":19.13},{"x":1414814400000,"y":19.61},{"x":1414814400000,"y":17.61},{"x":1414814400000,"y":17.5},{"x":1417410000000,"y":19.0},{"x":1417410000000,"y":15.73},{"x":1420088400000,"y":9.67},{"x":1420088400000,"y":15.19},{"x":1420088400000,"y":15.02},{"x":1420088400000,"y":9.62333333333333},{"x":1422766800000,"y":16.95},{"x":1422766800000,"y":14.29},{"x":1425186000000,"y":12.9},{"x":1425186000000,"y":16.1166666666667}]},{"type":"line","key":"ROUTER_CPU","yAxis":"1","values":[{"x":1388552400000,"y":3.0},{"x":1388552400000,"y":4.0},{"x":1388552400000,"y":4.89},{"x":1388552400000,"y":7.0},{"x":1391230800000,"y":4.57},{"x":1391230800000,"y":4.0},{"x":1391230800000,"y":4.0},{"x":1391230800000,"y":7.0},{"x":1393650000000,"y":7.0},{"x":1393650000000,"y":4.18},{"x":1393650000000,"y":5.0},{"x":1393650000000,"y":5.0},{"x":1396324800000,"y":5.0},{"x":1396324800000,"y":5.0},{"x":1396324800000,"y":5.06},{"x":1396324800000,"y":6.0},{"x":1398916800000,"y":5.0},{"x":1398916800000,"y":5.0},{"x":1398916800000,"y":5.0},{"x":1398916800000,"y":5.0},{"x":1401595200000,"y":6.0},{"x":1401595200000,"y":6.09},{"x":1401595200000,"y":6.0},{"x":1401595200000,"y":6.0},{"x":1404187200000,"y":6.36},{"x":1404187200000,"y":7.0},{"x":1404187200000,"y":7.0},{"x":1404187200000,"y":7.0},{"x":1406865600000,"y":7.0},{"x":1406865600000,"y":7.02},{"x":1406865600000,"y":7.24},{"x":1406865600000,"y":7.0},{"x":1409544000000,"y":8.23},{"x":1409544000000,"y":8.11},{"x":1409544000000,"y":8.12},{"x":1409544000000,"y":8.03},{"x":1412136000000,"y":9.0},{"x":1412136000000,"y":8.93},{"x":1412136000000,"y":8.57},{"x":1412136000000,"y":9.0},{"x":1414814400000,"y":5.97},{"x":1414814400000,"y":6.0},{"x":1414814400000,"y":9.0},{"x":1414814400000,"y":9.0},{"x":1417410000000,"y":9.0},{"x":1417410000000,"y":8.78},{"x":1420088400000,"y":3.0},{"x":1420088400000,"y":2.01},{"x":1420088400000,"y":3.0},{"x":1420088400000,"y":3.01},{"x":1422766800000,"y":2.67},{"x":1422766800000,"y":2.0},{"x":1425186000000,"y":2.8},{"x":1425186000000,"y":3.63333333333333}]},{"type":"line","key":"SCTP_CPU","yAxis":"1","values":[{"x":1388552400000,"y":7.0},{"x":1388552400000,"y":10.0},{"x":1388552400000,"y":8.27},{"x":1388552400000,"y":8.0},{"x":1391230800000,"y":10.02},{"x":1391230800000,"y":8.04},{"x":1391230800000,"y":9.0},{"x":1391230800000,"y":10.0},{"x":1393650000000,"y":12.0},{"x":1393650000000,"y":10.04},{"x":1393650000000,"y":11.16},{"x":1393650000000,"y":10.0},{"x":1396324800000,"y":10.7},{"x":1396324800000,"y":13.31},{"x":1396324800000,"y":12.73},{"x":1396324800000,"y":9.0},{"x":1398916800000,"y":12.41},{"x":1398916800000,"y":11.95},{"x":1398916800000,"y":12.82},{"x":1398916800000,"y":9.58},{"x":1401595200000,"y":11.28},{"x":1401595200000,"y":14.01},{"x":1401595200000,"y":14.63},{"x":1401595200000,"y":11.83},{"x":1404187200000,"y":14.06},{"x":1404187200000,"y":13.96},{"x":1404187200000,"y":14.66},{"x":1404187200000,"y":14.36},{"x":1406865600000,"y":16.6},{"x":1406865600000,"y":16.95},{"x":1406865600000,"y":17.11},{"x":1406865600000,"y":15.94},{"x":1409544000000,"y":19.86},{"x":1409544000000,"y":22.97},{"x":1409544000000,"y":21.56},{"x":1409544000000,"y":24.55},{"x":1412136000000,"y":22.66},{"x":1412136000000,"y":26.79},{"x":1412136000000,"y":26.54},{"x":1412136000000,"y":25.35},{"x":1414814400000,"y":21.0},{"x":1414814400000,"y":20.35},{"x":1414814400000,"y":21.93},{"x":1414814400000,"y":23.63},{"x":1417410000000,"y":24.0},{"x":1417410000000,"y":21.43},{"x":1420088400000,"y":12.63},{"x":1420088400000,"y":25.14},{"x":1420088400000,"y":21.85},{"x":1420088400000,"y":12.5766666666667},{"x":1422766800000,"y":26.3},{"x":1422766800000,"y":24.4},{"x":1425186000000,"y":23.3833333333333},{"x":1425186000000,"y":24.5833333333333}]},{"type":"line","key":"DP_CPU","yAxis":"1","values":[{"x":1388552400000,"y":2.0},{"x":1388552400000,"y":5.0},{"x":1388552400000,"y":2.17},{"x":1388552400000,"y":2.0},{"x":1391230800000,"y":3.01},{"x":1391230800000,"y":2.56},{"x":1391230800000,"y":3.0},{"x":1391230800000,"y":9.0},{"x":1393650000000,"y":10.0},{"x":1393650000000,"y":3.64},{"x":1393650000000,"y":4.06},{"x":1393650000000,"y":4.0},{"x":1396324800000,"y":4.04},{"x":1396324800000,"y":5.11},{"x":1396324800000,"y":5.9},{"x":1396324800000,"y":8.0},{"x":1398916800000,"y":5.08},{"x":1398916800000,"y":4.65},{"x":1398916800000,"y":4.74},{"x":1398916800000,"y":2.98},{"x":1401595200000,"y":6.13},{"x":1401595200000,"y":8.98},{"x":1401595200000,"y":9.22},{"x":1401595200000,"y":5.84},{"x":1404187200000,"y":8.12},{"x":1404187200000,"y":7.89},{"x":1404187200000,"y":8.41},{"x":1404187200000,"y":8.47},{"x":1406865600000,"y":11.06},{"x":1406865600000,"y":11.84},{"x":1406865600000,"y":11.92},{"x":1406865600000,"y":10.8},{"x":1409544000000,"y":14.58},{"x":1409544000000,"y":18.39},{"x":1409544000000,"y":15.5},{"x":1409544000000,"y":18.33},{"x":1412136000000,"y":18.01},{"x":1412136000000,"y":21.3},{"x":1412136000000,"y":21.11},{"x":1412136000000,"y":18.37},{"x":1414814400000,"y":18.59},{"x":1414814400000,"y":18.81},{"x":1414814400000,"y":17.13},{"x":1414814400000,"y":16.92},{"x":1417410000000,"y":18.0},{"x":1417410000000,"y":15.18},{"x":1420088400000,"y":9.16},{"x":1420088400000,"y":12.13},{"x":1420088400000,"y":11.76},{"x":1420088400000,"y":9.31},{"x":1422766800000,"y":13.47},{"x":1422766800000,"y":13.41},{"x":1425186000000,"y":12.2333333333333},{"x":1425186000000,"y":12.4}]}];
</script>

	<script>
  		$(function(){
		

			/* area chart and line chart titles */
			d3.select("#areaChart svg").append("text").attr("x", 200).attr("y", 15)
					.attr("text-anchor", "middle").style("font-size", "16px").text(
							"Data Usage in NJ Locations");
			
			d3.select("#lineChart svg").append("text").attr("x", 200).attr("y", 15)
					.attr("text-anchor", "middle").style("font-size", "16px").text(
							"Data Usage in NJ Locations");  
  	});
</script>

<div ng-controller="welcomeController">
	<fmt:message key="general.home" var="title" /> 
	<div>
	  	<span style="font-weight:bold;font-size:11pt;">Welcome ${sessionScope.user.firstName} ${sessionScope.user.lastName}</span>&nbsp;
	(Last Login:&nbsp;<fmt:formatDate value="${sessionScope.user.lastLoginDate}" type="date" pattern="dd MMM yyyy hh:mma zzz" var="lastLogin" /> ${lastLogin})
	</div>
  		
  	<div id="gridDiv" class="center">
        <div class="gridster-container">
            <div att-gridster att-gridster-options='gridsterOpts'>
                <div att-gridster-item='item' ng-repeat="item in standardItems">
                    <div att-gridster-item-header 
                         header-text={{item.headerText}} 
                         sub-header-text={{item.subHeaderText}}>
                            <!--ICON BUTTONS PLACEHOLDER START-->
                            <div class="tileMinMaxBtn" ng-click="toggleMinMax($index,'')">
								<span class="tileMinMaxIcon">
									<i	class="ion-chevron-up" style="color:gray"  ng-show="item.max"></i>
									<i	class="ion-chevron-down" style="color:gray"  ng-hide="item.max"></i>
								</span>
							</div>
                            <!--ICON BUTTONS PLACEHOLDER END-->
                    </div>
               	 	<div att-gridster-item-body >
		                <!--ACTUAL BODY CONTENT START-->
			                <div align="center" style="margin-top:10px;">
			               		<div align="left" ng-if="item.headerText=='Dashboard' && item.max">
			               			<label>&nbsp; Sample Charts</label><BR>
					              	<iframe scrolling="no" frameBorder="0" style="width: 430px; height: 360px;" src="static/fusion/sample/html/wordcloud.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Donut Chart' && item.max">
					              	<iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/donut_d3.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Area Chart' && item.max">
				               		<div id="areaChart">
				               			<div> <svg></svg> </div> 
			               				<script src="static/fusion/sample/html/js/area_chart.min.js"></script>
			               			</div>			               		
					              	<!-- <iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/area_chart.html"></iframe> -->
			               		</div>
			               		<div ng-if="item.headerText=='Pie Chart' && item.max">
					              	<iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/pie_chart.html"></iframe>
			               		</div>
			               		<div ng-if="item.headerText=='Line Chart' && item.max">
				               		<div id="lineChart">
				               			<div> <svg></svg> </div> 
			               				<script src="static/fusion/sample/html/js/line_chart.min.js"></script>
			               			</div>

<!-- 			               			<iframe scrolling="no" frameBorder="0"  style="width: 310px; height: 210px;" src="static/fusion/sample/html/line_chart.html"></iframe> -->
			               		</div>
			               		<div ng-if="item.headerText=='Gauges' && item.max">
					              	<iframe scrolling="no" frameBorder="0"  style="width: 310pxx; height: 210px;" src="static/fusion/sample/html/d3_gauges_demo.html"></iframe>
			               		</div>
			               		
			               		<div align="left" ng-if="item.headerText=='Traffic distribution by day of week' && item.max">
  					    			<div id = "selectedTrafficDay">
 					    				<ul>
					    					<li ng-repeat="Daytab in selectedTrafficDay"
					    					ng-class="{active1:isActiveTab1(Daytab.url)}" 
					    					ng-click="onClickTab1(Daytab)">{{Daytab.title}}</li>
					    				</ul> 
					    				<div id = "SelectedTrafficeDayView">
						    				<div ng-include="currentSelectedDayTab"></div>
					    				</div>
					    				<script type="text/ng-template" id="#Monday">
											<div id="Monday"  align="centers"><img src="static/fusion/sample/images/tunnels/1_mon.png" width=100% height=100% alt="Monday"></div>
										</script>
										<script type="text/ng-template" id="#Tuesday">
											<div id="Tuesday"  align="center"><img src="static/fusion/sample/images/tunnels/2_tue.png" width=100% height=100% alt="Tuesday"></div>
										</script>
										<script type="text/ng-template" id="#Wednesday">
											<div id="Wednesday" align="center"><img src="static/fusion/sample/images/tunnels/3_wed.png" width=100% height=100% alt="Wednesday"></div>
										</script>
										<script type="text/ng-template" id="#Thursday">
											<div id="Thursday" align="center"><img src="static/fusion/sample/images/tunnels/4_thu.png" width=100% height=100% alt="Thursday"></div>
										</script>
										<script type="text/ng-template" id="#Friday">
											<div id="Friday"  align="center"><img src="static/fusion/sample/images/tunnels/5_fri.png" width=100% height=100% alt="Friday"></div>
										</script>
										<script type="text/ng-template" id="#Saturday">
											<div id="Saturday"  align="center"><img src="static/fusion/sample/images/tunnels/6_sat.png" width=100% height=100% alt="Saturday"></div>
										</script>
										<script type="text/ng-template" id="#Sunday">
											<div id="Sunday"  align="center"><img src="static/fusion/sample/images/tunnels/7_sun.png" width=100% height=100% alt="Sunday"></div>
										</script>
							        </div>
			               		</div>

			               		<div align="left" ng-if="item.headerText=='Busy hour traffic analysis by day of week' && item.max">
 					    			<div id = "BusyHourTraffic">
					    				<ul>
					    					<li ng-repeat="TrafficTab in BusyHourTraffic"
					    					ng-class="{active2:isActiveTab2(TrafficTab.url)}" 
					    					ng-click="onClickTab2(TrafficTab)">{{TrafficTab.title}}</li>
					    				</ul>
					    				<div id = "BusyHourTrafficView">
						    				<div ng-include="currentSelectedBusyHourTraffic"></div>
					    				</div>
					    				<script type="text/ng-template" id="#Incoming">
							        		<div id="Incoming" align="left"><img src="static/fusion/sample/images/tunnels/BH_DLSTX_IN.png" width=100% height=100%></div>
							        	</script>
										<script type="text/ng-template" id="#Outgoing">
											<div id="Outgoing" align="center"><img src="static/fusion/sample/images/tunnels/BH_DLSTX_OUT.png" width=100% height=100%></div>
										</script>
										<script type="text/ng-template" id="#Default">
											<div id="Default" align="center"><img src="static/fusion/sample/images/tunnels/BH_Nat_Def.png" width=100% height=100%></div>
										</script>
										<script type="text/ng-template" id="#Priority">
											<div id="Priority" align="center"><img src="static/fusion/sample/images/tunnels/BH_Nat_Priority.png" width=100% height=100%></div>
										</script>
										<script type="text/ng-template" id="#BHNational">
											<div id="BHNational" align="center"><img src="static/fusion/sample/images/tunnels/BH_Nat.png" width=100% height=100%></div>
										</script>
									</div> 
			               		</div>

			               		<div align="left" ng-if="item.headerText=='Additional Samples' && item.max">
					              	<label>&nbsp;Quick Links</label>				          		 
						    		<table att-table >
							    
										  
										  <tr>
										    <td att-table-body width="90%" ><a href="http://jquery.com" target="_blank">JQuery</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										
										<!--   <tr>
										    <td att-table-body width="90%" ><a href="sample_heat_map.htm" target="">Heat Map</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr> -->
										  <tr>
										  	<td att-table-body width="90%" ><a href="leafletMap.htm" target="">Animated Map</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
										  <tr>
										  	<td att-table-body width="90%" ><a href="collaborate_list.htm">Chat Session</a></td>
											<td att-table-body width="10%">
										    	<a ng-click="removeRole();" ><img src="static/fusion/sample/images/deleteicon.gif"></a>
										     </td>
										  </tr>
									</table>
			               		</div>
			               		<div ng-if="item.headerText=='Sticky Notes' && item.max">
					              	<div style="width:100%; height:400px" id="scribble-pad"><pre id="scribble" contenteditable="true" onkeyup="storeUserScribble(this.id);"></pre></div>
			               		</div>

							<div ng-if="item.headerText=='Service Configuration' && item.max">
								<accordion close-others="true" css="att-accordion">
								          <accordion-group heading="Service Configuration" is-open="group11.open"> 
										        <iframe style="overflow:auto" frameBorder="0" align="center" width="100%" height="400px"  src="static/fusion/sample/org_chart/example.html" ></iframe> 	
								          </accordion-group>
								          <accordion-group heading="VSP Service Configuration" is-open="group12.open">             	
						              			<iframe style="overflow:auto" frameBorder="0" align="center" width="100%" height="400px" src="static/fusion/sample/org_chart/example_vsp.html" ></iframe>
								          </accordion-group>
						          </accordion> 	

							</div>

						</div>

						<!--ACTUAL BODY CONTENT END-->
					</div>
					
				</div>
			</div>
		</div>
	</div> <!-- End gridDiv -->

</div>

<script>
	$(document).ready(function() {
		$("#rightIcon").hide();
		$("#leftIcon").show();
	});

	app.controller('welcomeController',function($scope, modalService, $modal) {
						$scope.gridsterOpts = {
							columns : 3, // the width of the grid, in columns
							pushing : true, // whether to push other items out of the way on move or resize
							floating : true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
							width : 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
							colWidth : 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
							rowHeight : 60, // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
							margins : [ 10, 10 ], // the pixel distance between each widget
							outerMargin : true, // whether margins apply to outer edges of the grid
							swapping : true,
							draggable : {
								enabled : true, // whether dragging items is supported
	            			stop: function(event, uiWidget, $element) {$scope.setCookie();} // optional callback fired when item is finished dragging
							}

						};

						/* $scope.gridsterOpts = {
						        columns: 6,
						        width: 'auto',
						        colWidth: '230',
								rowHeight: '120',
								margins: [10, 10],
								outerMargin: true,
								pushing: true,
								floating: true,
						        swapping: true
						        }; */

						$scope.toggleMinMax = function(index, tileName) {
							if (tileName == '') {
								$scope.standardItems[index].max = !$scope.standardItems[index].max;
								if ($scope.standardItems[index].max)
									$scope.standardItems[index].sizeY = $scope.standardItems[index].maxHeight;
								else
									$scope.standardItems[index].sizeY = 0;
							} else {
								$scope.tileTemp = $scope.$eval(tileName);
								var tileMax = $parse(tileName + '.max');
								tileMax.assign($scope,!$scope.$eval(tileName).max);
								var tileSizeY = $parse(tileName + '.sizeY');
								if ($scope.tileTemp.max)
									tileSizeY.assign($scope,$scope.tileTemp.maxHeight);
								else
									tileSizeY.assign($scope, 0);
							}
						};
						
						

						// These map directly to gridsterItem options
						// IMPORTANT: Items should be placed in the grid in the order in which 
						// they should appear.
						// In most cases the sorting should be by row ASC, col ASC
		    			$scope.standardItems = [{
									sizeX : 1,
									sizeY : 8,
									maxHeight : 8,
									row : 0,
									col : 0,
									headerText : 'Dashboard',
									max : false

								},
								{
									sizeX : 1,
									sizeY : 5,
									maxHeight : 5,
									row : 0,
									col : 1,
									headerText : 'Donut Chart',
									max : false

								},
								{
									sizeX : 1,
									sizeY : 5,
									maxHeight : 5,
									row : 0,
									col : 2,
									headerText : 'Area Chart',
									max : false
								},
								{
									sizeX : 1,
									sizeY : 5,
									maxHeight : 5,
									row : 8,
									col : 0,
									headerText : 'Pie Chart',
									max : false
								},
								{
									sizeX : 1,
									sizeY : 5,
									maxHeight : 5,
									row : 8,
									col : 1,
									headerText : 'Line Chart',
									max : false
								},
								{
									sizeX : 1,
									sizeY : 5,
									maxHeight : 5,
									row : 8,
									col : 2,
									headerText : 'Gauges',
									max : false
								},
								{
									sizeX : 1,
									sizeY : 8,
									maxHeight : 8,
									row : 16,
									col : 0,
									headerText : 'Traffic distribution by day of week',
									max : false
								},
								{
									sizeX : 1,
									sizeY : 8,
									maxHeight : 8,
									row : 16,
									col : 1,
									headerText : 'Busy hour traffic analysis by day of week',
									max : false
								}, 
									{
									sizeX : 1,
									sizeY : 6,
									maxHeight : 6,
									row : 24,
									col : 0,
									headerText : 'Additional Samples',
									max : false
								}, 
									{
									sizeX : 1,
									sizeY : 8,
									maxHeight : 8,
									row : 24,
									col : 1,
									headerText : 'Sticky Notes',
									max : false
								}, 
									{
									sizeX : 3,
									sizeY : 10,
									maxHeight : 10,
									row : 32,
									col : 0,
									headerText : 'Service Configuration',
									max : false
								} ];

						$.each($scope.standardItems, function(i, a) {
							$scope.toggleMinMax(i, '');
						});
						$scope.activeTabId = 'Monday';
						//for generic tabs
						
						$scope.selectedTrafficDay = [{
							title : 'Mon',
							url : '#Monday'
						}, {
							title : 'Tue',
							url : '#Tuesday'						
						}, {
							title : 'Wed',
							url : '#Wednesday'						
						}, {
							title : 'Thu',
							url : '#Thursday'						
						}, {
							title : 'Fri',
							url : '#Friday'				
						}, {
							title : 'Sat',
							url : '#Saturday'				
						}, {
							title : 'Sun',
							url : '#Sunday'				
						}];
						
					    $scope.currentSelectedDayTab = '#Monday';

					    $scope.onClickTab1 = function (Daytab) {
					        $scope.currentSelectedDayTab = Daytab.url;
					    }
					    
					    $scope.isActiveTab1 = function(tabUrl) {
					        return tabUrl == $scope.currentSelectedDayTab;
					    }
						
						
						$scope.gTabs = [ {
							title : 'Monday',
							id : 'Monday',
							url : '#Monday',
							selected : true
						}, {
							title : 'Tuesday',
							id : 'Tuesday',
							url : '#Tuesday'							
						}, {
							title : 'Wednesday',
							id : 'Wednesday',
							url : '#Wednesday'
						}, {
							title : 'Thursday',
							id : 'Thursday',
							url : '#Thursday'
						}, {
							title : 'Friday',
							id : 'Friday',
							url : '#Friday'
						}, {
							title : 'Saturday',
							id : 'Saturday',
							url : '#Saturday'
						}, {
							title : 'Sunday',
							id : 'Sunday',
							url : '#Sunday'
						}
					];


					
					
					
					$scope.BusyHourTraffic = [ {
						title : 'BH SNRC DLSTX - Incoming',
						url : '#Incoming'
					}, {
						title : 'BH SNRC DLSTX - Outgoing',
						url : '#Outgoing'
					}, {
						title : 'BH National - Default',
						url : '#Default'
					}, {
						title : 'BH National - Priority',
						url : '#Priority'
					}, {
						title : 'BH National',
						url : '#BHNational'
					}

				];
				
				    $scope.currentSelectedBusyHourTraffic = '#Incoming';

				    $scope.onClickTab2 = function (TrafficTab) {
				        $scope.currentSelectedBusyHourTraffic = TrafficTab.url;
				    }
				    
				    $scope.isActiveTab2 = function(tabUrl) {
				        return tabUrl == $scope.currentSelectedBusyHourTraffic;
				    }

					
						$scope.activeTabId2 = 'Incoming';
						//for generic tabs
						$scope.gTabs2 = [ {
							title : 'BH SNRC DLSTX - Incoming',
							id : 'Incoming',
							url : '#Incoming',
							selected : true
						}, {
							title : 'BH SNRC DLSTX - Outgoing',
							id : 'Outgoing',
							url : '#Outgoing'
						}, {
							title : 'BH National - Default',
							id : 'Default',
							url : '#Default'
						}, {
							title : 'BH National - Priority',
							id : 'Priority',
							url : '#Priority'
						}, {
							title : 'BH National',
							id : 'BHNational',
							url : '#BHNational'
						}
					];

						$scope.activeTabId3 = 'Incoming';
						//for generic tabs
						$scope.gTabs3 = [ {
							title : 'BH SNRC DLSTX - Incoming',
							id: 'Incoming',
							url : '#Incoming',
							selected : true
						}, {
							title : 'BH SNRC DLSTX - Outgoing',
							id : 'Outgoing',
							url : '#Outgoing'
						}, {
							title : 'BH National - Default',
							id : 'Default',
							url : '#Default'
						}, {
							title : 'BH National - Priority',
							id : 'Priority',
							url : '#Priority'
						}, {
							title : 'BH National',
							id : 'BHNational',
							url : '#BHNational'
						}
					];

						/* $scope.$watch('activeTabId', function(newVal) {
							alert(newval);
							$('#'+newval).show();
						}, true); */

						$scope.toggleEastToWest = function() {
							$("#toggle").toggle('slide');
							if ($("#leftIcon").is(":visible")) {
								$("#rightIcon").show();
								$("#leftIcon").hide();
							} 
							else if ($("#rightIcon").is(":visible")) {
								$("#rightIcon").hide();
								$("#leftIcon").show();
							}
						};

						$scope.group1 = {
							open : true
						};
						$scope.group2 = {
							open : true
						};
						$scope.group3 = {
							open : true
						};
						$scope.group4 = {
							open : true
						};
						$scope.group5 = {
							open : true
						};
						$scope.group6 = {
							open : true
						};
						$scope.group7 = {
							open : true
						};
						$scope.group71 = {
							open : true
						};
						$scope.group8 = {
							open : true
						};
						$scope.group9 = {
							open : true
						};
						$scope.group10 = {
							open : true
						};
						$scope.group11 = {
							open : true
						};
						$scope.group12 = {
							open : false
						};
					});
</script>

<!-- Select the Slider control by default -->
  <script>$('input[name=viewer]:eq(1)').click();</script>
