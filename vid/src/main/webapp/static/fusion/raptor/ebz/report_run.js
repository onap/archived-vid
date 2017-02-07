app.controller("reportRunController", ['$scope','$rootScope','$routeParams','$http','dateFilter', '$window', '$timeout', 'rowSorter',
                                       function ($scope,$rootScope,$routeParams,$http,dateFilter,$window,$timeout,rowSorter) { 
	$scope.dateformat = "MM/dd/yyyy";
	$scope.datetimeformat = "MM/dd/yyyy hh:mm a";
	$scope.showFormFields =  false;
	$scope.showGrid =  false;
	$scope.showChart =  false;
	$scope.showBackButton = false;
	$scope.reportData = {};
	$scope.reportData.allowEdit = false;
	$scope.formFieldSelectedValues = {};
	$scope.showFormFieldIds = false;
	
	$scope.isInProgress = true;
	
	if($routeParams.reportUrlParams.indexOf("parent___params===")>-1) {
		$scope.showBackButton = true;
		$scope.parentReportUrlParams = $routeParams.reportUrlParams.substring($routeParams.reportUrlParams.indexOf("parent___params===")+18);
		$scope.currentReportUrlParams = $routeParams.reportUrlParams.substring(0,$routeParams.reportUrlParams.indexOf("parent___params==="));
	} else {
		$scope.currentReportUrlParams = $routeParams.reportUrlParams;
	}
	console.log($routeParams.reportUrlParams);
	var parseQueryString = function( queryString ) {
	    var params = {}, queries, temp, i, l;
	    // Split into key/value pairs
	    queries = queryString.split("&");
	    // Convert the array of strings into an object
	    for ( i = 0, l = queries.length; i < l; i++ ) {
	        temp = queries[i].split('=');
	        //console.log(temp[0]);
	        //console.log(temp[0] != "refresh");
	        if(temp[0] && temp[0] != "refresh")
	        	params[temp[0]] = temp[1];
	    }
	    return params;
	};
	
	var convertQueryString = function(queryString) {
		var keys = ""; var str = "";
		keys = Object.keys(queryString);
		//console.log(keys);
		for ( i = 0, l = keys.length; i < l; i++ ) {
			str += keys[i]+"="+queryString[keys[i]] + "&";
			
		}
		return str;
		//queryString = 
	}
	
	
	
	
	$scope.urlParams = parseQueryString($scope.currentReportUrlParams);
	
	$scope.reportChartURL = 'report#/report_chart_wizard/'+$scope.urlParams.c_master;
	
	$scope.reportEditURL = 'report_wizard.htm?action=report.edit&c_master='+$scope.urlParams.c_master;

	
	$http.get('raptor.htm?action=report.run.container&'+$scope.currentReportUrlParams).then(
		function(response){
			console.log(response);
			$scope.isInProgress = false;
			$scope.reportData = response.data;
	      	if(!$scope.urlParams.hideChart && $scope.reportData.chartAvailable && $scope.reportData.totalRows>1){
	      		console.log('raptor.htm?action=chart.run&'+convertQueryString($scope.urlParams));
		    	$http.get('raptor.htm?action=chart.run&'+convertQueryString($scope.urlParams)).then(
		      		 	function(response){
		      		 	  $scope.showChart =  true;
		      			  document.getElementById('chartiframe').contentWindow.document.write(response.data);
		      		 	  document.getElementById('chartiframe').contentWindow.document.close();
		      			});
	    	}
	
	        if($scope.reportData.displayForm && $scope.reportData.formFieldList && $scope.reportData.formFieldList.length>0 && !$scope.urlParams.hideFormFields){
			  $scope.showFormFields = true;
		    }
		});
    $scope.getFormFieldSelectedValuesAsURL = function(){
    	var formFieldsUrl = '';
    	$scope.reportData.formFieldList.forEach(function(formField) {
	   		if(formField.fieldType==='LIST_BOX') {
	   			if($scope.formFieldSelectedValues && $scope.formFieldSelectedValues[formField.fieldId] && $scope.formFieldSelectedValues[formField.fieldId].value != '') {
	   				formFieldsUrl = formFieldsUrl+formField.fieldId+'='+$scope.formFieldSelectedValues[formField.fieldId].value+'&';
	   			}
	   		} else if(formField.fieldType==='LIST_MULTI_SELECT') {
	   			if($scope.formFieldSelectedValues[formField.fieldId].length >0) {
	   				for (var i = 0; i < $scope.formFieldSelectedValues[formField.fieldId].length; i++) {
	   					if($scope.formFieldSelectedValues[formField.fieldId][i].defaultValue){
		   				  formFieldsUrl = formFieldsUrl+formField.fieldId+'='+$scope.formFieldSelectedValues[formField.fieldId][i].value+'&';
	   					}
	   			    }
	   			}
	   		} else if((formField.fieldType === 'text' || formField.fieldType === 'TEXT') && formField.validationType === 'DATE'){
	   			formFieldsUrl = formFieldsUrl+formField.fieldId+'='+dateFilter($scope.formFieldSelectedValues[formField.fieldId],$scope.dateformat)+'&';
	   		} else if((formField.fieldType === 'text' || formField.fieldType === 'TEXT') && formField.validationType === 'TIMESTAMP_MIN'){
	   			formFieldsUrl = formFieldsUrl+formField.fieldId+'='+dateFilter($scope.formFieldSelectedValues[formField.fieldId],$scope.datetimeformat)+'&';
	   		} else if((formField.fieldType === 'text' || formField.fieldType === 'TEXT') && $scope.formFieldSelectedValues[formField.fieldId] && $scope.formFieldSelectedValues[formField.fieldId] != ''){
	   			formFieldsUrl = formFieldsUrl+formField.fieldId+'='+$scope.formFieldSelectedValues[formField.fieldId]+'&';
	   		}
    	});
    	return formFieldsUrl;

    }

    $scope.runReport = function(pagination){
    	
    	var formFieldsUrl = $scope.getFormFieldSelectedValuesAsURL();
		console.log("pagination");
    	if(!pagination) {
    		console.log("refreshed ...");
    		$scope.gridOptions.pageNumber = 1;
  		  $scope.gridOptions.paginationPageSizes= [$scope.reportData.pageSize];
		  $scope.gridOptions.paginationPageSize= $scope.reportData.pageSize;
		  if($scope.reportData.totalRows<14){
			  $scope.gridHeight = ($scope.reportData.totalRows+7)*30+'px';
		  } else{
			  $scope.gridHeight = '400px';
		  }
		  $scope.gridOptions.totalItems = $scope.reportData.totalRows;
		  $scope.gridOptions.data= $scope.reportData.reportDataRows;
		  $scope.gridOptions.exporterPdfHeader.text= $scope.reportData.reportName;

    	}
        $scope.currentReportUrlParams = 'c_master='+$scope.urlParams.c_master+'&'+formFieldsUrl+'&display_content=Y&r_page='+(paginationOptions.pageNumber-1);
    	console.log('raptor.htm?action=report.run.container&c_master='+$scope.urlParams.c_master+'&'+formFieldsUrl+'refresh=Y&display_content=Y&r_page='+(paginationOptions.pageNumber-1));
    	  $http.get('raptor.htm?action=report.run.container&c_master='+$scope.urlParams.c_master+'&'+formFieldsUrl+'refresh=Y&display_content=Y&r_page='+(paginationOptions.pageNumber-1)).then(
    		function(response){
		    	$scope.reportData = response.data;
		    	if($scope.reportData.errormessage) {
		    		document.getElementById('errorDiv').innerHTML = $scope.reportData.errormessage;
		    		console.log(document.getElementById('errorDiv').innerHtml);
		    		console.log($scope.reportData.errormessage);
		    	}		   
		    if(!pagination) {	
		      if(!$scope.urlParams.hideChart && $scope.reportData.chartAvailable && $scope.reportData.totalRows>1){
		      		console.log('raptor.htm?action=chart.run&c_master='+$scope.urlParams.c_master+'&'+formFieldsUrl+'display_content=Y&r_page='+(paginationOptions.pageNumber-1));
			    	$http.get('raptor.htm?action=chart.run&c_master='+$scope.urlParams.c_master+'&'+formFieldsUrl+'display_content=Y&r_page='+(paginationOptions.pageNumber-1)).then(
			      		 	function(response) {
			      		 		console.log(response.data);
			      		 		$scope.showChart =  true;
			      		 		document.getElementById('chartiframe').contentWindow.document.write(response.data);
			      		 		document.getElementById('chartiframe').contentWindow.document.close();
			      			});
		      	} else {
			      		 		$scope.showChart =  false;
		      	}
		    }
		        if($scope.reportData.displayForm && $scope.reportData.formFieldList && $scope.reportData.formFieldList.length>0 && !$scope.urlParams.hideFormFields){
		        	$scope.showFormFields = true;
				} else {
					$scope.showFormFields = false;
				}
    		});
    };

	  var paginationOptions = {
			    pageNumber: 1,
			    pageSize: 5,
			    sort: null
	  };
	  
	  $scope.gridOptions = {
			  	pageNumber: 1,
			  	sort : null,
			    paginationPageSizes: [5],
			    paginationPageSize: 5,
			    useExternalPagination: true,
				columnDefs: [],
				data: [],
			    enableGridMenu: true,
			    enableSelectAll: true,
				gridMenuCustomItems : [
   					{	title : 'All Reports',
						action : function($event) {
							$window.open('report.htm','_self');
							},	order : 210	},
   					{	title : 'Edit Report',
						action : function($event) {
							$window.open($scope.reportEditURL,'_self');
							},	order : 211	},
					{	title : 'Export All data as Excel 2007',
						action : function($event) {
							$window.open('raptor.htm?c_master='+$scope.reportData.reportID+'&r_action=report.download.excel2007.session','_self');
							},	order : 212	},
					{	title : 'Export All data as Excel',
						action : function($event) {
							$window.open('raptor.htm?c_master='+$scope.reportData.reportID+'&r_action=report.download.excel.session','_self');
							},	order : 213 },
					{	title : 'Export All data as CSV',
						action : function($event) {
							$window.open('raptor.htm?c_master='+$scope.reportData.reportID+'&r_action=report.download.csv.session','_self');
							},	order : 214	},
					{	title : 'Export All data as PDF',
						action : function($event) {
							$window.open('raptor.htm?c_master='+$scope.reportData.reportID+'&r_action=report.download.pdf.session','_self');
							},	order : 215	} ],
			    exporterMenuPdf: false,
	    		exporterMenuCsv: false,
			    exporterCsvFilename: 'myFile.csv',
			    exporterPdfDefaultStyle: {fontSize: 9},
			    exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
			    exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
			    exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
			    exporterPdfFooter: function ( currentPage, pageCount ) {
			      return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
			    },
			    exporterPdfCustomFormatter: function ( docDefinition ) {
			      docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
			      docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
			      return docDefinition;
			    },
			    exporterPdfOrientation: 'portrait',
			    exporterPdfPageSize: 'LETTER',
			    exporterPdfMaxGridWidth: 500,
			    exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),
			    onRegisterApi: function(gridApi) {
			        $scope.gridApi = gridApi;
			        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
			          paginationOptions.pageNumber = newPage;
			          paginationOptions.pageSize = pageSize;
			          $scope.runReport(true);
			        });
			      }
			  };
 
	  $scope.uiGridRefresh = function(){
		  var columnDefsArray = [];
		  var columnFreezeEndColumn = $scope.reportData.colIdxTobeFreezed;
		  var doColumnNeedToFreeze = false;
		  if(columnFreezeEndColumn && columnFreezeEndColumn.length>0) {
			  doColumnNeedToFreeze = true;
		  }
		  $scope.reportData.reportDataColumns.forEach(function(entry) {
			  var tempColumnDef = { displayName: entry.columnTitle, field: entry.colId, enableSorting: entry.sortable,
					  sortingAlgorithm: function(a, b) {
				         return rowSorter.sortAlpha(a.displayValue, b.displayValue);
					  },
					  cellTemplate: '<div  class="ui-grid-cell-contents"  style="text-align:{{COL_FIELD.alignment}};" title="TOOLTIP">   '+
					  	 '  <div ng-if="!COL_FIELD.drillDownURL || COL_FIELD.drillDownURL==\'\'">{{COL_FIELD.displayValue}}</div>' +
		                 '  <a ng-if="COL_FIELD.drillDownURL && COL_FIELD.drillDownURL!=\'\'" ng-href="{{COL_FIELD.drillDownURL}}&parent___params==={{grid.appScope.currentReportUrlParams}}" >{{COL_FIELD.displayValue}}</a>' +
						  '</div>'};
			  if(entry.columnWidth && entry.columnWidth!='null' && entry.columnWidth!='pxpx' && entry.columnWidth!='nullpx' && entry.columnWidth!='nullpxpx'){
				  tempColumnDef['minWidth'] = entry.columnWidth.substring(0, entry.columnWidth.length - 2);
			  } else {
				  tempColumnDef['minWidth'] = '100';
			  }
			  if(doColumnNeedToFreeze) {
				  tempColumnDef['pinnedLeft']= true;
				  if(columnFreezeEndColumn === entry.colId){
					  doColumnNeedToFreeze = false;
				  }
			  }
			  columnDefsArray.push(tempColumnDef);
		  }); 
	  
		  $scope.gridOptions.paginationPageSizes= [$scope.reportData.pageSize];
		  $scope.gridOptions.paginationPageSize= $scope.reportData.pageSize;
		  if($scope.reportData.totalRows<14){
			  $scope.gridHeight = ($scope.reportData.totalRows+5)*30+'px';
		  }else{
			  $scope.gridHeight = '400px';
		  }
		  $scope.gridOptions.totalItems = $scope.reportData.totalRows;
		  $scope.gridOptions.columnDefs= columnDefsArray;
		  $scope.gridOptions.data= $scope.reportData.reportDataRows;
		  $scope.gridOptions.exporterPdfHeader.text= $scope.reportData.reportName;
	  };

		$scope.$watch("reportData",function(newValue,oldValue) {
	  	 if(!$scope.urlParams.hideGrid){
		  if($scope.reportData){
	    	if($scope.reportData.displayData && $scope.reportData.reportDataColumns){
	  			$scope.showGrid =  true;
		        $scope.uiGridRefresh();
	  		}
	      }
		 }
		});

		$scope.triggerOtherFormFields = function(){
  			console.log("report_run");
	    	var formFieldsUrl = $scope.getFormFieldSelectedValuesAsURL();
	    	$http.get('raptor.htm?action=report.formfields.run.container&c_master='+$scope.reportData.reportID+'&'+formFieldsUrl).then(
	      		 	function(response){
	    		    	$scope.reportData = response.data;
	      			});
		};
		$timeout(function() {
			$rootScope.isViewRendering = false;
			});
}]);
