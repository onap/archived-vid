app.controller("reportSearchController", ['$scope','$rootScope','$http','$timeout',function ($scope,$rootScope,$http,$timeout) { 
    
	$http.get('raptor.htm?action=report.search.execute&r_page=0').then(
		function(result){$scope.searchdData = result.data;		  
	});
	  
	$scope.runReport = function(){
			var searchParams = '';
			if($scope.reportId && $scope.reportId!=''){
				searchParams = '&rep_id='+$scope.reportId+'&rep_id_options='+$scope.operatorRepId.index;
			}
			if($scope.reportName && $scope.reportName!=''){
				searchParams = searchParams+'&rep_name='+$scope.reportName+'&rep_name_options='+$scope.operatorRepName.index;
			}

			console.log('raptor.htm?action=report.search.execute&r_page='+(paginationOptions.pageNumber-1)+searchParams);
			$http.get('raptor.htm?action=report.search.execute&r_page='+(paginationOptions.pageNumber-1)+searchParams).then(
					function(result){$scope.searchdData = result.data;		  
				});
			//quantum/raptor.htm?action=report.search.execute&rep_id=1000&rep_id_options=2&rep_name=cross&rep_name_options=2
	};


	var paginationOptions = {
		    pageNumber: 1,
		    pageSize: 5,
		    sort: null
	};
	  
	$scope.gridOptions = {
	  paginationPageSizes: [5],
	  paginationPageSize: 5,
	  useExternalPagination: true,
	  columnDefs: [],
	  data: [],
	  enableGridMenu: true,
	  enableSelectAll: true,
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
			      $scope.runReport();
			    });
			  }
			};


	var getPage = function() {
		$scope.gridOptions.columnDefs = [];
		$scope.searchdData.columns[0].forEach(function(entry) {
			if(entry.columnTitle=='Run'){
				$scope.gridOptions.columnDefs.push({ displayName: entry.columnTitle, field: entry.columnId+'==drillDownLink', enableSorting: false,
				cellTemplate: '<div class="ui-grid-cell-contents"><a ng-href="#/report_run/{{COL_FIELD.substr(39)}}" class="icon-play" style="font-size:20px;"></a></div>'
				});
			} else if(entry.columnTitle=='Edit'){
				$scope.gridOptions.columnDefs.push({ displayName: entry.columnTitle, field: entry.columnId+'==drillDownLink', enableSorting: false,
					cellTemplate: '<div class="ui-grid-cell-contents"><a ng-href="{{COL_FIELD}}" class="icon-edit" style="font-size:20px;"></a></div>'
				});
			} else if(entry.columnTitle=='Delete'){
				$scope.gridOptions.columnDefs.push({ displayName: entry.columnTitle, field: entry.columnId+'==drillDownLink', enableSorting: false,
					cellTemplate: '<div class="ui-grid-cell-contents"><a href=""  ng-click="grid.appScope.deleteReport(COL_FIELD,row)" class="icon-trash" style="font-size:20px;"></a></div>'
				});
			}  else if(entry.columnTitle=='Copy'){
				$scope.gridOptions.columnDefs.push({ displayName: entry.columnTitle, field: entry.columnId+'==drillDownLink', enableSorting: false,
					cellTemplate: '<div class="ui-grid-cell-contents"><a ng-href="{{COL_FIELD}}" class="icon-plans" style="font-size:20px;"></a></div>'
				});
			} else if(entry.columnTitle=='Schedule'){
				//$scope.gridOptions.columnDefs.push({ displayName: entry.columnTitle, field: entry.columnId+'==drillDownLink',
					//  cellTemplate: '<div class="ui-grid-cell-contents"><a ng-href="{{COL_FIELD}}" class="full-linear-icon-calendar" style="font-size:20px;font-weight: bold;"></a></div>'
				//});
			} else if(entry.columnTitle=='No'){
			} else {
					$scope.gridOptions.columnDefs.push({ displayName: entry.columnTitle, field: entry.columnId+'==displayValue'});
			}
		});
		$scope.gridOptions.data = $scope.searchdData.rows[0];
		$scope.gridOptions.paginationPageSizes= [$scope.searchdData.metaReport.pageSize];
		$scope.gridOptions.paginationPageSize= $scope.searchdData.metaReport.pageSize;
		$scope.gridOptions.totalItems = $scope.searchdData.metaReport.totalSize;
	};

	$scope.$watch("searchdData",function(newValue,oldValue) {
		if($scope.searchdData){
			getPage();
		}
	});

	$scope.operatorsRepId = [{index: 0, value: 'Equal To', title: 'Equal To', alias:'Equal To'},
	                 {index: 1, value: 'Less Than', title: 'Less Than', alias:'Less Than'},
	                 {index: 2, value: 'Greater Than', title: 'Greater Than', alias:'Greater Than'}];
	$scope.operatorsRepName = [{index: 0, value: 'Starts With', title: 'Starts With', alias:'Starts With'},
                        {index:1, value: 'Ends With', title: 'Ends With', alias:'Ends With'},
                        {index: 2, value: 'Contains', title: 'Contains', alias:'Contains'}];
	
	$scope.deleteReport = function(reportDeleteUrl,row) {
		if (confirm("Please confirm: Are you sure you want to delete report #" + reportDeleteUrl.substr(41) + "?")) {
			$http.get(reportDeleteUrl).then(
					function(result){
						if(result.data=='deleted:true'){
							var index = $scope.gridOptions.data.indexOf(row.entity);
							$scope.gridOptions.data.splice(index, 1);
							alert("Report deleted.");
						} else {
							alert("Report not deleted.");
						}
					}
				);
		}
	};

	$timeout(function() {
		$rootScope.isViewRendering = false;
		});


}]);
