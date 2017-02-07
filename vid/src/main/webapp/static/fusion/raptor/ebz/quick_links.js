'use strict';
angular.module('quantum').directive('quickLinks', ['$http','$window', function($http,$window){
	return {
	restrict: 'E',
	scope:{ 
		ngModel: '='
		},
	template: "<div att-links-list class='att-accordion'>" +
 				"<a  ng-repeat='quickLink in quickLinks' class='att-accordion__group' att-links-list-item href='' ng-click='openLink(quickLink.reportURL)'>{{quickLink.reportName}}<span ng-show='quickLink.showDescr'> - {{quickLink.reportDescr}}</span></a>" +
 				"<a  ng-repeat='quickLink in ngModel' class='att-accordion__group' att-links-list-item href='' ng-click='openLink(quickLink.reportURL)'>{{quickLink.reportName}}<span ng-show='quickLink.showDescr'> - {{quickLink.reportDescr}}</span></a>" +
	 		"</div>",
	link: function($scope,element,attr){
		   		$scope.ngDisplayType=attr.ngDisplayType;
		   		$scope.popUpWindowSize = "top=" + ((screen.height*.2)/2) + ",left=" + ((screen.width*.05)/2) + ",width="+(screen.width-(screen.width*.06))+",height="+(screen.height-(screen.height*.25));

		   		$scope.openLink = function(url){
		   			if($scope.ngDisplayType){
		   				if($scope.ngDisplayType=='1'){
				   			$window.open(url,'_self');
			   			}else if($scope.ngDisplayType=='2'){
				   			$window.open(url, '_blank');
			   			}else if($scope.ngDisplayType=='3'){
				   	        $window.open(url, '', $scope.popUpWindowSize);
			   			}
			   		}
		   		};

		   		$http.get('raptor.htm?action=quicklinks.json&quick_links_menu_id='+attr.ngMenuId).then(function(result){
		   			$scope.quickLinks=result.data;
		   		});
	   	}
	}; 
}]);