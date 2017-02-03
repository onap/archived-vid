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
var fn_menu_popupController =  function ($scope, $modalInstance, message, $http){ /// examine the LeftMenuService
				if(message.availableFnMenuItem==null)
					$scope.label='Add New Menu Item'
						
				else{
					$scope.label='Edit Menu Item'
					//$scope.disableParentId=true;
				}
				
				$scope.getParentData = function(){
					var uuu = "admin_fn_menu/get_parent_list"
					  	  $.ajax({
						  		 type : 'GET',
						  		 url : uuu,
						  		 dataType: 'json',				     			// data type expected from server
						  		 contentType: 'application/json',  
						  		 //data: JSON.stringify(postData),   			// data type sent to server	
						  		 success : function(data){
						  			$scope.$apply(function(){
						  				//$scope.availableRoleFunctions=[];$scope.$apply();
						  				$scope.parentListSelectData=data;		// data from server
						  				menuItems = $scope.parentListSelectData;
						  				var heirarchicalMenuItems = [];
						  				var children = [];
						  				for ( var i=0; i<menuItems.length; i++){
						  					for(var j=0; j<menuItems.length; j++){
						  						if(menuItems[j][2]==menuItems[i][0]) 
						  							children.push(
						  									{
						  									menuId: menuItems[j][0],
						  									label:  menuItems[j][1]
						  									}
						  							);
						  					}
						  					if(children.length!=0){
						  					heirarchicalMenuItems.push(
						  							{
					  									
						  									menuId: menuItems[i][0],
						  									label:  menuItems[i][1],
							  								children: children.sort($scope.sortItems("label"))
						  							}
						  					
						  					);
						  					}
						  					children = [];
						  					
						  				}
						  				heirarchicalMenuItems.sort($scope.sortItems("label"));
						  				$scope.childListSelectData = heirarchicalMenuItems;
						  				//console.log(data);
						  			});  
						  			//$scope.editRoleFunction = null;
						  			///$modalInstance.close({functionCDselectData:$scope.functionCDselectData});
								 },
								 error : function(data){
									 alert("Parent Data not available !"); 
								 }
						  	  });
					
				};
				
				$scope.sortItems = function(prop){
					return function(a, b) {  
				        if (a[prop] > b[prop]) {  
				            return 1;  
				        } else if (a[prop] < b[prop]) {  
				            return -1;  
				        }  
				        return 0;  
				    }  
									
				};
				
				$scope.getParentLabel = function(parentId, parentListSelectData){
					var element;
					element = parentListSelectData[0];
				    for (var i=0; i<parentListSelectData.length; i++){
				    	
				    	element = parentListSelectData[i];
				        if (element[0] == parentId)                    
				            return element[1];
				        else "---";
				    }
				};


				$scope.getFunctionCDselectData = function(){
					var uuu = "admin_fn_menu/get_function_cd_list"
					  	  $.ajax({
						  		 type : 'GET',
						  		 url : uuu,
						  		 dataType: 'json',				// data type expected from server
						  		 contentType: 'application/json',  
						  		 //data: JSON.stringify(postData),   // data type sent to server	
						  		 success : function(data){
						  			$scope.$apply(function(){
						  				//$scope.availableRoleFunctions=[];$scope.$apply();
						  				$scope.functionCDselectData=data;
						  			});  
						  			//alert("Update Successful.") ;
						  			//$scope.editRoleFunction = null;
						  			///$modalInstance.close({functionCDselectData:$scope.functionCDselectData});
								 },
								 error : function(data){
									 alert("Function Code Data not available !"); 
								 }
						  	  });
					
				};
				
				if(message.availableFnMenuItem){
					$scope.addFnMenuItem = message.availableFnMenuItem;
				} else {
					$scope.addFnMenuItem = {};
				}
				$scope.addFnMenuItem.menuSetCode = "APP";
				
			    $scope.statusOptions = [        
				                        {index: 0, value: 'true', title: 'Y'},
				                        {index: 1, value: 'false', title: 'N'}
				                    ];
				$scope.selectedValue = $scope.statusOptions[0];
				
				$scope.separator = {
							availableOptions:[
											    {value: 'true',  name: 'Y'},
											    {value: 'false', name: 'N'}
											 ],
											 
											 selectedOption: {value: 'true', name: 'Y'}
						              
									};
					
					
			    $scope.activeStatusOptions = [        
				                        {index: 0, value: 'true', title: 'Y'},
				                        {index: 1, value: 'false', title: 'N'}
				                    ];
			    $scope.activeSelectedValue = $scope.activeStatusOptions[0];
				    
			    $scope.separatorStatusOptions = [        
							                        {index: 0, value: 'true', title: 'Y'},
							                        {index: 1, value: 'false', title: 'N'}
							                    ];
			    $scope.separatorSelectedValue = $scope.separatorStatusOptions[1];				    
				
				$scope.active = {
							availableOptions:[
											    {value: 'true',  name: 'Y'},
											    {value: 'false', name: 'N'}
											 ],
											 
											 selectedOption: {value: 'true', name: 'Y'}
						              
									};
				
	  			
				$scope.updateFnMenu = function(availableFnMenuItem) { 
						// form validation
						if( document.getElementById("popupAddMenuItemImageSrc").value == "" ) availableFnMenuItem.imageSrc = "N/A";
						if( document.getElementById("popupAddMenuItemTarget").value == "" ) availableFnMenuItem.target = "N/A";
						if( document.getElementById("popupAddMenuItemExternalUrl").value == "" ) availableFnMenuItem.externalUrl = "N/A";
						if( document.getElementById("popupAddMenuItemQueryString").value == "" ) availableFnMenuItem.queryString = "N/A";
						if( document.getElementById("popupAddMenuItemServlet").value == "" ) availableFnMenuItem.servlet = "N/A";
						
						validationRule = /^\S{3,}$/;
						var selectedFunction = document.getElementById("repeatSelect");
						var selectedFunctionText = selectedFunction.options[selectedFunction.selectedIndex].text;
						if(
								(document.getElementById("popupAddMenuItemLabel").value == "" || document.getElementById("popupAddMenuItemLabel").value.replace(" ", "").length == 0 ) ||//!validationRule.test(document.getElementById("popupAddMenuItemLabel").value)) ||
								document.getElementById("popupAddMenuItemParentId").value == "" ||
								document.getElementById("popupAddMenuItemAction").value == "" ||
								selectedFunctionText == "" ||
								document.getElementById("popupAddMenuItemSortOrder").value == "" ||
								document.getElementById("popupAddMenuItemMenuSetCode").value == ""
						  )	
							{
								alert("please provide valid entries !");  
							}
						
						else{ 
								  var uuu = "admin_fn_menu/updateFnMenu.htm";
								  var postData={availableFnMenuItem: availableFnMenuItem};
								  
							  	  $.ajax({
							  		 type : 'POST',
							  		 url : uuu,
							  		 //dataType: 'json',				// data type expected from server
							  		 contentType: 'application/json',  
							  		 data: JSON.stringify(postData),    // data type sent to server	
							  		 success : function(data){
							  			$scope.$apply(function(){ 
							  			$scope.availableFnMenuItems=data.availableFnMenuItems;
							  			});
							  			
							  			$modalInstance.close({availableFnMenuItems:$scope.availableRoleFunctions});
									 },
									 error : function(data){
										 alert("Error while saving."); 
									 }
							  	  }); 

							}
						$scope.getLeftMenuItems();						// get left menu items again from database

					};

					$scope.getLeftMenuItems = function() {
																return $http.get('get_menu')
																.then(function(response) {
																		if (typeof response.data === 'object') {
																        	var leftChildData=[];
																        	var leftParentData=[];
																        	var leftMenuItems = [];
																  			var j = response; 
															    	  		try{
															    	  			if(j && j !== "null" && j!== "undefined"){
															    	  				leftParentData = JSON.parse(j.data);
															    	      		    leftChildData = JSON.parse(j.data2);
															    	  			}else{
															    	  				throw "Get Left Menu respsone is not an object/is empty"; 
															    	  			}  
															      		    	try{
															        	  			var leftChildItemList = leftChildData; 											
															                  		var pageUrl = window.location.href.split('/')[window.location.href.split('/').length-1];
															                 		var leftParentList = leftParentData;												
															                 		for (var i = 0; i < leftParentList.length; i++) {
															                 			$scope.item = {
															                 				parentLabel : leftParentList[i].label,
															                 				parentAction : leftParentList[i].action,
															                 				parentImageSrc : leftParentList[i].imageSrc,                 				
															                 				open:pageUrl==leftParentList[i].action?true:false,
															                 				childItemList : leftChildItemList[i]==null?'x':leftChildItemList[i]
															                 			}
															                 			leftMenuItems.push($scope.item); 											
															                 		};
															                 		
															                 		$scope.leftMenuItems = leftMenuItems;
										
															        	  		}catch(err){
															        	  			console.log("error happened while trying to set left menu structure: "+err);  					   
															        	  		}
															    	  		}catch (e) {
															    	  			console.log("error happened while trying to get left menu items: "+e);
															    	  			reloadPageOnce();
															    	  			return;
															    	        }	
																			return response.data;
																		} else {
																			return $q.reject(response.data);
																		}
																}, function(response) {
																	// something went wrong
																	return $q.reject(response.data);
																});
					};

				$scope.close = function() {
					$modalInstance.close();
				};
}
