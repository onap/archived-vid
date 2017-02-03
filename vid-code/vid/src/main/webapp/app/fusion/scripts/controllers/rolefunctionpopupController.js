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
var rolefunctionpopupController =  function ($scope, modalService, $modalInstance, message, $http,AdminService){
				if(message.availableRoleFunction==null) {
					$scope.label='Add Role Function';
					var tempText = "";
				}
				else{
					$scope.label='Edit Role Function'
					$scope.disableCd=true;
					var tempText = new String(message.availableRoleFunction.name);
					$scope.editRoleFunction = message.availableRoleFunction; 
				}
				
				$scope.tempText = tempText;
				
				$scope.saveRoleFunction = function(availableRoleFunction) {
					  var uuu = "role_function_list/saveRoleFunction.htm";
					  var postData={availableRoleFunction: availableRoleFunction};

					  if(availableRoleFunction==null){
							modalService.showFailure("Warning",  "Please enter valid role function details.");
					  }
					  var exists = false;
					  for(x in message.availableRoleFunctions){
						  console.log(message.availableRoleFunctions[x].name);
							if(message.availableRoleFunctions[x].name==availableRoleFunction.name){
								modalService.showFailure("Warning",  "Role Function already exists.");
								exists = true;
								availableRoleFunction.name = $scope.tempText;
							}
					  }
					  
					  if(!exists && availableRoleFunction.name.trim() != '' && availableRoleFunction.code.trim() != ''){
			              $http.post(uuu, JSON.stringify(postData)).then(function(res){
			            	  console.log("data");
//			            	  console.log(res.data);
//			            	  $scope.availableRoleFunctionsTemp = res.data.availableRoleFunctions;
			            	  AdminService.getRoleFunctionList().then(function(data){
			            			
			            			var j = data;
			            	  		$scope.data = JSON.parse(j.data);
			            	  		$scope.availableRoleFunctions =JSON.parse($scope.data.availableRoleFunctions);
			            	  		
			            	  		//$scope.resetMenu();
			            	  		$modalInstance.close();
			            		},function(error){
			            			console.log("failed");
			            			reloadPageOnce();
			            			$modalInstance.close();			            			
			            		});
			            	  
			            	  
			              });						  
						  
						  
						  
						  
						}
				};
					  
				  	  
					
				$scope.close = function() { 
					$modalInstance.close();
				};
}
