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
var modalpopupController =  function ($scope, $modalInstance, message){
	
	$scope.message = message;
	
	
	$scope.hello = function () {
        $modalInstance.close($scope.digitPattern);
    };
	$modalInstance.ok = function() {
        //add the  ok functionality
        alert("Logout");        
    };
    $modalInstance.cancel = function() {
        //add the cancel functionality
        alert("Keep Log in");
    };
    $modalInstance.cancelbutton = function() {
        //add the cancel functionality
        alert("Modal Waring popup close event");
    };
}
