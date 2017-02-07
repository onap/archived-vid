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
	                                		 url:"javascript:angular.element('[ng-controller=headerController]').scope().redirectLink('contact');"
	                                	 },
	                                	 {
	                                		 text:"Get Access",
	                                		 url:"javascript:angular.element('[ng-controller=headerController]').scope().redirectLink('access');"
	                                	 }]
	                                 }
	                                 ];
	return megaMenuDataObjectTemp;
}; 
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
app.service('HeaderService', function ($http,$log, $q,UserInfoService) {
	return{
	
		getUserNameFromSession : function(){
	    		UserInfoService.getFunctionalMenuStaticDetailSession()
	    	  	.then(function (res) {
	  		  		$scope.userName = res.userName;
	  		  		$scope.redirectUrl = res.portalUrl;
	    	  	});
	    },
		getTopMenuStaticInfo:function() {
	    		var promise = UserInfoService.getFunctionalMenuStaticDetailShareContext();
	    		promise.then(
					function(res) {   					
						if(res==null || res==''){
							$log.info('failed getting static User information');    
							this.getUserNameFromSession();
						}else{
							$log.info('Received static User information');
							var resData = res;						
							$scope.inputUserInfo(resData);					
							$scope.userProfile.fullName = $scope.userProfile.firstName+ ' '+ $scope.userProfile.lastName;
							return $scope.userProfile;				
						}
					},
					function(err) {
						$log.info('failed getting static User information');       				
					}
	    		);
	  		}
	  	}  	
	}
);

