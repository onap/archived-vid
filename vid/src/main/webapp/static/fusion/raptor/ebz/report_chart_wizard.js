app.controller('ChartController', function ($scope, $rootScope, $timeout, $window, $http, $routeParams,modalService) {
	//$scope.test="1223";
	//alert($scope.chartType.value);
	
	$scope.populateChrtWzdFields = function() {
	 	
		$scope.reportRunJson = {};
	//	console.log($routeParams.reportId); 
		$http.get("raptor.htm?action=chart.json&c_master="+$routeParams.reportId).then(function (response) {
			//$scope.myData = JSON.stringify(response.data);  
		//	response.data.rangeAxisList[0].rangeColorJSON = {};  
			$scope.reportRunJson = response.data;
			//  $scope.chrtheight =  $scope.reportRunJson.height;
		//	console.log(JSON.stringify($scope.reportRunJson));
		/*	  if ($scope.reportRunJson.showTitle==false)
				  $scope.reportRunJson.showTitle="false";
			  else 
				  $scope.reportRunJson.showTitle="true"; */
			  
			  console.log($scope.reportRunJson);
			  
			  
			  
			  
			  
			  //Set chart type
			  if ($scope.reportRunJson.chartTypeJSON) {
				  var chrtTypeValue = $scope.reportRunJson.chartTypeJSON.value;
					 for(var i = 0; i < $scope.chartTypes.length; i++) {
						    var obj = $scope.chartTypes[i];
						    //console.log(obj.id);
						    if ($scope.chartTypes[i].value==chrtTypeValue) {
						    	$scope.reportRunJson.chartTypeJSON.index=$scope.chartTypes[i].index;
						    	$scope.reportRunJson.chartTypeJSON.title=$scope.chartTypes[i].title;
						    }
	
					}
			  }	 
				 
				//Set Domain Axis
				 if ($scope.reportRunJson.domainAxisJSON) {
					  var domaninAxisValue = $scope.reportRunJson.domainAxisJSON.value;
						 for(var i = 0; i < $scope.reportRunJson.chartColumnJSONList.length; i++) {
							   if ($scope.reportRunJson.chartColumnJSONList[i].value==domaninAxisValue) {
							    	$scope.reportRunJson.domainAxisJSON.index=$scope.reportRunJson.chartColumnJSONList[i].index;
							    	$scope.reportRunJson.domainAxisJSON.title=$scope.reportRunJson.chartColumnJSONList[i].title;
							    }
		
						}
				  }
				 
				//Set Category
				 if ($scope.reportRunJson.categoryAxisJSON) {
					  var categoryAxisValue = $scope.reportRunJson.categoryAxisJSON.value;
						 for(var i = 0; i < $scope.reportRunJson.chartColumnJSONList.length; i++) {
							   if ($scope.reportRunJson.chartColumnJSONList[i].value==categoryAxisValue) {
							    	$scope.reportRunJson.categoryAxisJSON.index=$scope.reportRunJson.chartColumnJSONList[i].index;
							    	$scope.reportRunJson.categoryAxisJSON.title=$scope.reportRunJson.chartColumnJSONList[i].title;
							    }
		
						}
				  }
				 
				//Set range axis label
		if ($scope.reportRunJson.rangeAxisList) {
			for(var j = 0; j < $scope.reportRunJson.rangeAxisList.length; j++) { 
				 
				 if ($scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON) {
					  var rangeAxisLabelValue = $scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON.value;
						 for(var i = 0; i < $scope.reportRunJson.chartColumnJSONList.length; i++) {
							   if ($scope.reportRunJson.chartColumnJSONList[i].value==rangeAxisLabelValue) {
							    	$scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON.index=$scope.reportRunJson.chartColumnJSONList[i].index;
							    	$scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON.title=$scope.reportRunJson.chartColumnJSONList[i].title;
							    }
		
						}
				  }
			}
		}
				
				
				//set range linetype
				if ($scope.reportRunJson.rangeAxisList) {
					for(var j = 0; j < $scope.reportRunJson.rangeAxisList.length; j++) { 
						 if ($scope.reportRunJson.rangeAxisList[j].rangeLineTypeJSON != null && $scope.reportRunJson.rangeAxisList[j].rangeLineTypeJSON.value != ""
							 && $scope.reportRunJson.rangeAxisList[j].rangeLineTypeJSON.value != null) {
							 var lineTypeValue = $scope.reportRunJson.rangeAxisList[j].rangeLineTypeJSON.value;
							 for(var i = 0; i < $scope.lineTypes.length; i++) {
								    if ($scope.lineTypes[i].value==lineTypeValue) {
								    	$scope.reportRunJson.rangeAxisList[j].rangeLineTypeJSON.index=$scope.lineTypes[i].index;
								    	$scope.reportRunJson.rangeAxisList[j].rangeLineTypeJSON.title=$scope.lineTypes[i].title;
								    }
						
							}
						} else {
							$scope.reportRunJson.rangeAxisList[j].rangeLineTypeJSON = null;
						}  
					}
				
				}
				//set range color
				if ($scope.reportRunJson.rangeAxisList) {
					for(var j = 0; j < $scope.reportRunJson.rangeAxisList.length; j++) { 
						 if ($scope.reportRunJson.rangeAxisList[j].rangeColorJSON != null && $scope.reportRunJson.rangeAxisList[j].rangeColorJSON.value != ""
							 && $scope.reportRunJson.rangeAxisList[j].rangeColorJSON.value != null) {
							 var colorValue = $scope.reportRunJson.rangeAxisList[j].rangeColorJSON.value; 
				
							 for(var i = 0; i < $scope.rangeColors.length; i++) {
								 
								 if ($scope.rangeColors[i].value==colorValue) {
									 $scope.reportRunJson.rangeAxisList[j].rangeColorJSON.index=$scope.rangeColors[i].index;
									 $scope.reportRunJson.rangeAxisList[j].rangeColorJSON.title=$scope.rangeColors[i].title;
								 }
							 }
						 }else {
							 $scope.reportRunJson.rangeAxisList[j].rangeColorJSON = null; 
						 }
					}
				}
				 
				 
			  
			  
			  /*	  var data = $.param({
				  json: JSON.stringify($scope.reportRunJson)
		        });
			  console.log(data);
			  $http.post("/ecomp-sdk-app/testraptorchart", JSON.stringify($scope.reportRunJson)).success(function(data, status) {
		            console.log(data);
		            console.log(status);
		            
		        })*/
			  
	 	  });
		
		
	/*	$scope.chrtwidth =  $scope.reportRunJson.width;
	 	
		$scope.chrtheight =  $scope.reportRunJson.height;
	 	$scope.title="false";
	 	$scope.domainAxes = $scope.reportRunJson.chartColumnJSONList;
	 	$scope.categories = $scope.reportRunJson.chartColumnJSONList;
	 	$scope.rangeAxes = $scope.reportRunJson.chartColumnJSONList; */
	 	
	 //	$scope.Color = $scope.rangeColors;
	 //	$scope.lineType = $scope.lineTypes;
	 //	$scope.praxis = $scope.reportRunJson.primaryAxisLabel;
	 //	$scope.secaxis = $scope.reportRunJson.secondaryAxisLabel;
	 //	$scope.raxisminrange = $scope.reportRunJson.minRange;
	 //	$scope.raxismaxrange = $scope.reportRunJson.maxRange;
	 //	$scope.reportRunJson.legendangle = "up_45";
	 	$scope.legend = "true";
	// 	$scope.animate = "true";
	// 	$scope.topmargin = $scope.reportRunJson.topMargin;
	// 	$scope.bottommargin = $scope.reportRunJson.bottomMargin;
	// 	$scope.leftmargin = $scope.reportRunJson.leftMargin;
	// 	$scope.rightmargin = $scope.reportRunJson.rightMargin;
	// 	$scope.tt1=false;
	
	
	
	}
	
	$scope.saveChartData = function() {
		//console.log(JSON.stringify($scope.reportRunJson));
	
		//Converting string variables to numbers
		$scope.reportRunJson.commonChartOptions.rightMargin = Number($scope.reportRunJson.commonChartOptions.rightMargin);
		$scope.reportRunJson.commonChartOptions.topMargin = Number($scope.reportRunJson.commonChartOptions.topMargin);
		$scope.reportRunJson.commonChartOptions.bottomMargin = Number($scope.reportRunJson.commonChartOptions.bottomMargin);
		$scope.reportRunJson.commonChartOptions.leftMargin = Number($scope.reportRunJson.commonChartOptions.leftMargin);
	
		//Concatenate range Y axis with range label with pipe delimiter
		/* if ($scope.reportRunJson.rangeAxisList) {
				
				for(var j = 0; j < $scope.reportRunJson.rangeAxisList.length; j++) { 
					
					if ($scope.reportRunJson.rangeAxisList[j].rangeYAxis) {
						if ($scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON != "" && $scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON != null) {
							$scope.reportRunJson.rangeAxisList[j].rangeYAxis = 
								$scope.reportRunJson.rangeAxisList[j].rangeYAxis + "|"+ $scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON.value;
							
							//document.getElementById('yaxs').value = $scope.reportRunJson.rangeAxisList[j].rangeYAxis.substring(0,$scope.reportRunJson.rangeAxisList[j].rangeYAxis.indexOf('|'));
						}
					} else {
						if ($scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON != "" && $scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON != null)
							$scope.reportRunJson.rangeAxisList[j].rangeYAxis = "|"+ $scope.reportRunJson.rangeAxisList[j].rangeAxisLabelJSON.value;
					
					}
				}
				
				
		}*/	
 
		if ($scope.reportRunJson.categoryAxisJSON == "") { 
			console.log('Inside categoryAxisJSON value'); 
			$scope.reportRunJson.categoryAxisJSON = {}; 
			$scope.reportRunJson.categoryAxisJSON.value = -1; 
			console.log('$scope.reportRunJson.categoryAxisJSON',$scope.reportRunJson.categoryAxisJSON);  
		}
		
			
		$http.post("save_chart", JSON.stringify($scope.reportRunJson)).success(function(data, status) {
	          //  console.log(data);
	          //  console.log(status);
			 $scope.successSubmit=true;
			//  modalService.showSuccess("Success","Chart Wizard details have been successfully submitted.");
	          //      saveProfile();
	        
			})
			
			
	/*	$http.post("/ecomp-sdk-app/testraptorchart", $scope.reportRunJson).then(function(response) {
			console.log(response.data);
			console.log(response.status);
	
			});*/
	}
	
	//$scope.samplearray = ["abc","test"];
	
	
	$scope.addRangeAxisRow = function (rangeaxisitem) {
		
		
		
		
		$scope.reportRunJson.rangeAxisList.push({  
	         
	  /*       "rangeColor":{  
	            "index":0,
	            "value":rangeaxisitem.rangeColor.value,
	            "title":rangeaxisitem.rangeColor.title
	            
	         },
	         "rangeLineType":{  
	            "index":0,
	            "value":rangeaxisitem.rangeLineType.value,
	            "title":rangeaxisitem.rangeLineType.title
	            
	         },
	         "rangeaxisLabel":{  
	            "index":0,
	            "value":rangeaxisitem.rangeaxisLabel.value,
	            "title":rangeaxisitem.rangeaxisLabel.value
	            
	         },
	         "YAxis":"",
	         "chartTitle":""*/
	      });
	//	alert($scope.reportRunJson.rangeAxisList.length);
		console.log($scope.reportRunJson.rangeAxisList);
	
	};

	$scope.removeRangeAxisRow = function (index) {
		$scope.reportRunJson.rangeAxisList.splice(index, 1);
		//console.log($scope.hardCodeReport.rangeAxisList)
	};
	
	
	
	/*$scope.saveProfile = function() {
		  var uuu = "profile/saveProfile?profile_id=" + $scope.profileId;;
		  var postData={profile: $scope.profile,
				        selectedCountry:$scope.selectedCountry!=null?$scope.selectedCountry.value:"",
				        selectedState:$scope.stateList.selected!=null?$scope.stateList.selected.value:"",
				        selectedTimeZone:$scope.selectedTimeZone!=null?$scope.selectedTimeZone.value:""
		               };
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			modalService.showSuccess("Success","Update Successful.");
			 },
			 error : function(data){
				 modalService.showFailure("Fail","Error while saving.");
			 }
	  	  });
	};*/
	
	
 /*	$scope.actionClicked = function(){
        
		if ($scope.chartType=="Bar Chart") {
        	$scope.tt1=true;
        } else if ($scope.chartType=="Time Series/Area Chart") {
        	$scope.tt2=true;
        }else 
        //	$scope.tt3=true;
    };*/
	
	$scope.init = function () {
		//	console.time("In init");
		if ($scope) { 
			$scope.populateChrtWzdFields();
		//	$scope.saveChartData();
		}
			
	};
	
	
	/*$scope.accgroups =  [
		                 {
		                     title: "Accordion Item #1",
		                     content:"Unit test me bro! Proin eu quam malesuada, accumsan velit eu, tristique orci. Donec neque nisl, dignissim ut hendrerit sit amet, tempor id erat. Donec fermentum commodo semper. Sed lectus odio, egestas non volutpat eu, venenatis eu turpis. Donec eget fringilla lorem. Fusce sit amet semper velit. ",
		                     open: false
		                 },
		                 {
		                     title: "Accordion Item #2",
		                     content: "Lint me too! Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eleifend aliquam eros quis fringilla. Integer eu justo turpis. Nam mauris augue, posuere interdum dignissim sed, tempor sit amet neque. In nec tortor id nibh sollicitudin aliquam sit amet ac augue",
		                     open: false
		                 },
		                 {
		                     title: "Accordion Item #3",
		                     content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eleifend aliquam eros quis fringilla. Integer eu justo turpis. Nam mauris augue, posuere interdum dignissim sed, tempor sit amet neque. In nec tortor id nibh sollicitudin aliquam sit amet ac augue",
		                     open: false
		                 }
		             ];*/
	
	$scope.domainItems = [{title:"Domain Axis1", content:"Test1", open: false},{title:"Domain Axis2", content:"Test2", open: false}];
	
	$scope.chartTypes = [        
	                     {index: 0, value: 'BarChart3D', title: 'Bar Chart'},
	                     {index: 1, value: 'TimeSeriesChart', title: 'Time Series/Area Chart'},
	                     {index: 2, value: 'PieChart', title: 'Pie Chart'},
	                     {index: 3, value: 'AnnotationChart', title: 'Annotation Chart'},
	                     {index: 4, value: 'FlexTimeChart', title: 'Flexible Time Chart'}
	 ];
	 
	 /* $scope.domainAxes = [        
		                     {index: 0, value: 'scenario_name', title: 'scenario_name'},
		                     {index: 1, value: 'total_traffic_in_PB', title: 'Total Traffic in PB'},
		                     {index: 2, value: 'Avg Utilization Day', title: 'Avg Utilization Day'}
		                     
		 ];*/
	 
	 $scope.categories = [        
		                     {index: 0, value: 'scenario_name', title: 'scenario_name'},
		                     {index: 1, value: 'total_traffic_in_PB', title: 'Total Traffic in PB'},
		                     {index: 2, value: 'Avg Utilization Day', title: 'Avg Utilization Day'}
		                     
		 ];
	 
	 $scope.rangeColors = [        
		                     
		                     {index: 0, value: "#1f77b4",title: "Dodger Blue"},  						
							 {index: 1, value: "#ff7f0e",title: "Vivid orange"},							
							 {index: 2, value: "#2ca02c",title: "Forest Green"},							
							 {index: 3, value: "#8c864b",title: "Greenish Red"},							
							 {index: 4, value: "#9467bd",title: "Desaturated violet"},					
							 {index: 5, value: "#8c564b",title: "Dark moderate red"},					
							 {index: 6, value: "#e377c2",title: "Soft pink"},							
							 {index: 7, value: "#7f7f7f",title: "Dark gray"},							
							 {index: 8, value: "#bcbd22",title: "Strong yellow"},						
							 {index: 9, value: "#17becf",title: "Strong cyan"},							
							 {index: 10, value: "#dc143c",title: "Vivid red"},							
							 {index: 11, value: "#800080",title: "Dark magenta"},							
							 {index: 12, value: "#0000FF",title: "Blue"},									
							 {index: 13, value: "#008000",title: "Dark lime green"},						
							 {index: 14, value: "#D2691E",title: "Reddish Orange"},					
							 {index: 15, value: "#FF0000",title: "Red"},								
							 {index: 16, value: "#000000",title: "Black"},								
							 {index: 17, value: "#DB7093",title: "Pink"},								
							 {index: 18, value: "#FF00FF",title: "Pure Magenta"},						
							 {index: 19, value: "#7B68EE",title: "Soft blue"},							
							 {index: 20, value: "#1f77b6",title: "Strong blue"},						
							 {index: 21, value: "#9edae5",title: "Very soft cyan"},						
							 {index: 22, value: "#393b79",title: "Dark Blue"},							
							 {index: 23, value: "#5254a3",title: "Dark moderate Blue"},					
							 {index: 24, value: "#6b6ecf",title: "Slightly desaturated blue"},			
							 {index: 25, value: "#9c9ede",title: "Very soft blue"},			
							 {index: 26, value: "#637939",title: "Dark Green"},							
							 {index: 27, value: "#8ca252",title: "Dark moderate green"},				
							 {index: 28, value: "#b5cf6b",title: "Slightly desaturated green"},			
							 {index: 29, value: "#cedb9c",title: "Desaturated Green"},	
     
							 /* Old Colors  */
							 {index: 30, value: "#00FFFF",title: "Aqua"},
							 {index: 31, value: "#000000",title: "Black"},
							 {index: 32, value: "#0000FF",title: "Blue"},
							 {index: 33, value: "#FF00FF",title: "Fuchsia"},
							 {index: 34, value: "#808080",title: "Gray"},
							 {index: 35, value: "#008000",title: "Green"},
							 {index: 36, value: "#00FF00",title: "Lime"},
							 {index: 37, value: "#800000",title: "Maroon"},
							 {index: 38, value: "#000080",title: "Navy"},
							 {index: 39, value: "#808000",title: "Olive"},
							 {index: 40, value: "#FF9900",title: "Orange"},
							 {index: 41, value: "#800080",title: "Purple"},
							 {index: 42, value: "#FF0000",title: "Red"},
							 {index: 43, value: "#C0C0C0",title: "Silver"},
							 {index: 44, value: "#008080",title: "Teal"},
							 {index: 45, value: "#FFFFFF",title: "White"},
							 {index: 46, value: "#FFFF00",title: "Yellow"}
						];	
    
    
	 $scope.lineTypes = [        
	                     {index: 0, value: 'default', title: 'Default'},
	                     {index: 1, value: 'dotted_lines', title: 'Dotted Lines'},
	                     {index: 2, value: 'dashed_lines', title: 'Dashed Lines'}
	                     
	 ]; 
	 
	 
	 $scope.hardCodeReport= {
		   "reportID":"3356",
		   "reportName":"Test: Line Chart",
		   "reportDescr":"",
		   "reportTitle":"",
		   "reportSubTitle":"",
		   "formFieldList":[

		   ],
		   "chartColumnJSONList":[
		      {
		         "index":0,
		         "value":"tr0",
		         "title":"traffic_date",
		         "$$hashKey":"056"
		      },
		      {
		         "index":1,
		         "value":"ut1",
		         "title":"util_perc",
		         "$$hashKey":"057"
		      }
		   ],
		   "formfield_comments":null,
		   "totalRows":0,
		   "chartSqlWhole":"SELECT traffic_date tr0, traffic_date tr0_1,util_perc ut1, 1 FROM portal.demo_util_chart   ORDER BY 1",
		   "chartAvailable":true,
		   "chartType":{"index": "", "value": "Bar Chart", "title": ""},
		   "width":"700",
		   "height":"420",
		   "animation":false,
		   "rotateLabels":"90",
		   "staggerLabels":false,
		   "showTitle":"false",
		   "domainAxis":{
			      "index":0,
			      "value":"tr0",
			      "title":"traffic_date",
			      "$$hashKey":"11H"
			   },

			   "categoryAxis":{
				      "index":1,
				      "value":"ut1",
				      "title":"util_perc",
				      "$$hashKey":"11I"
				   },

		   "hasCategoryAxis":false,
		   "rangeAxisList":[
		      {
		         
		         "rangeColor":{
		        	"index":"",
		            "value":"#bcbd22",
		            "title":""
		            
		         },
		         
		         "rangeLineType":{
		            "index":"",
		            "value":"dotted_lines",
		            "title":""
		            
		            
		         },
		         
		         "rangeAxisLabel":{
		            "index":0,
		            "value":"tr0",
		            "title":"traffic_date",
		            "$$hashKey":"056"
		         },
		         "YAxis":"10",
		         "chartTitle":"test"
		      },
		      {
			         
			         "rangeColor":{
			        	"index":"",
			            "value":"#2ca02c",
			            "title":""
			            
			         },
			         
			         "rangeLineType":{
			            "index":"",
			            "value":"dashed_lines",
			            "title":""
			            
			            
			         },
			         
			         "rangeAxisLabel":{
			            "index":0,
			            "value":"tr0",
			            "title":"traffic_date",
			            "$$hashKey":"056"
			         },
			         "YAxis":"10",
			         "chartTitle":"test"
			      }
		      
		     
		   ],
		  
		   "primaryAxisLabel":"testlabel",
		   "secondaryAxisLabel":"testseclabel",
		   "minRange":"10",
		   "maxRange":"20",
		   "topMargin":"6",
		   "bottomMargin":"5",
		   "leftMargin":"4",
		   "rightMargin":"3"
		};
	 
	 var colorValue = $scope.hardCodeReport.rangeAxisList[0].rangeColor.value;
	 
	 //console.log($scope.hardCodeReport.chartType);
	 
	 // prefill range color
	/* for(var i = 0; i < $scope.rangeColors.length; i++) {
		    var obj = $scope.rangeColors[i];
		    //console.log(obj.id);
		    if ($scope.rangeColors[i].value==colorValue) {
		    	$scope.hardCodeReport.rangeAxisList[0].rangeColor.index=$scope.rangeColors[i].index;
		   	 	$scope.hardCodeReport.rangeAxisList[0].rangeColor.title=$scope.rangeColors[i].title;
		    }

		}*/
	 
	 //prefill chart type
	 
//	 console.log($scope.hardCodeReport.chartType);
	 
//	 console.log($scope.hardCodeReport.chartType);
	 
	// console.log($scope.hardCodeReport.rangeAxisList);
	 //prefill rangle line type
	/* for(var j = 0; j < $scope.hardCodeReport.rangeAxisList.length; j++) { 
	 
		 var lineTypeValue = $scope.hardCodeReport.rangeAxisList[j].rangeLineType.value;
		 for(var i = 0; i < $scope.lineTypes.length; i++) {
			    var obj = $scope.lineTypes[i];
			    //console.log(obj.id);
			    if ($scope.lineTypes[i].value==lineTypeValue) {
			    	$scope.hardCodeReport.rangeAxisList[j].rangeLineType.index=$scope.lineTypes[i].index;
			    	$scope.hardCodeReport.rangeAxisList[j].rangeLineType.title=$scope.lineTypes[i].title;
			    }
	
			}
	 }*/
//	 console.log($scope.hardCodeReport.rangeAxisList);
	 
	
	 
	 
	 // console.log($scope.hardCodeReport.rangeAxisList[0].rangeColor);
	/* $scope.hardCodeReport.rangeAxisList[0].rangeColor.index=4;
	 $scope.hardCodeReport.rangeAxisList[0].rangeColor.title="Desaturated violet";*/
	 
	 
	 //console.log($scope.hardCodeReport);
	 

	 
	 
	 
	 /*$scope.reportRunJson = 
	 	{
			   "reportID":"3356",
			   "reportName":"Test: Line Chart",
			   "reportDescr":"",
			   "reportTitle":"",
			   "reportSubTitle":"",
			   "formFieldList":[

			   ],
			   "chartColumnJSONList":[
			      {
			         "index":0,
			         "value":"tr0",
			         "title":"traffic_date"
			      },
			      {
			         "index":1,
			         "value":"ut1",
			         "title":"util_perc"
			      }
			   ],
			   "formfield_comments":null,
			   "totalRows":0,
			   "chartSqlWhole":"SELECT traffic_date tr0, traffic_date tr0_1,util_perc ut1, 1 FROM portal.demo_util_chart   ORDER BY 1",
			   "chartAvailable":true,
			   "chartType":"TimeSeriesChart",
			   "width":"700",
			   "height":"420",
			   "animation":false,
			   "rotateLabels":"90",
			   "staggerLabels":false,
			   "showTitle":false,
			   "domainAxis":"tr0",
			   "categoryAxis":null,
			   "hasCategoryAxis":false,
			   "rangeAxisList":[
			      {
			         "YAxis":"",
			         "chartTitle":""
			      }
			   ],
			   "primaryAxisLabel":"",
			   "secondaryAxisLabel":"",
			   "minRange":"",
			   "maxRange":"",
			   "topMargin":"",
			   "bottomMargin":"",
			   "leftMargin":"",
			   "rightMargin":"3"
			}; */
	 	$timeout(function() {
			$rootScope.isViewRendering = false;
			});


});

app.directive('onlyDigits', function () {

    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, ngModel) {
            if (!ngModel) return;
            ngModel.$parsers.unshift(function (inputValue) {
                var digits = inputValue.split('').filter(function (s) { return (!isNaN(s) && s != ' '); }).join('');
                ngModel.$viewValue = digits;
                ngModel.$render();
                return digits;
            });
        }
    };
});

app.directive('onlyCharacters', function () {

    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, ngModel) {
            if (!ngModel) return;
            ngModel.$parsers.unshift(function (inputValue) {
                var digits = inputValue.split('').filter(function (s) { return (isNaN(s) && s != ' '); }).join('');
                ngModel.$viewValue = digits;
                ngModel.$render();
                return digits;
            });
        }
    };
});



