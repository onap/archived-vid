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
app.directive('qHeader', function () {
    return {
        restrict: 'A', //This menas that it will be used as an attribute and NOT as an element. I don't like creating custom HTML elements
        replace: false,
        templateUrl: "app/fusion/scripts/view-models/header.html",
        controller: ['$scope', '$filter','$http','$timeout', '$log','UserInfoService', '$window', '$cookies', function ($scope, $filter, $http, $timeout, $log,UserInfoService, $window, $cookies) {
        	
        	/*Define fields*/
        	$scope.userName;
        	$scope.userFirstName;
        	$scope.redirectUrl;
        	$scope.contactUsUrl;
        	$scope.getAccessUrl;
        	$scope.childData=[];
        	$scope.parentData=[];
        	$scope.menuItems = [];
        	$scope.loadMenufail=false;
        	$scope.megaMenuDataObject=[];       	
        	$scope.activeClickSubMenu = {
          		x: ''
          	};
      		$scope.activeClickMenu = {
      			x: ''
      		};
      		$scope.favoritesMenuItems = [];
            $scope.favoriteItemsCount = 0;
            $scope.showFavorites = false;
            $scope.emptyFavorites = false;
            $scope.favoritesWindow = false;
      		
            /*Menu Structure*/
			var menuStructureConvert = function(menuItems) {	
      			console.log(menuItems);     		
  				$scope.megaMenuDataObjectTemp = 
  				[
  					 {
  						 text: "ECOMP",
  						 children: menuItems
	  				 },
	  				 {
	  					 text: "Help",
	  					 children: [
	  					            {
		      						 text:"Contact Us",
		      						 url:$scope.contactUsUrl
	  					            },
									{
	  					             text:"Get Access",
	  					             url:$scope.getAccessUrl
									}]
	  				 }
  				 ];      			
  				return $scope.megaMenuDataObjectTemp;
	        };
	        
	        /***************functions**************/
	        /*Put user info into fields*/
	    	$scope.inputUserInfo = function(userInfo){
	    		if (typeof(userInfo) != "undefined" && userInfo!=null && userInfo!=''){
	    			if(typeof(userInfo.USER_FIRST_NAME) != "undefined" && userInfo.USER_FIRST_NAME!=null){		
	    				$scope.userFirstName = userInfo.USER_FIRST_NAME;
	    			}
	    		}		
	    	}
	        /*getting user info from session*/
	    	$scope.getUserNameFromSession = function(){
	    		UserInfoService.getFunctionalMenuStaticDetailSession()
	    	  	.then(function (res) {
	    	  		$scope.contactUsUrl=res.contactUsLink;
      		  		$scope.userName = res.userName;
      		  		$scope.userFirstName = res.firstName;
      		  		$scope.redirectUrl = res.portalUrl;
      		  		$scope.getAccessUrl = res.getAccessUrl;
	    	  	});
	        }
      		$scope.getTopMenuStaticInfo=function() {
        		var promise = UserInfoService.getFunctionalMenuStaticDetailShareContext();
        		promise.then(
    				function(res) {   					
    					if(res==null || res==''){
    						$log.info('failed getting static User information');     
    						$scope.getUserNameFromSession();
    					}else{
    						$log.info('Received static User information');
    						
    						var resData = res;
    						console.log(resData);
    						$scope.inputUserInfo(resData);
    						$scope.userName = $scope.firstName+ ' '+ $scope.lastName;
    					}
    				},
    				function(err) {
						$log.info('failed getting static User information');       				
    				}
        		);
      		}

        	$scope.returnToPortal=function(){
        		window.location.href = $scope.redirectUrl;
        	}
        	
	        var unflatten = function( array, parent, tree ){
	            tree = typeof tree !== 'undefined' ? tree : [];
	            parent = typeof parent !== 'undefined' ? parent : { menuId: null };
	            var children = _.filter( array, function(child){ return child.parentMenuId == parent.menuId; });
	            if( !_.isEmpty( children )  ){
	              if( parent.menuId === null ){
	                tree = children;
	              }else{
	                parent['children'] = children
	              }
	              _.each( children, function( child ){ unflatten( array, child ) } );
	            }
	            return tree;
	        }
        	
        	$scope.getMenu=function() {
        		$scope.getTopMenuStaticInfo();
	        	$http({
	      		        method: "GET",
	      		        url: 'get_functional_menu',
// TIMEOUT USED FOR LOCAL TESTING ONLY	      		        
//	      		        timeout: 100
	      		 }).success(function (response) {
	      			 
				  		if (response == '101: Timeout') {
				  			$log.debug('Timeout attempting to get_functional_menu');
				  			$scope.megaMenuDataObject = menuStructureConvert("");
//				  			$scope.createErrorMenu();
 				 	   		//$scope.loadMenufail=true;
				  		} else {
		 				 	if(typeof response != 'undefined' && response.length!=0 && typeof response[0] != 'undefined' && typeof response[0].error!="undefined"){
					  			$log.debug('Timeout attempting to get_functional_menu');		 				 		
		 				 		$scope.menuItems = unflatten( response);
	 				 	   		$scope.megaMenuDataObject = menuStructureConvert($scope.menuItems);	 				 	 
//					  			$scope.createErrorMenu();
	 				 	   		//$scope.loadMenufail=true;
	 				 	   	}else{
	 				 	   		$scope.loadMenufail=false;
	 				 	   		$scope.contactUsURL = response.contactUsLink;
	 				 	   		$log.debug('functional_menu',response);	 				 	   		
	 				 	   		$scope.megaMenuDataObject = menuStructureConvert("");
	 				 	   	}
				  		}
	      		 }).error(function (response){
				  		$scope.megaMenuDataObject = menuStructureConvert("");
//				  		$scope.createErrorMenu();
			 	   		//$scope.loadMenufail=true;
				        $log.debug('REST API failed get_functional_menu...');
	      		 });
	        	
        	}
        	
        	$scope.adjustHeader=function() {
        		$scope.showHeader = ($cookies.show_app_header == undefined ? true : $cookies.show_app_header);
        		
        		if($scope.showHeader == true) {
    	    		$scope.drawer_margin_top = 70;
    	    		$scope.drawer_custom_top = 54;
    	    		$scope.toggle_drawer_top = 55;
        		}
        		else  {
        			
        			$scope.drawer_margin_top = 60;
            		$scope.drawer_custom_top = 0;
            		$scope.toggle_drawer_top = 10;
        		}
        		
        	}

        	$scope.getMenu();
        	$scope.adjustHeader();

/* **************************************************************************/
/* Logic for the favorite menus is here */

            $scope.loadFavorites = function () {
                $log.debug('loadFavorites has happened.');
                if ($scope.favoritesMenuItems == '') {
                    $scope.generateFavoriteItems();
                    $log.debug('loadFavorites is calling generateFavoriteItems()');
                } else {
                    $log.debug('loadFavorites is NOT calling generateFavoriteItems()');
                }
            }
            
            $scope.goToUrl = function (item) {
                $log.info("goToUrl called")
                $log.info(item);

                var url = item.url;
                var restrictedApp = item.restrictedApp;
                $log.debug('Restricted app status is: ' + restrictedApp);
                if (!url) {
                    $log.info('No url found for this application, doing nothing..');
                    return;
                }
                if (restrictedApp) {
                    $window.open(url, '_blank');
                } else {
                    $window.open(url, '_self');
                }

            }
            
            $scope.submenuLevelAction = function(index, column) {
                if ($scope.favoritesMenuItems == '') {
                    $scope.generateFavoriteItems();
                    $log.debug('submenuLevelAction is calling generateFavoriteItems()');
                }
                $log.debug('item hovered/clicked: ' + index + '; column = ' + column);
                if (column == 2) {  // 2 is Design
                    $scope.favoritesWindow = false;
                    $scope.showFavorites = false;
                    $scope.emptyFavorites = false;
                }
                if (index=='Favorites' && $scope.favoriteItemsCount != 0) {
                    $log.debug('Showing Favorites window');
                    $scope.favoritesWindow = true;
                    $scope.showFavorites = true;
                    $scope.emptyFavorites = false;
                }
                if (index=='Favorites' && $scope.favoriteItemsCount == 0) {
                    $log.debug('Hiding Favorites window in favor of No Favorites Window');
                    $scope.favoritesWindow = true;
                    $scope.showFavorites = false;
                    $scope.emptyFavorites = true;
                }
                if (column > 2) {
                    $scope.favoritesWindow = false;
                    $scope.showFavorites = false;
                    $scope.emptyFavorites = false;
                }
            };
            
            $scope.hideFavoritesWindow = function() {
                $log.debug('$scope.hideFavoritesWindow has been called');
                $scope.showFavorites = false;
                $scope.emptyFavorites = false;
            }
            
            $scope.isUrlFavorite = function (menuId) {
//                 $log.debug('array objects in menu favorites = ' + $scope.favoriteItemsCount + '; menuId=' + menuId);
                var jsonMenu =  JSON.stringify($scope.favoritesMenuItems);
                var isMenuFavorite =  jsonMenu.indexOf('menuId\":' + menuId);
                if (isMenuFavorite==-1) {
                    return false;
                } else {
                    return true;
                }

            }
            
            $scope.generateFavoriteItems  = function() {
            	$http({
      		        method: "GET",
      		        url: 'get_favorites',
// TIMEOUT USED FOR LOCAL TESTING ONLY	      		        
//	      		        timeout: 100
	      		    }).success(function (response) {
				  		if (response == '101: Timeout') {
						$log.error('Timeout attempting to get_favorites_menu');
					} else {
					 	if(typeof response != 'undefined' && response.length!=0 && typeof response[0] != 'undefined' && typeof response[0].error!="undefined"){
						    $log.error('REST API failed get_favorites' + response);
					 	   	}else{
					 	   		$log.debug('get_favorites = ' + JSON.stringify(response));
						 	   	$scope.favoritesMenuItems = response;
		                        $scope.favoriteItemsCount = Object.keys($scope.favoritesMenuItems).length;
		                        $log.info('number of favorite menus: ' + $scope.favoriteItemsCount);
	                        }
				  		}
				}).error(function (response){
				    $log.error('REST API failed get_favorites' + response);
//createFavoriteErrorMenu() USED FOR LOCAL TESTING ONLY	      		        
//					$scope.createFavoriteErrorMenu();
				});
        	}

        	$scope.createFavoriteErrorMenu=function() {
                $scope.favoritesMenuItems = [];
                $scope.favoriteItemsCount = Object.keys($scope.favoritesMenuItems).length;
                $log.info('number of favorite menus: ' + $scope.favoriteItemsCount);
        	}
            
        	/* end of Favorite Menu code */
	        /* **************************************************************************/

        	
	        /* **************************************************************************/
        	// THIS IS USED FOR LOCAL TESTING ONLY
	        /* **************************************************************************/
        	$scope.createErrorMenu=function() {
	  			$log.debug('Creating fake menu now...');
//        		$scope.loadMenufail=true;
                $scope.menuItems = [
                                       {
                                   	    "menuId": 1,
                                   	    "column": 2,
                                   	    "text": "Design",
                                   	    "parentMenuId": null,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 2,
                                   	    "column": 3,
                                   	    "text": "Infrastructure Ordering",
                                   	    "parentMenuId": null,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 3,
                                   	    "column": 4,
                                   	    "text": "Service Creation",
                                   	    "parentMenuId": null,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 4,
                                   	    "column": 5,
                                   	    "text": "Service Mgmt",
                                   	    "parentMenuId": null,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 90,
                                   	    "column": 1,
                                   	    "text": "Google",
                                   	    "parentMenuId": 1,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 91,
                                   	    "column": 1,
                                   	    "text": "Mike Little's Coffee Cup",
                                   	    "parentMenuId": 2,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 92,
                                   	    "column": 2,
                                   	    "text": "Andy and his Astrophotgraphy",
                                   	    "parentMenuId": 3,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 93,
                                   	    "column": 1,
                                   	    "text": "JSONLint",
                                   	    "parentMenuId": 4,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 94,
                                   	    "column": 2,
                                   	    "text": "HROneStop",
                                   	    "parentMenuId": 4,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 95,
                                   	    "column": 2,
                                   	    "text": "4th Level App4a R16",
                                   	    "parentMenuId": 4,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 96,
                                   	    "column": 3,
                                   	    "text": "3rd Level App1c R200",
                                   	    "parentMenuId": 4,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 97,
                                   	    "column": 1,
                                   	    "text": "3rd Level App4b R16",
                                   	    "parentMenuId": 5,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 98,
                                   	    "column": 2,
                                   	    "text": "3rd Level App2b R16",
                                   	    "parentMenuId": 5,
                                   	    "url": ""
                                   	  },
                                   	  {
                                   	    "menuId": 99,
                                   	    "column": 1,
                                   	    "text": "Favorites",
                                   	    "parentMenuId": null,
                                   	    "url": ""
                                   	  }
                                   	];
		  		$scope.menuItems = unflatten( $scope.menuItems );
		  		//remove this
	 	   		$scope.megaMenuDataObject = menuStructureConvert($scope.menuItems);
        	}
        }]
    }
});

app.filter("ellipsis", function(){
    return function(text, length){
        if (text) {
            var ellipsis = text.length > length ? "..." : "";
            return text.slice(0, length) + ellipsis;
        };
        return text;        
    }
});

function reloadPageOnce() {
	if( window.localStorage )
	{
	  if( !localStorage.getItem('firstLoad') )
	  {
	    localStorage['firstLoad'] = true;
	    window.location.reload();
	  }  
	  else
	    localStorage.removeItem('firstLoad');
	}
}
app.controller('loginSnippetCtrl', function ($scope,$http, $log,UserInfoService){
	/*Define fields*/
	$scope.userProfile={
			firstName:'',
			lastName:'',
			fullName:'',
			email:'',
			userid:''
	}
	/*Put user info into fields*/
	$scope.inputUserInfo = function(userInfo){
		if (typeof(userInfo) != "undefined" && userInfo!=null && userInfo!=''){
			if (typeof(userInfo.USER_FIRST_NAME) != "undefined" && userInfo.USER_FIRST_NAME!=null && userInfo.USER_FIRST_NAME!='')
    			$scope.userProfile.firstName = userInfo.USER_FIRST_NAME;
    		if (typeof(userInfo.USER_LAST_NAME) != "undefined" && userInfo.USER_LAST_NAME!=null && userInfo.USER_LAST_NAME!='')
    			$scope.userProfile.lastName = userInfo.USER_LAST_NAME;
    		if (typeof(userInfo.USER_EMAIL) != "undefined" && userInfo.USER_EMAIL!=null && userInfo.USER_EMAIL!='')
    			$scope.userProfile.email = userInfo.USER_EMAIL;	
    		if (typeof(userInfo.USER_ORGUSERID) != "undefined" && userInfo.USER_ORGUSERID!=null && userInfo.USER_ORGUSERID!='')
				$scope.userProfile.userid = userInfo.USER_ORGUSERID;    	
		}		
	}
	/*getting user info from session*/
	$scope.getUserNameFromSession = function(){
		UserInfoService.getFunctionalMenuStaticDetailSession()
	  	.then(function (response) {	  		
	  		$scope.userProfile.fullName = response.userName;
	  		$scope.userProfile.userid = response.userid;
	  		$scope.userProfile.email = response.email;
	  	});
    }
	/*getting user info from shared context*/
	$scope.getUserName=function() {
		var promise = UserInfoService.getFunctionalMenuStaticDetailShareContext();
		promise.then(
			function(res) { 
				if(res==null || res==''){
					$log.info('Getting User information from session');
					$scope.getUserNameFromSession();
					
				}else{
					$log.info('Received User information from shared context',res);
					var resData = res;
					console.log(resData);
					$scope.inputUserInfo(resData);
					$scope.userProfile.fullName = $scope.userProfile.firstName+ ' '+ $scope.userProfile.lastName;					
				}
			},
			function(err) {
				console.log('error');
			}
		);
    };
    /*call the get user info function*/
    try{
    	$scope.getUserName();
    }catch(err){
    	$log.info('Error while getting User information',err);
    }
});
