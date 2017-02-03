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
app.controller('selfProfileController', function ($scope,$http,ProfileService,$routeParams,modalService){
	$scope.tableData=[];
	$scope.profile=[];
	$scope.ociavailableRoles=[];
	$scope.ociTimeZones;
	$scope.ociCountries;
	var stateList=[];
	$scope.availableRoles = []; 
	$scope.timeZones = []; 
	$scope.selectedTimeZone = null;
	$scope.countries = []; 
	$scope.selectedCountry = null;
	$scope.isUserSystemAdmin = false;

	ProfileService.getSelfProfileDetail().then(function(data){
		var j = data;
  		$scope.data = JSON.parse(j.data);
  		$scope.profile =JSON.parse($scope.data.profile);
  		$scope.profileId = $scope.profile.id;
  		$scope.ociavailableRoles =JSON.parse($scope.data.availableRoles);
  		$scope.ociTimeZones=JSON.parse($scope.data.timeZones);
  		$scope.ociCountries=JSON.parse($scope.data.countries);
  		stateList=JSON.parse($scope.data.stateList);
  		$scope.orgUserId=$scope.profile.orgUserId;
  		$scope.orgManagerUserId=$scope.profile.orgManagerUserId;
  		
  		if($scope.ociavailableRoles)
  			$.each($scope.ociavailableRoles, function(i, a){ 
  				var availableRole = a;
  				availableRole.selected = false;
  			    $.each($scope.profile.roles, function(j, b){ 
  			    	if(a.id === b.id) {
  			    		availableRole.selected = true;
  			    		if(a.id === 1){
  			    			$scope.isUserSystemAdmin = true;
  			    		}
  			    	} 
  			    });
  			    $scope.availableRoles.push(availableRole);	    
  			});	
  			;
  		
  		/*$scope.ociTimeZones = ${model.timeZones};*/
  		
  		if($scope.ociTimeZones){
  			$.each($scope.ociTimeZones, function(i, a){ 
  				var timeZone = {"index":i, "value":a.value, "title":a.label};
  			    $scope.timeZones.push(timeZone);
  			    if($scope.profile.timeZoneId !== null && a.value === $scope.profile.timeZoneId.toString()){
  			    	$scope.selectedTimeZone = timeZone;
  				}	    
  			});	
  		};
  		
  		/*$scope.ociCountries = ${model.countries};*/
  		
  		//alert($scope.ociCountries[0].label);
  		if($scope.ociCountries)
  		$.each($scope.ociCountries, function(i, a){ 
  			var country = {"index":i, "value":a.value, "title":a.label};
  		    $scope.countries.push(country);
  		    if(a.value === $scope.profile.country){
  		    	$scope.selectedCountry = country;
  			}	    
  		});	
  		;
  		
  		/*var stateList=${model.stateList};*/
  		//alert(stateList[0].label);
  		stateList = stateList== null? []: stateList;
  		var selectedState= $scope.profile.state ? $scope.profile.state:"";
  		$scope.stateList = initDropdownWithLookUp(stateList,selectedState );
  		
  		//$scope.resetMenu();
	    },function(error){
		console.log("failed");
		reloadPageOnce();
	});

	/*$scope.profile=${model.profile};*/
	$scope.orgUserId=$scope.profile.orgUserId;
	$scope.orgManagerUserId=$scope.profile.orgManagerUserId;
	
	$scope.viewPerPage = 2;
	$scope.currentPage = 1;
	$scope.totalPage;
	$scope.searchCategory = "";
	$scope.searchString = "";
	
	$( "#dialog" ).hide();
	
	/*$scope.ociavailableRoles=${model.availableRoles};*/
	//modalService.showFailure('Error','') ; 
	
	
	$scope.saveProfile = function() {
		  var uuu = "profile/saveProfile?profile_id=" + $scope.profileId;;
		  var postData={profile: $scope.profile,
				        selectedCountry:$scope.selectedCountry!=null?$scope.selectedCountry.value:"",
				        selectedState:$scope.stateList.selected!=null?$scope.stateList.selected.value:"",
				        selectedTimeZone:$scope.selectedTimeZone!=null?$scope.selectedTimeZone.value:""
		               };
	  	  $.ajax({
	  		 type : 'POST',
	  		 url : uuu,
	  		 dataType: 'json',
	  		 contentType: 'application/json',
	  		 data: JSON.stringify(postData),
	  		 success : function(data){
	  			modalService.showSuccess("Success","Update Successful.");
			 },
			 error : function(data){
				 modalService.showFailure("Fail","Error while saving.");
			 }
	  	  });
	};
		
		$scope.addNewRolePopup = function(role) {
			$( "#dialog" ).dialog({
			      modal: true,
			      width: 500,
			      height:600
			    });
			
			$(".ui-dialog").css("z-index",10002);  
			$(".ui-dialog-titlebar").hide();     
		};
		
		$scope.toggleRole = function(selected,availableRole) {
				//alert('toggleRole: '+selected);
				if(!selected) {
					//remove role
					var uuu = "profile/removeRole?profile_id=" + $scope.profileId;;
					modalService.popupConfirmWinWithCancel("Confirm","You are about to remove the role "+availableRole.name+" from the profile for "+$scope.profile.firstName+" "+$scope.profile.lastName+". Do you want to continue?",
			    			function(){
									var postData={role:availableRole};
								  	  $.ajax({
								  		 type : 'POST',
								  		 url : uuu,
								  		 dataType: 'json',
								  		 contentType: 'application/json',
								  		 data: JSON.stringify(postData),
								  		 success : function(data){
								  			$scope.$apply(function(){$scope.profile=data;}); 
										 },
										 error : function(data){
											 modalService.showFailure("Fail","Error while saving.");
										 }
								  	  });
					},
				    function(){
				          availableRole.selected=!availableRole.selected;
					});
										
					
				} else {
					//add role
					var uuu = "profile/addNewRole?profile_id=" + $scope.profileId;;
					modalService.popupConfirmWinWithCancel("Confirm","You are about to add the role "+availableRole.name+" from the profile for "+$scope.profile.firstName+" "+$scope.profile.lastName+". Do you want to continue?",
			    			function(){
									var postData={role:availableRole};
								  	  $.ajax({
								  		 type : 'POST',
								  		 url : uuu,
								  		 dataType: 'json',
								  		 contentType: 'application/json',
								  		 data: JSON.stringify(postData),
								  		 success : function(data){
								  			$scope.$apply(function(){$scope.profile=data;}); 
										 },
										 error : function(data){
											 modalService.showFailure("Fail","Error while saving.");
										 }
								  	  });
						
			    	},function(){
				          availableRole.selected=!availableRole.selected;
			    	})
				
				}
				
				  
		};

		$scope.removeRole = function(role) {
			
			
			modalService.popupConfirmWin("Confirm","You are about to remove the role "+role.name+" from the profile for "+$scope.profile.firstName+" "+$scope.profile.lastName+". Do you want to continue?",
	    			function(){
							var uuu = "profile/removeRole?profile_id=" + $scope.profileId;;
							  var postData={role:role};
						  	  $.ajax({
						  		 type : 'POST',
						  		 url : uuu,
						  		 dataType: 'json',
						  		 contentType: 'application/json',
						  		 data: JSON.stringify(postData),
						  		 success : function(data){
						  			$scope.$apply(function(){
						  				$scope.profile=data;
						  				$.each($scope.availableRoles, function(k, c){ 
						  			    	if(c.id === role.id) {
						  			    		c.selected = false;
						  			    	}
						  			    });
						  			});
						  			
								 },
								 error : function(data){
									 modalService.showFailure("Fail","Error while saving.");
								 }
						  	  });
				
	    	})
	    	
			
		};
		
		function initDropdownWithLookUp(arr,selectedValue){
			var dropdownArray=[];
			var selected = null;
			if(arr){
				for(var i = 0,l = arr.length; i < l; i++) {
					var option = { 
					        "index" : i ,
					        "value" : arr[i].value,
					        "title" : arr[i].label
					    };
					dropdownArray.push(option);
					if(arr[i].value === selectedValue){
						selected = option;
					}
				}
			}
			var dropDown={};
			dropDown.options = dropdownArray;
			dropDown.selected = selected;
			return dropDown;
		};
		
		$scope.doRolePopup = function() {	
			 var modalInstance = $modal.open({
			    templateUrl: 'roles_popup.html',
			    controller: 'rolepopupController',
			    resolve: {
			    	 message: function () {
			    		 var message ={
			    				 availableRoles: $scope.availableRoles
		        		  };
			          return message;
			        }
			      }
			  });   
			 	modalInstance.result.then(function (opts) {
					if(opts!=null){
						$scope.profile=opts.profile;
					}		
	           });
		} 
		
		$scope.close = function(){
			$('#dialog').dialog('close');
		}

});
