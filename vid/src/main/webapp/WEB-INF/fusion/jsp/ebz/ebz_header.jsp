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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties"%>
<%@ page import="org.openecomp.portalsdk.core.onboarding.crossapi.PortalApiProperties"%>
<%@ page import="org.openecomp.portalsdk.core.onboarding.crossapi.PortalApiConstants"%>
<%@ page import="org.openecomp.portalsdk.core.domain.MenuData"%>
<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/ebz_header/header.css">
<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/ebz_header/portal_ebz_header.css">
<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/sandbox/styles/style.css" >

<!-- Icons -->
<link rel="stylesheet" type="text/css" href="app/fusion/external/ionicons-2.0.1/css/ionicons.css" />
<script src= "app/fusion/external/ebz/angular_js/angular.js"></script> 
<script src= "app/fusion/external/ebz/angular_js/angular-route.min.js"></script>
<script src= "app/fusion/external/ebz/angular_js/angular-sanitize.js"></script>
<script src= "app/fusion/external/ebz/angular_js/angular-cookies.js"></script>
<script src= "app/fusion/external/ebz/angular_js/app.js"></script>
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


<jsp:include page="/WEB-INF/fusion/jsp/ebz/loginSnippet.html" ></jsp:include> 

<c:set var="UserName"	value="<%= session.getAttribute(\"fullName\")%>" />
<c:set var="UserFirstName"	value="<%= session.getAttribute(\"first_name\")%>" />

<% 
	String contactUsLink = SystemProperties.getProperty(SystemProperties.CONTACT_US_LINK);
	String redirectUrl = PortalApiProperties.getProperty(PortalApiConstants.ECOMP_REDIRECT_URL);
	String portalUrl = redirectUrl.substring(0, redirectUrl.lastIndexOf('/')) + "/processSingleSignOn";
	String getAccessLink = redirectUrl.substring(0, redirectUrl.lastIndexOf('/')) + "/get_access";	
%>
<c:set var="returnPortalUrl" value="<%=portalUrl%>" />
<c:set var="contactUsLink" value="<%=contactUsLink%>" />
<c:set var="getAccessLink" value="<%=getAccessLink%>" />
<style>
.att-drawer{
	z-index:997 !important;
}

.secondaryMenuContainer{
	z-index:1000;
}
.megamenu-tabs .megamenu__item {
	padding: 0 0;
}
.megamenu-tabs .megamenu__item span {
    font-size: 18px;
}

.submenu-tabs {
   line-height:50px;
}
.submenu-tabs .sub__menu{
	top:54px;
}

.top-megamenu .megamenu-tabs ul{
	width:98%;
	list-style: none;
}

</style>
<%@include  file="/WEB-INF/fusion/jsp/ebz/loginSnippet.html" %>

<div style="position: relative; z-index: 999;">
	<div ng-controller="headerController">
	
		 <div class="headerContainer" id="headerContainer" ng-cloak  ng-show="{{showHeader}}">
			<div id="megaMenuContainer" class="megaMenuContainer" style="margin-top: 0; overflow: visible;">
				<div>
					<!--for mega Menu-->
					<!-- Mega Menu parent-tab directive with three models menu-items, active-sub-menu, active-menu -->
					<div id="topMenu" class="top-megamenu" ng-mouseleave="activeClickSubMenu.x.active=false; activeClickMenu.x.active=false">
						<div style="float:left;width:100%;"parent-tab menu-items="megaMenuDataObject" active-sub-menu='activeClickSubMenu.x' active-menu='activeClickMenu.x'>
			                <div parentmenu-tabs mega-menu="true" menu-items="megaMenuDataObject" style="height:55px;">
								<div style="float:left">
									<li class="megamenu__item" style="line-height:55px;" onclick="returnToPortal()">
										
<!-- 											<a id='returnPortal' class="primaryMenuOptionLink" style="font-weight: 400 !important; font-family: Arial !important; font-size: 18px;">ECOMP</a>
 -->										<strong	style="font-weight: 400 !important; font-family: Arial !important; font-size: 18px;" id='returnPortal' >ECOMP Portal</strong>
									</li>										
									<div menu-tabs mega-menu="true" tab-name="item.text" menu-item="item" active-menu="activeClickMenu.x" 
									ng-repeat="item in megaMenuDataObject" style="font-size: 18px;" ng-mousedown="loadFavorites()" >
				                        <div parentmenu-tabs sub-menu="true" ng-show="activeClickMenu.x.active && item.active" menu-items="activeClickMenu.x.children">
							<!-- Second level menu -->
									<div>
										<div menu-tabs sub-menu="true" tab-name="subItem.text" 
											tab-url="subItem.url"  menu-item="subItem" 
											ng-repeat="subItem in activeClickMenu.x.children | orderBy : 'column'" active-menu="activeClickSubMenu.x" 
											sub-item-active="{{subItem.active}}" style="float:left;" aria-label="{{subItem.text}}"
	                                         ng-mouseenter="submenuLevelAction(subItem.text,subItem.column)"
	                                         ng-mouseleave="submenuLevelAction(subItem.text,subItem.column)"
                                        	ng-click="submenuLevelAction(subItem.text,subItem.column)"  >
                                                <i ng-if="subItem.text=='Favorites'" id="favorite-star"
                                                   class="icon-star favorites-icon-active">
                                                </i>
										</div>
	                                                                                 	
			                                <div class="sub__menu" ng-mouseleave="activeClickSubMenu.x.active=false" >
			                                	<ul ng-show="activeClickSubMenu.x.active"  role="menubar" class="columns">
			               <!-- Third level menu -->					                                	
				                                    <div menu-tabs menu-item="subItem" 
				                                    class="columns-div"
				                                    ng-repeat="subItem in activeClickSubMenu.x.children | orderBy : 'column'" 
				                                    ng-show="activeClickSubMenu.x.active">

	                                                    <i id="favorite-selector-third-level"
	                                                       ng-show="isUrlFavorite(subItem.menuId)==false"
	                                                       class="icon-star favorites-icon-inactive"
	                                                       ng-if="subItem.url.length > 1">
	                                                    </i>					                                    
	                                                    <i id="favorite-selector-third-level"
	                                                       ng-show="isUrlFavorite(subItem.menuId)"
	                                                       class="icon-star favorites-icon-active"
	                                                       ng-if="subItem.url.length > 1">
	                                                    </i>					                                    
														<span class="title" aria-label="{{subItem.text}}" 
														ng-click="goToUrl(subItem)">{{subItem.text}}</span>
							<!-- Fourth level menus -->	
				                                        <div att-links-list="">
                                                            <i id="favorite-selector-fourth-level"
                                                               class="icon-star favorites-icon-inactive"
                                                               ng-show="isUrlFavorite(tabValue.menuId)==false"
                                                               ng-if="tabValue.url.length > 1">

                                                            </i>
                                                            <i id="favorite-selector-fourth-level"
                                                               class="icon-star favorites-icon-active"
                                                               ng-show="isUrlFavorite(tabValue.menuId)"
                                                               ng-if="tabValue.url.length > 1">

                                                            </i>
				                                            <span role="menuitem" att-links-list-item="" 
				                                            ng-repeat="tabValue in subItem.children" 
				                                            ng-click="goToUrl(tabValue)" 
				                                            att-accessibility-click="13,32" 
				                                            ng-class="{'disabled': tabValue.disabled}">{{tabValue.text}}</span>
				                                        </div>
				                                        <hr ng-show="!$last"/>
				                                        
				                                    </div>
				                                 </ul>
			           <!-- Favorites level menu -->
												<div class="favorites-window" ng-show='favoritesWindow' ng-mouseleave="hideFavoritesWindow()">
													<div id="favorites-menu-items" ng-show="showFavorites">
														<div ng-repeat="subItem in favoritesMenuItems" att-links-list="" style='display: inline'>
																<i id="favorite-selector-favorites-list" class="icon-star favorites-icon-active">
																</i>
																<a id="favorites-list" aria-label="{{subItem.text}}"
																   ng-click="goToUrl(subItem)" 
																   style="margin-left: 3px; margin-right: 20px; text-decoration: none;  color: #666666;">
																   {{subItem.text}}
																</a>
														</div>
														<div>
															<br>
															<p style='font-weight: 400; font-family: Arial !important;
															font-size: 18px; text-align: center; background-color: lightgray;
															width: 400px; margin-left: 25%; margin-right: 25%;'>
																Manage favorites on ECOMP Portal.
															</p>
														</div>
													</div>
								  <!-- Favorites when empty -->
														<div id="favorites-empty" ng-show='favoritesWindow' ng-show="emptyFavorites">
			                                                <div id="favorites-empty" ng-show="emptyFavorites" class="favorites-window-empty">
			                                                    <div>
			                                                        <img src="app/fusion/external/ebz/images/no_favorites_star.png">
			                                                        <p class='favoritesLargeText'>No Favorites</p>
																	<p class='favoritesNormalText'>Manage favorites on ECOMP Portal.</p>
			                                                    </div>
			                                                </div>
														</div>
													</div>
					                                
				                                </div>
				                            </div>
				                        </div> 
				                    </div >
				                    <li class="megamenu__item" style="line-height:55px;" ng-if="loadMenufail">
										<strong	style="font-weight: 400 !important; font-family: Arial !important; font-size: 18px;" >Unable to load menus</strong>
									</li>
<!-- 										<li class="megamenu__item" style="width: 20%;">&nbsp;</li>
 -->									</div>
								<!--  Login Snippet-->
								<div  style="float:right">
									<li id="bcLoginSnippet" class="megamenu__item" style="width: 140px;" >
										<div popover="loginSnippet.html"  aria-label="Login Snippet"	referby="loginSnippet" att-accessibility-click="13,32" popover-style="\" popover-placement="below" style="width: 200px;">
											<div class="ion-android-person login-snippet-icon" style="display:inline-block"></div>
											<div class="login-snippet-text" style="display: inline-block; font-size:12px; margin-left:5px;overflow: hidden; max-height: 31px;   max-width:120px; padding-top: 0px; margin-top: 0px; white-space: nowrap;" ng-bind="userProfile.firstName"></div>
										</div>
									</li>
									<li class="megamenu__item" style="width:120px;">&nbsp;</li>
								</div>
								
							</div>		
						</div>
						<div style="clear: both"></div>
					</div>
					</div>
				</div>
			</div>

		<div style="position: relative; color: black; top: 70px;">
			<div ng-cloak>		
				<span ng-style="adjustHLeftMenu('burgerIcon')" style="z-index:998; position:fixed; left:0%; font-size:35px; margin-left:10px;text-decoration:none;">
					<a ng-click="toggleDrawer();isOpen = !isOpen" href="javascript:void(0);" class="arrow-icon-left" >
					<span class="ion-navicon-round"></span></a>	
					<span ng-init="isOpen = true" ng-show="isOpen" style="font-size:16px; position:relative; top:-8px; left:-15px;">&nbsp&nbsp&nbsp {{app_name}}</span>
				</span>
				<div att-drawer drawer-slide="left" drawer-custom-top="{{drawer_custom_top}}px" drawer-size="200px" drawer-open="drawerOpen" drawer-custom-height="100%" >
				    <div ng-style="adjustHLeftMenu('leftMenu')">
					    <div class="attDrawer" style="margin-top:{{drawer_margin_top}}px;">   
					    	<div style="margin-left:10px; margin-right:10px;">
						    	<accordion close-others="true" css="att-accordion--no-box">
				                    <accordion-group ng-repeat="parent in menuItems" heading="{{parent.parentLabel}}" child="{{parent.parentAction}}" parent-link="{{parent.parentAction}}" image-source="{{parent.parentImageSrc}}" child-length="{{parent.childItemList.length}}" is-open="parent.open">
				                    	<div ng-repeat="subMenu in parent.childItemList" style="font-size:12px; margin-left:10px;">
				                    		<a href="{{subMenu.action}}" style="font-size:12px; color:#666666;" >{{subMenu.label}}</a>
				                    	</div>
				                    </accordion-group>
				                </accordion>
							</div>						
					    </div>
				    </div>
				</div>
			</div>		
		</div>
	</div>
</div>
<script>    
	function returnToPortal(){
		window.location.href = "<c:out value='${returnPortalUrl}'/>";
	}
	detectScrollEvent = function() {
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
	}
	$(window).scroll(function() {
		if ($('.att-drawer').is(':visible')) {
			detectScrollEvent();
		}
	});
	app.controller("headerController", function($scope, $timeout, $log, $http, UserInfoService, $window, $cookies,LeftMenuService) {
  		$log.debug('HeaderController started');
		$scope.jsonMenuData = [];
		$scope.loadMenufail=false;
		$scope.app_name = "";
		$scope.app_name_full = "";
		$scope.megaMenuDataObject =[];
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
        $scope.userProfile={
    			firstName:'',
    			lastName:'',
    			fullName:'',
    			email:''
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
    		}		
    	}
      	 /*getting user info from session*/
    	$scope.getUserNameFromSession = function(){
    		UserInfoService.getFunctionalMenuStaticDetailSession()
    	  	.then(function (res) {
  		  		$scope.userProfile.firstName = res.firstName;
  		  		$scope.redirectUrl = res.portalUrl;
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
						$scope.inputUserInfo(resData);					
						$scope.userProfile.fullName = $scope.userProfile.firstName+ ' '+ $scope.userProfile.lastName;					
					}
				},
				function(err) {
					$log.info('failed getting static User information');       				
				}
    		);
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
		
		var menuStructureConvert = function(menuItems) {
			var megaMenuDataObjectTemp = [
	                                 {
	                                	 text: "ECOMP",
	                                	 children:menuItems
	                                 },
	                                 {
	                                	 text: "Help",
	                                	 children: [{
	                                		 text:"Contact Us",
	                                		 url:"<c:out value='${contactUsLink}'/>"
	                                	 },
	                                	 {
	                                		 text:"Get Access",
	                                		 url:"<c:out value='${getAccessLink}'/>"
	                                	 }]
	                                 }
	                                 ];
			return megaMenuDataObjectTemp;
		}; 
   		
		
		/*Left Menu*/
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
		
	    $scope.getTopMenuStaticInfo();      
    	$scope.getMenu=function() {
    		
   		 $http({
   		        method: "GET",
   		        url: 'get_functional_menu',
// TIMEOUT USED FOR LOCAL TESTING ONLY	      		        
// 		        timeout: 100
   		 }).success(function (response) {
			if(response == '101: Timeout') {
	  			$log.error('Timeout attempting to get_functional_menu');
	  		// TIMEOUT USED FOR LOCAL TESTING ONLY	      		        
//	  			$scope.createErrorMenu();
	  			$scope.megaMenuDataObject = menuStructureConvert('');
	  		}else {
 		   		$log.debug('get_functional_menu success: ' + response); 		   			   		
				if(typeof response != 'undefined' && response.length!=0 && typeof response[0] != 'undefined' && typeof response[0].error!="undefined"){
// createErrorMenu() USED FOR LOCAL TESTING ONLY
//		  			$scope.createErrorMenu();
					$scope.megaMenuDataObject = menuStructureConvert('');
		 	   	//	$scope.loadMenufail=true;
			 	}else{
		 	   		$scope.jsonMenuData = unflatten( response );
		 	   		$scope.megaMenuDataObject = menuStructureConvert($scope.jsonMenuData);
	 	   		}
			}
		}).error(function (response){
// createErrorMenu() USED FOR LOCAL TESTING ONLY	      		        
//		  		$scope.createErrorMenu();

	 	   		//$scope.loadMenufail=true;
	 	   		$scope.megaMenuDataObject = menuStructureConvert('');
		        $log.debug('REST API failed get_functional_menu...'+ response);
		  });
  		}
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
    	$scope.adjustHeader=function() {
    		$scope.showHeader = ($cookies.show_app_header == undefined ? true : $cookies.show_app_header);
    		
    		if($scope.showHeader == true) {
	    		$scope.drawer_margin_top = 50;
	    		$scope.drawer_custom_top = 54;
	    		$scope.toggle_drawer_top = 55;
    		}
    		else  {
    			
    			$scope.drawer_margin_top = 40;
        		$scope.drawer_custom_top = 0;
        		$scope.toggle_drawer_top = 10;
    		}
    		
    		
    	}
    	
    	
		//$scope.getMenu();
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
//			      		        timeout: 100
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

					});
        	}
		            
		        	$scope.createFavoriteErrorMenu=function() {
		                $scope.favoritesMenuItems = [

		                                         	];
		                $scope.favoriteItemsCount = Object.keys($scope.favoritesMenuItems).length;
		                $log.info('number of favorite menus: ' + $scope.favoriteItemsCount);
		        	}
		            
        	/* end of Favorite Menu code */
	        /* **************************************************************************/

        	
	        /* **************************************************************************/
        	// THIS IS USED FOR LOCAL TESTING ONLY
	        /* **************************************************************************/

			$scope.createErrorMenu=function() {
    		$scope.jsonMenuData = [
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
                               	    "url": "http://google.com"
                               	  },
                               	  {
                               	    "menuId": 91,
                               	    "column": 1,
                               	    "text": "Mike Little's Coffee Cup",
                               	    "parentMenuId": 2,
                               	    "url": "http://coffee.com"
                               	  },
                               	  {
                               	    "menuId": 92,
                               	    "column": 2,
                               	    "text": "Andy and his Astrophotgraphy",
                               	    "parentMenuId": 3,
                               	    "url": "http://nightskypix.com"
                               	  },
                               	  {
                               	    "menuId": 93,
                               	    "column": 1,
                               	    "text": "JSONLint",
                               	    "parentMenuId": 4,
                               	    "url": "http://http://jsonlint.com"
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
                               	    "url": "http://app1c.com"
                               	  },
                               	  {
                               	    "menuId": 97,
                               	    "column": 1,
                               	    "text": "3rd Level App4b R16",
                               	    "parentMenuId": 5,
                               	    "url": "http://app4b.com"
                               	  },
                               	  {
                               	    "menuId": 98,
                               	    "column": 2,
                               	    "text": "3rd Level App2b R16",
                               	    "parentMenuId": 5,
                               	    "url": "http://app2b.com"
                               	  },
                               	  {
                               	    "menuId": 99,
                               	    "column": 1,
                               	    "text": "Favorites",
                               	    "parentMenuId": null,
                               	    "url": ""
                               	  }
                               	];
	  		$scope.jsonMenuData = unflatten( $scope.jsonMenuData );
 	   		$scope.megaMenuDataObject = menuStructureConvert($scope.jsonMenuData);
//	  		$log.debug(JSON.stringify($scope.jsonMenuData));
    	}
		var childItemList="";
		var parentList = "";
		try{
			childItemList = ${menu.childItemList};
			parentList = ${menu.parentList};
		}catch(err){
			console.log("error while getting left menu");
		}
		
 		var pageUrl = window.location.href.split('/')[window.location.href.split('/').length-1];
		
		$scope.menuItems = [];
		for (var i = 0; i < parentList.length; i++) {
			$scope.openCurrentMenu = false;
		 if(pageUrl==parentList[i].action)
				$scope.openCurrentMenu = true;
			$scope.childItemList = childItemList[i];
			for(chIndex in  $scope.childItemList){
				if($scope.childItemList.length>0)
					if($scope.childItemList[chIndex].action!=null){
						if($scope.childItemList[chIndex].action==pageUrl)
							$scope.openCurrentMenu = true;
					}
			}
			$scope.item = {
				parentLabel : parentList[i].label,
				parentAction : parentList[i].action,
				parentImageSrc : parentList[i].imageSrc,
				open:$scope.openCurrentMenu,
				childItemList : $scope.childItemList
			}
			$scope.menuItems.push($scope.item);
		}
		$scope.arrowShow = true;
		$scope.drawerOpen = false;
		$scope.subMenuContent = false;
		$scope.toggleSubMenu = function() {
			$scope.subMenuContent = !$scope.subMenuContent;
		};

		var drawerOpen = 'open';
		if (drawerOpen == 'open') {
			$scope.drawerOpen = true;
			$scope.arrowShow = true;
		} else {
			$scope.arrowShow = false;
		}
		$scope.arrowShow = true;
		$scope.drawerOpen = false;
		$scope.toggleDrawer = function() {
			$scope.drawerOpen = !($scope.drawerOpen);
			if ($scope.drawerOpen) {
				$scope.arrowShow = true;				
				if (document.getElementById('mContent')!=null)
					document.getElementById('mContent').style.marginLeft = "210px"; 			
			} else {
				$scope.arrowShow = false;
				if (document.getElementById('mContent')!=null)
					document.getElementById('mContent').style.marginLeft = "50px";
			}
		};
		//var drawerOpen = getCookie('drawerOpen');
		if (drawerOpen == 'open') {
			$scope.drawerOpen = true;
			$scope.arrowShow = true;
		} else {
			$scope.arrowShow = false;
		}
		$timeout(function() {
			detectScrollEvent();
		}, 800);
		
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
</script>
