<script src="./app/fusion/external/angular-1.5/angular.min.js"></script>
<script src="./app/fusion/external/angular-1.5/angular-messages.js"></script>
<script src="./app/fusion/external/angular-1.5/angular-touch.js"></script>
<script src="./app/fusion/external/angular-1.5/angular-sanitize.js"></script>
<script src="./app/fusion/external/angular-1.5/angular-route.min.js"></script>
<script src="./app/fusion/external/angular-1.5/angular-cookies.min.js"></script>
<script src="./app/fusion/external/ds2/js/b2b-angular/b2b-library.min.js"></script>
<script src="./app/fusion/external/ds2/js/digital-ng-library/digital-design-library.js"></script>
<script src="./app/fusion/scripts/DS2-services/leftMenuServiceDS2.js"></script>
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/b2b-angular/b2b-angular.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/b2b-angular/b2b-angular.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-accordion.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-bootstrap-datepicker.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-cc-input-field.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/extras/ds2-tooltip.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/extras/x-tabs-pills.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-accordion.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-bootstrap-datepicker.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-c2c.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-cc-input-field.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-filmstrip.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-filters.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-legacynav-fix.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-marquee.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-pagination.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-popover.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-progressbar.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ds2-tooltip.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/global.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/digital-design-library.css">

<link rel="stylesheet" type="text/css" href="./app/fusion/external/ds2/css/digital-ng-library/ionicons.css">
<link rel="stylesheet" type="text/css" href="./app/fusion/styles/ecomp.css">
<script>
angular.module('att.abs.helper', []);
angular.module('quantum', []);
//angular.module('ui.bootstrap', []);
var appDS2=angular.module("abs", ["ngRoute", 'ngMessages', 'ngCookies', 'b2b.att.tpls', 'ddh.att.tpls', 'ddh.att.switches', 'b2b.att.footer', 'b2b.att.header']);
</script>

<script src="./app/fusion/scripts/DS2-services/leftMenuServiceDS2.js"></script>

<script>
appDS2.controller("leftMenuController", ['$scope', '$filter','$http','$timeout','$cookies','LeftMenuServiceDS2', function ($scope, $filter, $http, $timeout, $cookies, LeftMenuServiceDS2) {
	$scope.menuData = [];
	$scope.leftChildData=[];
	$scope.leftParentData=[];
	$scope.leftMenuItems = [];
	$scope.app_name = "";
	$scope.app_name_full;
	LeftMenuServiceDS2.getLeftMenu().then(function(response){
	var j = response; 
		try{
			if(j && j !== "null" && j!== "undefined"){
				$scope.leftParentData = JSON.parse(j.data);
  		    $scope.leftChildData = JSON.parse(j.data2);
			}else{
				throw "Get Left Menu respsone is not an object/is empty"; 
			}  
	    	try{
	    		var leftChildItemList = $scope.leftChildData;
          		var pageUrl = window.location.href.split('/')[window.location.href.split('/').length-1];
         		var leftParentList =$scope.leftParentData;
         		
	    		for (var i = 0; i < leftParentList.length; i++) {
	    			var parentItem = {};
	    			parentItem.name = leftParentList[i].label;
	    			if(parentItem.name == 'Home'){
	    				parentItem.imageSrc = /*"icon-location-pinpoint"*/ "icon-building-home";
	    			} else if(parentItem.name == 'Sample Pages'){
	    				parentItem.imageSrc = "icon-documents-book";
	    			} else if(parentItem.name == 'Reports'){
	    				parentItem.imageSrc = "icon-misc-piechart";
	    			} else if(parentItem.name == 'Profile'){
	    				parentItem.imageSrc = "icon-people-oneperson";
	    			} else if(parentItem.name == 'Admin'){
	    				parentItem.imageSrc = "icon-content-star";
	    			} else if(parentItem.name == 'Sample Pages'){
	    				parentItem.imageSrc = "icon-content-searchchannels";
	    			} else {
	    				parentItem.imageSrc = "icon-building-door";
	    			}            		    			
	    			parentItem.menuItems = [];
	    			for (var j = 0; j < leftChildItemList[i].length; j++) {
	    				if(leftChildItemList[i][j].label != null && leftChildItemList[i][j].label.length > 0) {
		    				var childItem = {};
		    				childItem.name = leftChildItemList[i][j].label;
		    				childItem.href = leftChildItemList[i][j].action;
		    				parentItem.menuItems.push(childItem)
	    				}
		    		}
	    			$scope.menuData.push(parentItem);
	    		}
	    		
  			
	    	//For Home, add href
	    	$scope.menuData[0].href = leftParentList[0].action;
	    		
     		for (var i = 0; i < leftParentList.length; i++) {
     			$scope.item = {
     				parentLabel : leftParentList[i].label,
     				parentAction : leftParentList[i].action,
     				parentImageSrc : leftParentList[i].imageSrc,                 				
     				open:pageUrl==leftParentList[i].action?true:false,
     				childItemList : leftChildItemList[i]
     			};
     		};   
  		}catch(err){
  			console.log("error happened while trying to set left menu structure"+err);  					   
  		}
		}catch (e) {
			console.log("error happened while trying to get left menu items"+e);
			reloadPageOnce();
			return;
    }	       
},function(error){
	console.log("error happened while calling getLeftMenu"+error);
});

LeftMenuServiceDS2.getAppName().then(function(response){
	var j = response; 
		try{
			if(j && j !== "null" && j!== "undefined"){
				console.log("app name is " + $scope.app_name);
				$scope.app_name_full = j.data;
				var processed_app_name = j.data;
				var n = processed_app_name.length;
				if (n > 15) {
					n = 15;
				}
				$scope.app_name = processed_app_name.substr(0, n);
			}else{
				throw "Get app_name respsone is not an object/is empty"; 
			}  
		}catch (e) {
			console.log("error happened while trying to get app name "+e);
			return;
    }	       
},function(error){
	console.log("error happened while calling getAppName "+error);
});

$scope.adjustHeader=function() {
	$scope.showHeader = ($cookies.show_app_header == undefined ? true : $cookies.show_app_header);
	
	if($scope.showHeader == true) {
		$scope.drawer_margin_top = 50;
		$scope.drawer_custom_top = 54;
		$scope.toggle_drawer_top = 55;
	}
	else  {
		
		$scope.drawer_margin_	top = 40;
		$scope.drawer_custom_top = 0;
		$scope.toggle_drawer_top = 10;
	}
	
	
};

$scope.adjustHLeftMenu = function (type){
	$scope.showHeader = ($cookies.show_app_header == undefined ? true : $cookies.show_app_header);
	
	if($scope.showHeader == true) {
		$scope.drawer_margin_top = 60;
		$scope.drawer_custom_top = 54;
		$scope.toggle_drawer_top = 55;
	}
	else  {
		
		$scope.drawer_margin_top = 50;
		$scope.drawer_custom_top = 0;
		$scope.toggle_drawer_top = 10;
	}
	if(type=='burgerIcon'){
		return { "top": $scope.toggle_drawer_top+"px"};
	}else if(type=='leftMenu'){
		return { "margin-top": $scope.drawer_margin_top+"px"};
	}else
		return;
}
$scope.adjustHeader();
$scope.drawerOpen = true;

$scope.toggleDrawer = function() {
	$scope.drawerOpen = !($scope.drawerOpen);
	if ($scope.drawerOpen) {
	// setCookie('drawerOpen','open',30);
	$scope.arrowShow = true;


	if (document.getElementById('fnMenueContent')!=null)
	document.getElementById('fnMenueContent').style.marginLeft = "0px";
	
	if (document.getElementById('rightContentAdmin')!=null)
			document.getElementById('rightContentAdmin').style.marginLeft = "210px"; 
			
		else if (document.getElementById('rightContentProfile')!=null)
			document.getElementById('rightContentProfile').style.marginLeft = "210px";
	} else {

	$scope.arrowShow = false;

	if (document.getElementById('fnMenueContent')!=null)
	document.getElementById('fnMenueContent').style.marginLeft = "-150px";
	
	if (document.getElementById('rightContentAdmin')!=null) {
				document.getElementById('rightContentAdmin').style.marginLeft = "50px";					
			}
			
	else if (document.getElementById('rightContentProfile')!=null)
		document.getElementById('rightContentProfile').style.marginLeft = "50px";
	}
	};
	
$timeout(function() {
	//detectScrollEvent();
    	}, 800);
}]);
</script>

<style>
#page-content{
margin-top:-250px;
}

#page-content td, th {
    padding: 0px;
    border:none;
}
</style>
<div ng-app="abs">
	<div ng-controller = "leftMenuController">
		<ddh-left-navigation menu-data="menuData"></ddh-left-navigation>
	</div>
</div>