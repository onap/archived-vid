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

app.directive('qMenu', function () {
    return {
        restrict: 'A', //This menas that it will be used as an attribute and NOT as an element. I don't like creating custom HTML elements
        replace: false,
        templateUrl: "app/fusion/scripts/view-models/left_menu.html",
        controller: ['$scope', '$filter','$http','$timeout','$cookies','LeftMenuService', function ($scope, $filter, $http,$timeout,$cookies,LeftMenuService) {
        	
        	$scope.leftChildData=[];
        	$scope.leftParentData=[];
        	$scope.leftMenuItems = [];
        	$scope.app_name = "";
        	$scope.app_name_full;
        	LeftMenuService.getLeftMenu().then(function(response){
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
                 			$scope.item = {
                 				parentLabel : leftParentList[i].label,
                 				parentAction : leftParentList[i].action,
                 				parentImageSrc : leftParentList[i].imageSrc,                 				
                 				open:pageUrl==leftParentList[i].action?true:false,
                 				childItemList : leftChildItemList[i]
                 			}
                 			$scope.leftMenuItems.push($scope.item);
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
        	
        	LeftMenuService.getAppName().then(function(response){
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
    	    		$scope.drawer_custom_top = 20;
    	    		$scope.toggle_drawer_top = 55;
        		}
        		else  {
        			
        			$scope.drawer_margin_top = 0;
            		$scope.drawer_custom_top = 0;
            		$scope.toggle_drawer_top = 0;
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
    			detectScrollEvent();
    		}, 800);
        }]
    }
    
});
$(window).scroll(function() {
	if ($('.att-drawer').is(':visible')) {
		detectScrollEvent();
	}

});

function detectScrollEvent() {
	try{
		var footerOff = $('#footerContainer').offset().top;
		var headOff = $('#headerContainer').offset().top;
		var winHeight = $(window).height();
		if ((footerOff - headOff) <= winHeight) {
			$('.att-drawer').css({
				"height" : footerOff - headOff - 55
			});
		} else {
			$('.att-drawer').css({
				"height" : "94vh"
			});
		}
	}catch(err){
		console.log(err)
	}
}
