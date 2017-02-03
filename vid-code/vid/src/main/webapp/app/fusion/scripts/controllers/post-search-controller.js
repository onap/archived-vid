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
app.controller('postSearchCtrl', function ($scope,$http,ProfileService,modalService){ 

		$scope.viewPerPage = 200;
		$scope.currentPage = 1;
		$scope.totalPage;
		$scope.searchCategory = "";
		$scope.searchString = "";
		
		$scope.noResultsString = "";
		
		ProfileService.getPostProfile().then(function(data){
			
			var j = data;
	  		$scope.data = JSON.parse(j.data);
	  		
	  		$scope.postSearchBean =JSON.parse($scope.data.postSearchBean);
	  		$scope.profileList =JSON.parse($scope.data.profileList);
	  		$scope.ngexistingUsers=JSON.parse($scope.data.existingUsers);
	  		$scope.ocisortByList=JSON.parse($scope.data.sortByList);
	  		
	  		//$scope.resetMenu();
		
		},function(error){
			console.log("failed");
			reloadPageOnce();
		});
		
		$scope.sortByList = [];
		if($scope.ocisortByList)
		$.each($scope.ocisortByList, function(i, a){ 
			var sortBy = {"index":i, "value":a.value, "title":a.label};
		    $scope.sortByList.push(sortBy);
		});	
		;
	
		$scope.search = function(postSearchBean) {
				console.log(postSearchBean);
				var uuu = "post_search/search";
				  var postData={postSearchBean:postSearchBean};
			  	  $.ajax({
			  		 type : 'POST',
			  		 url : uuu,
			  		 dataType: 'json',
			  		 contentType: 'application/json',
			  		 data: JSON.stringify(postData),
			  		 success : function(data){
			  			$scope.$apply(function(){
			  				$scope.profileList=data.users;
			  				//$scope.postSearchBean={};
			  				console.log($scope.profileList);
			  				if($scope.profileList.length == 0)
			  					$scope.noResultsString = "There is currently no one in WEBPHONE matching your search criteria.";
			  				else
			  					$scope.noResultsString = "";
			  				});  
					 },
					 error : function(data){
						 console.log(data);
						 modalService.showFailure("Fail","Error while searching: "+ data.responseText);
					 }
			  	  });
			
		};
		
		$scope.reset = function() {
				$scope.postSearchBean={};
				$scope.profileList=[];
				$scope.noResultsString = "";
				//console.log($scope.postSearchBean);
		};
		
		$scope.process = function() {
			$scope.prepareProfileSelection();
			var uuu = "post_search/process";
			  var postData={postSearchBean:$scope.postSearchBean};
		  	  $.ajax({
		  		 type : 'POST',
		  		 url : uuu,
		  		 dataType: 'json',
		  		 contentType: 'application/json',
		  		 data: JSON.stringify(postData),
		  		 success : function(data){
		  			$scope.$apply(function(){
		  				$scope.profileList=data.users;
		  				//$scope.postSearchBean={};
		  				$scope.ngexistingUsers=data.existingUsers;
		  				$scope.postSearchBean.selected=null;		  				
		  				});  
				 },
				 error : function(data){
					 console.log(data);
					 modalService.showFailure("Fail","Error while searching: "+ data.responseText);
				 }
		  	  });
		
	};
	$scope.importProfileList=[];
	$scope.toggleSelection = function(profile) {
		if(profile.selected)
			$scope.importProfileList.push(profile);
		else{			
			var index = $scope.importProfileList.indexOf(profile);
		 	if(index>=0)
				$scope.importProfileList.splice(index, 1);
		}
	};
	
	$scope.prepareProfileSelection = function() {
		if($scope.importProfileList)
			$.each($scope.importProfileList, function(i, profile){ 
			    $scope.preparePostSearchBean(profile);
			});	
			;
	}
	
	$scope.preparePostSearchBean = function(profile) {
		//console.log('Importing: '+profile.orgUserId);
		//console.log('ngexistinguser:'+$scope.ngexistingUsers[profile.orgUserId])
		if($scope.postSearchBean.selected==null){
			$scope.postSearchBean.selected=[];
			$scope.postSearchBean.postOrgUserId=[];
			$scope.postSearchBean.postHrid=[];
			$scope.postSearchBean.postFirstName=[];
			$scope.postSearchBean.postLastName=[];
			$scope.postSearchBean.postOrgCode=[];
			$scope.postSearchBean.postPhone=[];
			$scope.postSearchBean.postEmail=[];
			$scope.postSearchBean.postAddress1=[];
			$scope.postSearchBean.postAddress2=[];
			$scope.postSearchBean.postCity=[];
			$scope.postSearchBean.postState=[];
			$scope.postSearchBean.postZipCode=[];
			$scope.postSearchBean.postLocationClli=[];
			$scope.postSearchBean.postBusinessCountryCode=[];
			$scope.postSearchBean.postBusinessCountryName=[];
			$scope.postSearchBean.postDepartment=[];
			$scope.postSearchBean.postDepartmentName=[];
			$scope.postSearchBean.postBusinessUnit=[];
			$scope.postSearchBean.postBusinessUnitName=[];
			$scope.postSearchBean.postJobTitle=[];
			$scope.postSearchBean.postOrgManagerUserId=[];
			$scope.postSearchBean.postCommandChain=[];
			$scope.postSearchBean.postCompanyCode=[];
			$scope.postSearchBean.postCompany=[];
			$scope.postSearchBean.postCostCenter=[];
			$scope.postSearchBean.postSiloStatus=[];
			$scope.postSearchBean.postFinancialLocCode=[];
		}
			
		$scope.postSearchBean.selected.push(profile.orgUserId);	
		$scope.postSearchBean.postOrgUserId.push(profile.orgUserId);
		$scope.postSearchBean.postHrid.push(profile.hrid);
		$scope.postSearchBean.postFirstName.push(profile.firstName);
		$scope.postSearchBean.postLastName.push(profile.lastName);
		$scope.postSearchBean.postOrgCode.push(profile.orgCode);
		$scope.postSearchBean.postPhone.push(profile.phone);
		$scope.postSearchBean.postEmail.push(profile.email);
		$scope.postSearchBean.postAddress1.push(profile.address1);
		$scope.postSearchBean.postAddress2.push(profile.address2);
		$scope.postSearchBean.postCity.push(profile.city);
		$scope.postSearchBean.postState.push(profile.state);
		if(profile.zipCodeSuffix==null)
			$scope.postSearchBean.postZipCode.push(profile.zipCode);
		else
			$scope.postSearchBean.postZipCode.push(profile.zipCode+'-'+profile.zipCodeSuffix);
		$scope.postSearchBean.postLocationClli.push(profile.locationClli);
		$scope.postSearchBean.postBusinessCountryCode.push(profile.businessCountryCode);
		$scope.postSearchBean.postBusinessCountryName.push(profile.businessCountryName);
		$scope.postSearchBean.postDepartment.push(profile.department);
		$scope.postSearchBean.postDepartmentName.push(profile.departmentName);
		$scope.postSearchBean.postBusinessUnit.push(profile.businessUnit);
		$scope.postSearchBean.postBusinessUnitName.push(profile.businessUnitName);
		$scope.postSearchBean.postJobTitle.push(profile.jobTitle);
		$scope.postSearchBean.postOrgManagerUserId.push(profile.orgManagerUserId);
		$scope.postSearchBean.postCommandChain.push(profile.commandChain);
		$scope.postSearchBean.postCompanyCode.push(profile.companyCode);
		$scope.postSearchBean.postCompany.push(profile.company);
		$scope.postSearchBean.postCostCenter.push(profile.costCenter);
		$scope.postSearchBean.postSiloStatus.push(profile.siloStatus);
		$scope.postSearchBean.postFinancialLocCode.push(profile.financialLocCode);
	};
		
});	
