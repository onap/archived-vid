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
app.factory('UserInfoService', function ($http, $q,$log) {
	return {
		getFunctionalMenuStaticDetailShareContext: function(contextId,key) {
			var deferred = $q.defer();
			$http({
                method: "GET",
                url: "get_userinfo",      
            }).success( function(res) {              	
            	if(res==null || res=='')
            		$log.info('Not be able to get User Info via shared context');                 
                deferred.resolve(res);
            }).error( function(status) {            	
                deferred.reject(status);
            });
            return deferred.promise;       
		},		
		getFunctionalMenuStaticDetailSession: function() {
			var deferred = $q.defer();
			$http({
                method: "GET",
                url: "get_topMenuInfo",    
            }).success(function(res) {                    	
            	if(res==null || res=='')
            		$log.info('Not be able to get User Info via shared context');                 
                deferred.resolve(res);
            }).error( function(status) {            	
                deferred.reject(status);
            });
            return deferred.promise;       
		}
	};
});
